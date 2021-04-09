package com.nocmok.pancakegui.scenes;

import java.io.File;
import java.io.IOException;

import com.nocmok.pancakegui.PancakeApp;
import com.nocmok.pancakegui.controls.ImageExplorer;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;

public class MainScene extends Scene {

    private static final Parent placeholder = new Label("failed to load layout");

    private ImageExplorer imgExplorer;

    public MainScene() {
        super(placeholder);
        
        Parent root = null;
        try {
            root = FXMLLoader.load(PancakeApp.getLayout("main_scene_layout.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        setRoot(root);

        imgExplorer = (ImageExplorer) lookup("#imageExplorer");
        imgExplorer.setPlaceholder(new Label("no image to display"));
        imgExplorer.setImage(new File("C:\\Users\\uporo\\source\\java\\pancake-desktop\\pleades1-crop-hpfm.png"));
    }

}