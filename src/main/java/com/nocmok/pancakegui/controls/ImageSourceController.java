package com.nocmok.pancakegui.controls;

import java.net.URL;
import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.ResourceBundle;

import com.nocmok.pancake.Spectrum;
import com.nocmok.pancakegui.pojo.ImageInfo;
import com.nocmok.pancakegui.pojo.SourceInfo;
import com.nocmok.pancakegui.utils.ImageUtils;

import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class ImageSourceController extends ControllerBase {

    public static ImageSourceController getNew() {
        return ControllerBase.getNew("image_source_layout.fxml");
    }

    @FXML
    private ImageView imageOverview;

    @FXML
    private Label imageName;

    @FXML
    private Label imageResolution;

    @FXML
    private Label imagePhotometry;

    @FXML
    private MenuButton moreButton;

    @FXML
    private MenuItem editItem;

    @FXML
    private MenuItem removeItem;

    @FXML
    private Parent root;

    private SourceInfo sourceInfo;

    private Runnable onEditListener = () -> {
    };

    private Runnable onRemoveListener = () -> {
    };

    private Runnable onSelectedListener = () -> {
    };

    public ImageSourceController() {
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        editItem.setOnAction((e) -> {
            e.consume();
            getOnEditListener().run();
        });
        removeItem.setOnAction((e) -> {
            e.consume();
            getOnRemoveListener().run();
        });
    }

    private Runnable getOnEditListener() {
        return onEditListener;
    }

    private Runnable getOnRemoveListener() {
        return onRemoveListener;
    }

    public void setOnEditListener(Runnable listener) {
        this.onEditListener = Optional.ofNullable(listener).orElse(() -> {
        });
    }

    public void setOnRemoveListener(Runnable listener) {
        this.onRemoveListener = Optional.ofNullable(listener).orElse(() -> {
        });
    }

    public void setOnSelectedListener(Runnable listener) {
        this.onSelectedListener = Optional.ofNullable(listener).orElse(() -> {
        });
    }

    private String mappingToString(Map<Integer, Spectrum> mapping) {
        StringBuilder photometry = new StringBuilder();
        Integer[] bands = mapping.keySet().toArray(new Integer[0]);
        Arrays.sort(bands);
        for (Integer band : bands) {
            photometry.append(band);
            photometry.append("[");
            photometry.append(mapping.get(band).getName().toUpperCase());
            photometry.append("]");
        }
        return photometry.toString();
    }

    public void setSourceInfo(SourceInfo info) {
        this.sourceInfo = info;
        imageName.setText(info.path().getName());
        imagePhotometry.setText(mappingToString(info.mapping()));
        ImageUtils.get().getInfo(info.path(), this::setImageInfo);
        ImageUtils.get().getImageThumbnail(info.path(), 100, 100, this::setOverview);
    }

    public void setImageInfo(ImageInfo info) {
        imageResolution.setText(info.getXsize() + "x" + info.getYsize());
    }

    public void setOverview(Image overview) {
        imageOverview.setImage(overview);
    }

    public void select() {
        if (root.getStyleClass().size() > 1) {
            root.getStyleClass().remove(root.getStyleClass().size() - 1);
        }
        root.getStyleClass().add("pnk-cloud-selected");
        onSelectedListener.run();
    }

    public void unselect() {
        if (root.getStyleClass().size() > 1) {
            root.getStyleClass().remove(root.getStyleClass().size() - 1);
        }
        root.getStyleClass().add("pnk-cloud");
    }

    public SourceInfo getSourceInfo() {
        return sourceInfo;
    }

    public Parent root() {
        return root;
    }

    @Override
    protected void setRoot(Parent root) {
    }
}
