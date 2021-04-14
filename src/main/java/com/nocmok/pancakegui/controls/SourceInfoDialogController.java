package com.nocmok.pancakegui.controls;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import com.nocmok.pancake.Spectrum;
import com.nocmok.pancakegui.PancakeApp;
import com.nocmok.pancakegui.pojo.ImageInfo;
import com.nocmok.pancakegui.utils.ImageUtils;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class SourceInfoDialogController extends ControllerBase {

    public static SourceInfoDialogController getNew() {
        return ControllerBase.getNew("source_info_dialog_layout.fxml");
    }

    @FXML
    private ImageView imageOverview;

    @FXML
    private Label imageName;

    @FXML
    private Label imageResolution;

    @FXML
    private Label imageFormat;

    @FXML
    private VBox bandList;

    @FXML
    private Button addButton;

    private Map<Integer, Spectrum> mapping;

    private Parent root;

    private List<DropDownOptionController<Spectrum>> mappingControllers;

    public SourceInfoDialogController() {
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        addButton.addEventHandler(MouseEvent.MOUSE_CLICKED, this::onAddSourceClicked);
    }

    public Map<Integer, Spectrum> runDialog(File imgFile) {
        Stage dialog = new Stage();
        dialog.setScene(new Scene(root));
        dialog.setResizable(false);
        dialog.initStyle(StageStyle.UTILITY);
        dialog.initOwner(PancakeApp.app().primaryStage());
        dialog.initModality(Modality.WINDOW_MODAL);
        ImageUtils.get().getInfo(imgFile, this::setInfo);
        ImageUtils.get().getImageThumbnail(imgFile, 100, 100, this::setOverview);
        dialog.showAndWait();
        return mapping;
    }

    private Map<Integer, Spectrum> parseMapping() {
        if (mappingControllers == null) {
            return Collections.emptyMap();
        }
        Map<Integer, Spectrum> mapping = new HashMap<>();
        for (int i = 0; i < mappingControllers.size(); ++i) {
            if (mappingControllers.get(i) == null) {
                continue;
            }
            if (!mappingControllers.get(i).isAnySelected()) {
                continue;
            }
            Spectrum value = mappingControllers.get(i).getSelected();
            if (value.equals(Spectrum.NONE)) {
                continue;
            }
            mapping.put(i, value);
        }
        return mapping;
    }

    private void onAddSourceClicked(MouseEvent event) {
        this.mapping = parseMapping();
        Node source = (Node) event.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
    }

    private void setOverview(Image image) {
        imageOverview.setImage(image);
    }

    private void setInfo(ImageInfo info) {
        bandList.getChildren().clear();

        imageName.setText(info.getPath().getName());
        imageResolution.setText(info.getXsize() + "x" + info.getYsize());
        imageFormat.setText(info.getImageFormat());

        mappingControllers = new ArrayList<DropDownOptionController<Spectrum>>(info.getnBands());
        for (int i = 0; i < info.getnBands(); ++i) {
            DropDownOptionController<Spectrum> controller = DropDownOptionController.getNew();
            mappingControllers.add(controller);
            controller.setOptionName("band#" + i);
            controller.setOptions(Spectrum.all());
            controller.setDefault(Spectrum.NONE);
            bandList.getChildren().add(controller.root());
        }
    }

    public Map<Integer, Spectrum> mapping() {
        return mapping;
    }

    public Parent root() {
        return root;
    }

    @Override
    protected void setRoot(Parent root) {
        this.root = root;
    }
}
