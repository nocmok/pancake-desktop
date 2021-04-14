package com.nocmok.pancakegui.controls.imgviewer;

import com.nocmok.pancakegui.pojo.ImageInfo;
import com.nocmok.pancakegui.utils.ImageUtils;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;

public class TileFactory implements PanningTiledPane.ImageSupplier {

    private ImageInfo imgInfo;

    private double zoom;

    private int gridCols;

    private int gridRows;

    private int imgTileSize;

    private int gridTileSize;

    private Image placeHolder;

    public TileFactory(int gridCols, int gridRows, int tileSize) {
        this.zoom = 1d;
        this.gridCols = gridCols;
        this.gridRows = gridRows;
        this.gridTileSize = tileSize;
        this.placeHolder = getPlaceHolder(tileSize, tileSize);
    }

    public void setZoom(double zoom) {
        this.zoom = Double.max(1, zoom);
        if (zoom >= 1) {
            setImage(imgInfo);
        }
    }

    public void setImage(ImageInfo info) {
        this.imgInfo = info;
        if (info == null) {
            return;
        }
        this.imgTileSize = computeTileSize(imgInfo, gridCols, gridRows, zoom);
    }

    public ImageInfo getImage() {
        return imgInfo;
    }

    public double getZoom() {
        return this.zoom;
    }

    private int computeTileSize(ImageInfo imgInfo, int gridCols, int gridRows, double zoom) {
        int size;
        if (imgInfo.getXsize() < imgInfo.getYsize()) {
            size = (int) (imgInfo.getYsize() / gridCols / zoom);
        } else {
            size = (int) (imgInfo.getXsize() / gridRows / zoom);
        }
        return size;
    }

    // private int clampX(int x0) {
    // if (x0 + imgTileSize > imgInfo.getXsize()) {
    // return Integer.max(0, imgInfo.getXsize() - x0);
    // }
    // if(x0 < 0){
    // return
    // }
    // return imgTileSize;
    // }

    // private int clampY(int y0) {
    // if (y0 + imgTileSize > imgInfo.getYsize()) {
    // return Integer.max(0, imgInfo.getYsize() - y0);
    // }
    // return imgTileSize;
    // }

    private Image getPlaceHolder(int width, int height) {
        Label label = new Label("X");
        label.setPrefSize(width, height);
        label.setMaxSize(width, height);
        label.setAlignment(Pos.CENTER);
        label.setStyle(String.format("-fx-background: %s; " + "-fx-background-color: -fx-background;", "white"));
        return label.snapshot(null, null);
    }

    @Override
    public Image getImage(int tileX, int tileY) throws InterruptedException {
        if (imgInfo == null) {
            throw new InterruptedException();
        }

        int x = tileX * imgTileSize;
        int y = tileY * imgTileSize;

        int xsize = imgTileSize;
        int ysize = imgTileSize;

        if (x + xsize >= imgInfo.getXsize()) {
            xsize = Integer.max(0, imgInfo.getXsize() - x);
        }
        if (y + ysize >= imgInfo.getYsize()) {
            ysize = Integer.max(0, imgInfo.getYsize() - y);
        }

        if (xsize == 0 || ysize == 0) {
            throw new InterruptedException();
        }

        int renderXSize = (int) (gridTileSize * xsize / imgTileSize);
        int renderYSize = (int) (gridTileSize * ysize / imgTileSize);

        Image image = ImageUtils.get().readRegion(imgInfo.getPath(), x, y, xsize, ysize, renderXSize, renderYSize);
        return image;
    }

}
