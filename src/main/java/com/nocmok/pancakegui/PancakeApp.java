package com.nocmok.pancakegui;

import java.net.URL;

import com.nocmok.pancake.Pancake;
import com.nocmok.pancakegui.scenes.MainScene;

import javafx.application.Application;
import javafx.stage.Stage;

public class PancakeApp extends Application {

    private static PancakeApp app;

    static {
        Pancake.load();
    }

    private Stage primaryStage;

    @Override
    public void start(Stage primaryStage) throws Exception {
        PancakeApp.app = this;
        this.primaryStage = primaryStage;

        primaryStage.setMinHeight(600);
        primaryStage.setMinWidth(800);
        primaryStage.setTitle("Pancake");

        primaryStage.setScene(new MainScene());
        primaryStage.toFront();
        primaryStage.show();
    }

    public Stage primaryStage() {
        return primaryStage;
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
}