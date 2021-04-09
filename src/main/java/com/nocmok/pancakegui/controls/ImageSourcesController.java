package com.nocmok.pancakegui.controls;

import java.io.File;
import java.net.URL;
import java.util.Map;
import java.util.ResourceBundle;

import com.nocmok.pancakegui.PancakeApp;
import com.nocmok.pancakegui.pojo.ImageInfo;
import com.nocmok.pancakegui.pojo.SourceInfo;
import com.nocmok.pancakegui.utils.ImageUtils;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class ImageSourcesController extends ControllerBase implements Initializable {

    public static ImageSourcesController getNew() {
        return ControllerBase.getNew("image_sources_layout.fxml");
    }

    @FXML
    private ImageView imageOverview;

    @FXML
    private Label headerLabel;

    @FXML
    private VBox sourceListVBox;

    @FXML
    private Button addSourceButton;

    private Parent root;

    public ImageSourcesController() {

    }

    public void addImageSource(SourceInfo sourceInfo, ImageInfo imageInfo) {
        ImageSourceController newSource = ImageSourceController.getNew();
        if (newSource == null) {
            return;
        }
        newSource.setSourceInfo(sourceInfo);
        newSource.setImageInfo(imageInfo);
        sourceListVBox.getChildren().add(newSource.root());
    }

    public void removeImageSource(SourceInfo info) {
        throw new UnsupportedOperationException("not implemented");
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        addSourceButton.addEventHandler(MouseEvent.MOUSE_CLICKED, this::onAddSourceClickedEventHandler);
    }

    private void onAddSourceClickedEventHandler(MouseEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose image file");
        File file = fileChooser.showOpenDialog(PancakeApp.app().primaryStage());
        if (file == null) {
            return;
        }
        SourceInfoDialogController dialogController = SourceInfoDialogController.getNew();
        if (dialogController == null) {
            return;
        }
        Stage dialog = new Stage();
        dialog.setScene(new Scene(dialogController.root()));
        dialog.setResizable(false);
        dialog.initStyle(StageStyle.UTILITY);
        dialog.initModality(Modality.WINDOW_MODAL);

        ImageUtils.get().getInfo(file, dialogController::setInfo);

        dialog.showAndWait();

        ImageInfo imageInfo = dialogController.getInfo();
        Map<Integer, String> mapping = dialogController.mapping();
        if (mapping == null) {
            return;
        }
        SourceInfo sourceInfo = SourceInfo.of(file, mapping);
        addImageSource(sourceInfo, imageInfo);
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
