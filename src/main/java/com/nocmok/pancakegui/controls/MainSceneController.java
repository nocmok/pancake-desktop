package com.nocmok.pancakegui.controls;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Map.Entry;

import com.nocmok.pancake.Pancake;
import com.nocmok.pancake.PancakeBand;
import com.nocmok.pancake.PancakeDataset;
import com.nocmok.pancake.PansharpJob;
import com.nocmok.pancake.PansharpJobBuilder;
import com.nocmok.pancake.Spectrum;
import com.nocmok.pancake.PancakeProgressListener;

import com.nocmok.pancakegui.PancakeApp;
import com.nocmok.pancakegui.Session;
import com.nocmok.pancakegui.pojo.SourceInfo;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;

public class MainSceneController extends ControllerBase {

    public static MainSceneController getNew() {
        return ControllerBase.getNew("main_scene_layout.fxml");
    }

    @FXML
    private GridPane imageSources;

    @FXML
    private ImageSourcesController imageSourcesController;

    @FXML
    private ImageExplorer imageExplorer;

    @FXML
    private HBox toolbar;

    @FXML
    private ToolbarController toolbarController;

    private Parent root;

    private Session session;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        session = PancakeApp.app().session();

        imageExplorer.setPlaceholder(new Label("no image to display"));

        toolbarController.setOnPlayButtonClickedHandler(this::pansharp);
        toolbarController.setOnZoomInButtonClickedHandler(this::zoomIn);
        toolbarController.setOnZoomOutButtonClickedHandler(this::zoomOut);

    }

    public void pansharp() {
        FileChooser fChooser = new FileChooser();
        File dstFile = fChooser.showSaveDialog(PancakeApp.app().primaryStage());
        if (dstFile == null) {
            return;
        }
        PansharpDialogController controller = PansharpDialogController.getNew();
        if (controller == null) {
            return;
        }
        PansharpJobBuilder jobBuilder = controller.runDialog();
        if (jobBuilder == null) {
            return;
        }
        ProgressDialogController progress = ProgressDialogController.getNew();
        if (progress == null) {
            return;
        }
        progress.setProgress(1f);
        progress.setMessage("start processing ...");
        progress.runDialog();

        PancakeApp.app().worker().submit(() -> {
            List<SourceInfo> ds = session.minifyDataset();
            Map<Spectrum, PancakeBand> source = new EnumMap<>(Spectrum.class);
            List<PancakeDataset> datasets = new ArrayList<>();
            try {
                for (SourceInfo si : ds) {
                    PancakeDataset pnkDs = Pancake.open(si.path(), Pancake.ACCESS_READONLY);
                    datasets.add(pnkDs);
                    for (Entry<Integer, Spectrum> entry : si.mapping().entrySet()) {
                        source.put(entry.getValue(), pnkDs.bands().get(entry.getKey()));
                    }
                }
                PansharpJob job = jobBuilder.withOutputFile(dstFile).withSource(source).build();
                job.setProgressListener(new PancakeProgressListener() {
                    @Override
                    public void listen(Integer phase, Double p, String message) {
                        Platform.runLater(() -> {
                            log(progress, phase, p, message);
                        });
                    }
                });
                PancakeDataset result = job.pansharp();
                result.flushCache();
                datasets.add(result);
                Platform.runLater(() -> {
                    progress.closeDialog();
                });
            } catch (RuntimeException e) {
                e.printStackTrace();
            } finally {
                try {
                    for (PancakeDataset pnkDs : datasets) {
                        pnkDs.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void log(ProgressDialogController frame, int phase, double progress, String message) {
        frame.setMessage(message);
        frame.setProgress(progress);
    }

    public void zoomIn() {

    }

    public void zoomOut() {

    }

    @Override
    protected void setRoot(Parent root) {
        this.root = root;
    }

    @Override
    public Parent root() {
        return root;
    }

}
