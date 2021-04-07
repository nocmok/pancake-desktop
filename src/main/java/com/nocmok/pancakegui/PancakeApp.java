package com.nocmok.pancakegui;

import com.nocmok.pancake.Pancake;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class PancakeApp extends Application {

    static {
        Pancake.load();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
    }

    public static void main(String[] args){
        Application.launch(args);
    }
}