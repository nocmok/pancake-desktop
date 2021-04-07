package com.nocmok.pancakegui.scenes;

import com.nocmok.pancakegui.PancakeApp;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;

public class MainScene extends Scene {

    private static final Parent placeholder = new Label("failed to load layout");

    public MainScene() {
        super(placeholder);
        Parent root = PancakeApp.loadLayout("main_scene_layout.fxml");
        setRoot(root);
    }
    

}
