package com.nocmok.pancakegui.controls;

import java.io.IOException;

import com.nocmok.pancakegui.PancakeApp;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

public abstract class ControllerBase {

    public static <T extends ControllerBase> T getNew(String layoutName) {
        FXMLLoader loader = new FXMLLoader(PancakeApp.getLayout(layoutName));
        try {
            Parent root = loader.load();
            T controller = loader.getController();
            controller.setRoot(root);
            return controller;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    protected abstract void setRoot(Parent root);

    public abstract Parent root();
}
