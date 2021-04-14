package com.nocmok.pancakegui.controls.imgviewer;

import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.concurrent.Worker;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * Represents an image "Tile" in a two-dimensional array, with support for
 * background loading of the images. Images are created via a user-supplied
 * function, which is executed in a background thread.
 *
 */
class Tile {

    private int x;
    private int y;
    private final ImageView view;
    private final Service<Image> imageService;

    /**
     * 
     * Creates a new tile, with the given location and (fixed) dimensions. The
     * imageSupplier will be invoked in a background thread and the resulting image
     * displayed in the view on successful completion.
     * 
     * @param x             Initial x-coordinate of tile.
     * @param y             Initial y-coordinate of tile.
     * @param width         Width of tile in pixels (fixed).
     * @param height        Height of tile in pixels (fixed).
     * @param imageSupplier Function for creating images, which will be invoked on a
     *                      background thread.
     * 
     */
    public Tile(int x, int y, double width, double height, PanningTiledPane.ImageSupplier imageSupplier) {
        this.x = x;
        this.y = y;
        this.view = new ImageView();
        view.setFitWidth(width);
        view.setFitHeight(height);
        view.setPreserveRatio(true);

        this.imageService = new Service<Image>() {
            @Override
            protected Task<Image> createTask() {
                final int tileX = getX();
                final int tileY = getY();
                return new Task<Image>() {
                    @Override
                    protected Image call() throws Exception {
                        return imageSupplier.getImage(tileX, tileY);
                    }
                };
            }
        };
        this.imageService.stateProperty().addListener((obs, oldState, newState) -> {
            if (newState == Worker.State.SUCCEEDED) {
                view.setImage(imageService.getValue());
            } else {
                view.setImage(null);
            }
        });
        imageService.start();
    }

    /**
     * Sets the location and updates the image with an existing image.
     * 
     * @param x     The new x-coordinate in the grid.
     * @param y     The new y-coordinate in the grid.
     * @param image The new image. If this is null, a new image will be loaded in
     *              the background.
     */
    public void setLocationAndImage(int x, int y, Image image) {
        this.x = x;
        this.y = y;
        if (image == null) {
            imageService.restart();
        } else {
            imageService.cancel();
            view.setImage(image);
        }
    }

    /**
     * Sets the location and loads a new image in the background.
     * 
     * @param x
     * @param y
     */
    public void setLocation(int x, int y) {
        setLocationAndImage(x, y, null);
    }

    /**
     * 
     * @return The current image.
     */
    public Image getImage() {
        return view.getImage();
    }

    /**
     * 
     * @return The view for this tile.
     */
    public Node getView() {
        return view;
    }

    /**
     * 
     * @return The current x-coordinate.
     */
    public int getX() {
        return x;
    }

    /**
     * 
     * @return The current y-coordinate.
     */
    public int getY() {
        return y;
    }

}
