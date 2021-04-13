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

    private static final LRUCache<File, ImageInfo> imageInfoCache = new LRUCache<File, ImageInfo>(10);

    public static ImageUtils get() {
        return singleton;
    }

    private ImageUtils() {
    }

    public Image getImage() {
        return null;
    }

    public void getInfo(File imgFile, Consumer<ImageInfo> listener) {
        ImageInfo infoInCache = imageInfoCache.get(imgFile);
        if (infoInCache != null) {
            listener.accept(infoInCache);
        }
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

    public ImageInfo getInfo(File imgFile) {
        ImageInfo ii = imageInfoCache.get(imgFile);
        if (ii != null) {
            return ii;
        }
        Image overview = getImageThumbnail(imgFile, 100, 100);
        PancakeDataset dataset = Pancake.open(imgFile, Pancake.ACCESS_READONLY);
        ii = new ImageInfo(overview, dataset.xSize(), dataset.ySize(), imgFile, dataset.bands().size(),
                dataset.formatString());
        imageInfoCache.add(imgFile, ii);
        return ii;
    }
}
