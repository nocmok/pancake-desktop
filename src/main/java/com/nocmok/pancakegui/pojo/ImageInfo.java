package com.nocmok.pancakegui.pojo;

import java.io.File;

import javafx.scene.image.Image;

public class ImageInfo {

    private Image overview;

    private int xsize;

    private int ysize;

    private File path;

    private int nBands;

    private String imageFormat;

    public ImageInfo(Image overview, int xsize, int ysize, File path, int nBands, String imageFormat) {
        this.overview = overview;
        this.xsize = xsize;
        this.ysize = ysize;
        this.path = path;
        this.nBands = nBands;
        this.imageFormat = imageFormat;
    }

    public Image getOverview() {
        return overview;
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
