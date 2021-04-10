package com.nocmok.pancakegui.utils;

import java.io.File;
import java.io.IOException;
import java.util.function.Consumer;

import com.nocmok.pancake.Pancake;
import com.nocmok.pancake.PancakeDataset;
import com.nocmok.pancakegui.PancakeApp;
import com.nocmok.pancakegui.pojo.ImageInfo;

import javafx.application.Platform;
import javafx.scene.image.Image;

public class ImageUtils {

    private static final ImageUtils singleton = new ImageUtils();

    public static ImageUtils get() {
        return singleton;
    }

    private ImageUtils() {
    }

    public Image getImage() {
        return null;
    }

    public void getInfo(File imgFile, Consumer<ImageInfo> listener) {
        PancakeApp.app().worker().submit(() -> {
            ImageInfo info = getInfo(imgFile);
            Platform.runLater(() -> listener.accept(info));
        });
    }

    public Image getImageThumbnail(File imgFile, int width, int height) {
        try {
            return new Image(imgFile.toURI().toURL().toString(), width, height, true, false, false);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /** TODO (image format detection) */
    public ImageInfo getInfo(File imgFile) {
        Image overview = getImageThumbnail(imgFile, 100, 100);
        PancakeDataset dataset = Pancake.open(imgFile, Pancake.ACCESS_READONLY);
        ImageInfo ii = new ImageInfo(overview, dataset.bands().get(0).getXSize(), dataset.bands().get(0).getYSize(),
                imgFile, dataset.bands().size(), "unknown");
        return ii;
    }
}
