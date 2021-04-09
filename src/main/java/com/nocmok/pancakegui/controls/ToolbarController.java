package com.nocmok.pancakegui.controls;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;

public class ToolbarController implements Initializable {

    @FXML
    private Button playButton;

    @FXML
    private Button zoominButton;

    @FXML
    private Button zoomoutButton;

    private Runnable onPlayButtonClickedHandler;

    private Runnable onZoomInButtonClicked;

    private Runnable onZoomOutButtonClicked;

    public ToolbarController() {
        onPlayButtonClickedHandler = () -> {
        };
        onZoomInButtonClicked = () -> {
        };
        onZoomOutButtonClicked = () -> {
        };
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void setOnPlayButtonClickedHandler(Runnable handler) {
        this.onPlayButtonClickedHandler = Optional.ofNullable(handler).orElse(() -> {
        });
    }

    public void setOnZoomInButtonClickedHandler(Runnable handler) {
        this.onZoomInButtonClicked = Optional.ofNullable(handler).orElse(() -> {
        });
    }

    public void setOnZoomOutButtonClickedHandler(Runnable handler) {
        this.onZoomOutButtonClicked = Optional.ofNullable(handler).orElse(() -> {
        });
    }
}
