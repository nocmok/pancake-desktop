package com.nocmok.pancakegui.controls;

import java.io.File;
import java.io.IOException;
import java.util.Optional;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;

public class ImageExplorer extends StackPane {

    private ImageView imageView;

    private Node placeholder = new Label();

    public ImageExplorer() {
        this.imageView = new ImageView();
        imageView.setPreserveRatio(true);
        imageView.setFitWidth(100);
        imageView.setFitHeight(100);
        setAlignment(Pos.CENTER);
        empty();
    }

    public void setPlaceholder(Node placeholder) {
        this.placeholder = Optional.ofNullable(placeholder).orElse(this.placeholder);
        empty();
    }

    public void setImage(File imgFile) {
        this.getChildren().clear();
        this.getChildren().add(imageView);
        try {
            Image img = new Image(imgFile.toURI().toURL().toString(), false);
            imageView.setImage(img);
        } catch (IOException e) {
            throw new RuntimeException();
        }
    }

    public void empty() {
        getChildren().clear();
        getChildren().add(placeholder);
    }

    @Override
    public void resize(double width, double height) {
        super.resize(width, height);
        imageView.setFitWidth(width * 0.9);
        imageView.setFitHeight(height * 0.9);
    }
}
