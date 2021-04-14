package com.nocmok.pancakegui.controls;

import javafx.scene.Node;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;

public class ClipPane extends StackPane {

    private Rectangle clip;

    public ClipPane() {
        this.clip = new Rectangle();
        clip.widthProperty().bind(this.widthProperty());
        clip.heightProperty().bind(this.heightProperty());
    }

    public void addChild(Node child) {
        child.setClip(clip);
        getChildren().add(child);
    }
}
