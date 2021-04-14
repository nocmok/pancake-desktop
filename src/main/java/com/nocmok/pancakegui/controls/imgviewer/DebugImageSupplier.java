package com.nocmok.pancakegui.controls.imgviewer;

import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;

public class DebugImageSupplier implements PanningTiledPane.ImageSupplier {

    private Random rng = new Random();

    private int TILE_WIDTH;

    private int TILE_HEIGHT;

    public DebugImageSupplier(int tileWidth, int tileHeight) {
        this.TILE_WIDTH = tileWidth;
        this.TILE_HEIGHT = tileHeight;
    }

    private String randomColorString() {
        return String.format("#%02x%02x%02x", rng.nextInt(256), rng.nextInt(256), rng.nextInt(256));
    }

    @Override
    public Image getImage(int column, int row) throws InterruptedException {
        // simulate slow loading from database, etc:
        Random rng = new Random();
        Thread.sleep(rng.nextInt(1000));

        // little hack to create image from snapshot and return it when we're running
        // on background thread (snapshot must be called from FX Application Thread):

        FutureTask<Image> runOnFXThread = new FutureTask<Image>(() -> {
            Label label = new Label(String.format("Tile [%d,%d]", column, row));
            label.setPrefSize(TILE_WIDTH, TILE_HEIGHT);
            label.setMaxSize(TILE_WIDTH, TILE_HEIGHT);
            label.setAlignment(Pos.CENTER);
            label.setStyle(String.format("-fx-background: %s; " + "-fx-background-color: -fx-background;",
                    randomColorString()));

            // must add label to a scene for background to work:
            new Scene(label);
            return label.snapshot(null, null);
        });

        Platform.runLater(runOnFXThread);
        try {
            return runOnFXThread.get();
        } catch (ExecutionException e) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "Error creating image", e);
            return null;
        }
    }

}
