package com.nocmok.pancakegui.controls;

import java.io.File;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.function.Consumer;

import com.nocmok.pancake.Spectrum;
import com.nocmok.pancakegui.PancakeApp;
import com.nocmok.pancakegui.pojo.SourceInfo;

import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;

public class ImageSourcesController extends ControllerBase {

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

    @FXML
    private Parent root;

    private ImageSourceController selectedItem;

    private Map<SourceInfo, ImageSourceController> items;

    private Consumer<SourceInfo> onItemSelectedListener = (i) -> {
    };

    public ImageSourcesController() {
        items = new HashMap<>();
    }

    public void addImageSource(SourceInfo sourceInfo) {
        ImageSourceController newSource = ImageSourceController.getNew();
        items.put(sourceInfo, newSource);
        if (newSource == null) {
            return;
        }

        newSource.setSourceInfo(sourceInfo);
        newSource.root().setOnMouseClicked((e) -> selectItem(newSource));

        sourceListVBox.getChildren().add(newSource.root());
        newSource.setOnEditListener(() -> edit(newSource));
        newSource.setOnRemoveListener(() -> remove(newSource));
        newSource.setOnSelectedListener(() -> {
            getOnItemSelectedListener().accept(sourceInfo);
        });
    }

    private void edit(ImageSourceController source) {
        SourceInfoDialogController controller = SourceInfoDialogController.getNew();
        Map<Integer, Spectrum> mapping = controller.runDialog(source.getSourceInfo().path());
        if (mapping == null) {
            return;
        }
        SourceInfo newSourceInfo = new SourceInfo(source.getSourceInfo().path(), mapping);
        PancakeApp.app().session().removeSource(source.getSourceInfo());
        if (!PancakeApp.app().session().addSource(newSourceInfo)) {
            PancakeApp.app().session().addSource(source.getSourceInfo());
            notifyCannotAddToDataset();
            return;
        }
        source.setSourceInfo(newSourceInfo);
    }

    private void remove(ImageSourceController source) {
        PancakeApp.app().session().removeSource(source.getSourceInfo());
        items.remove(source.getSourceInfo());
        source.root().setManaged(false);
        source.root().setVisible(false);
        sourceListVBox.getChildren().remove(source.root());
    }

    public void setOnItemSelectedListener(Consumer<SourceInfo> listener) {
        this.onItemSelectedListener = Optional.ofNullable(listener).orElse((i) -> {
        });
    }

    public Consumer<SourceInfo> getOnItemSelectedListener() {
        return this.onItemSelectedListener;
    }

    public void selectItem(ImageSourceController item) {
        if (item == null) {
            return;
        }
        item.select();
        if (selectedItem != null) {
            selectedItem.unselect();
        }
        selectedItem = item;
    }

    public void selectItem(SourceInfo sourceInfo) {
        ImageSourceController item = items.get(sourceInfo);
        selectItem(item);
    }

    public void removeImageSource(SourceInfo info) {
        ImageSourceController item = items.get(info);
        if (item == null) {
            return;
        }
        remove(item);
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
        Map<Integer, Spectrum> mapping = dialogController.runDialog(file);
        if (mapping == null) {
            return;
        }
        if (mapping.isEmpty() || !SourceInfo.isValidMapping(mapping)) {
            notifyBandMappingInvalid();
            return;
        }
        SourceInfo sourceInfo = new SourceInfo(file, mapping);
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
