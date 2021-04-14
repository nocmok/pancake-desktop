package com.nocmok.pancakegui.pojo;

import java.io.File;

public class ImageInfo {

    private int xsize;

    private int ysize;

    private File path;

    private int nBands;

    private String imageFormat;

    public ImageInfo(int xsize, int ysize, File path, int nBands, String imageFormat) {
        this.xsize = xsize;
        this.ysize = ysize;
        this.path = path;
        this.nBands = nBands;
        this.imageFormat = imageFormat;
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
