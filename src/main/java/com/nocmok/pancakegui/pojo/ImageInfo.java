package com.nocmok.pancakegui.pojo;

import java.io.File;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;

public class ImageInfo {

    // private Image overview;

    private int xsize;

    private int ysize;

    private File path;

    private int nBands;

    private String imageFormat;

    public ImageInfo(int xsize, int ysize, File path, int nBands, String imageFormat) {
        // this.overview = overview;
        this.xsize = xsize;
        this.ysize = ysize;
        this.path = path;
        this.nBands = nBands;
        this.imageFormat = imageFormat;
    }

    /** TODO */
    public Image getOverview() {
        Label label = new Label("X");
        label.setPrefSize(100, 100);
        label.setMaxSize(100, 100);
        label.setAlignment(Pos.CENTER);
        label.setStyle(String.format("-fx-background: %s; " + "-fx-background-color: -fx-background;", "blue"));
        return label.snapshot(null, null);
    }

    public int getXsize() {
        return xsize;
    }

    public int getYsize() {
        return ysize;
    }

    public File getPath() {
        return path;
    }

    public int getnBands() {
        return nBands;
    }

    public String getImageFormat() {
        return imageFormat;
    }
}
