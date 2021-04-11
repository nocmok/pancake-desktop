package com.nocmok.pancakegui.controls;

import java.io.File;
import java.net.URL;
import java.util.Map;
import java.util.ResourceBundle;

import com.nocmok.pancakegui.PancakeApp;
import com.nocmok.pancakegui.pojo.SourceInfo;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;

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

    public void addImageSource(SourceInfo sourceInfo) {
        ImageSourceController newSource = ImageSourceController.getNew();
        if (newSource == null) {
            return;
        }
        newSource.setSourceInfo(sourceInfo);
        sourceListVBox.getChildren().add(newSource.root());
    }

    public void removeImageSource(SourceInfo info) {
        throw new UnsupportedOperationException("not implemented");
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        addSourceButton.addEventHandler(MouseEvent.MOUSE_CLICKED, this::onAddSourceClickedEventHandler);
    }

    /** TODO */
    private void notifyBandMappingInvalid() {
        System.out.println(":::Pancake::: Invalid band mapping");
    }

    /** TODO */
    private void notifyCannotAddToDataset() {
        System.out.println(":::Pancake::: Cannot add to dataset");
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
        Map<Integer, String> mapping = dialogController.runDialog(file);
        if (mapping == null) {
            return;
        }
        if (mapping.isEmpty() || !SourceInfo.isValidMappingStr(mapping)) {
            notifyBandMappingInvalid();
            return;
        }
        SourceInfo sourceInfo = SourceInfo.of(file, mapping);
        if (!PancakeApp.app().session().addSource(sourceInfo)) {
            notifyCannotAddToDataset();
            return;
        }
        addImageSource(sourceInfo);
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
