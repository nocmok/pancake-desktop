package com.nocmok.pancakegui.scenes;

import com.nocmok.pancakegui.controls.MainSceneController;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;

public class MainScene extends Scene {

    private static final Parent placeholder = new Label("failed to load layout");

    public MainScene() {
        super(placeholder);
        MainSceneController controller = MainSceneController.getNew();
        setRoot(controller.root());
    }

}