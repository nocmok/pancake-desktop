package com.nocmok.pancakegui;

import java.io.IOException;
import java.net.URL;

import com.nocmok.pancake.Pancake;
import com.nocmok.pancakegui.scenes.MainScene;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.stage.Stage;

public class PancakeApp extends Application {

    private static PancakeApp app;

    static {
        Pancake.load();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        PancakeApp.app = this;
        
        primaryStage.setMinHeight(600);
        primaryStage.setMinWidth(800);
        primaryStage.setTitle("Pancake");

        primaryStage.setScene(new MainScene());
        primaryStage.show();
    }

    public static void main(String[] args) {
        Application.launch(args);
    }

    public static PancakeApp app() {
        return app;
    }

    public static URL getLayout(String name) {
        return PancakeApp.class.getClassLoader().getResource("layouts/" + name);
    }

    public static Parent loadLayout(String name) {
        try {
            Parent layout = FXMLLoader.load(getLayout(name));
            return layout;
        } catch (IOException e) {
            throw new RuntimeException("failed to load layout " + name + ", due to i/o error", e);
        }
    }
}