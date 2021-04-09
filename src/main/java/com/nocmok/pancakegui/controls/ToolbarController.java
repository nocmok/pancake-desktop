package com.nocmok.pancakegui.controls;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;

public class ToolbarController extends ControllerBase implements Initializable {

    public static ToolbarController getNew() {
        return ControllerBase.getNew("toolbar_layout.fxml");
    }

    @FXML
    private Button playButton;

    @FXML
    private Button zoominButton;

    @FXML
    private Button zoomoutButton;

    private Runnable onPlayButtonClickedHandler;

    private Runnable onZoomInButtonClicked;

    private Runnable onZoomOutButtonClicked;

    private Parent root;

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
        playButton.addEventHandler(MouseEvent.MOUSE_CLICKED, this::onPlayButtonClicked);
        zoominButton.addEventHandler(MouseEvent.MOUSE_CLICKED, this::onZoomInButtonClicked);
        zoomoutButton.addEventHandler(MouseEvent.MOUSE_CLICKED, this::onZoomOutButtonClicked);
    }

    private void onPlayButtonClicked(MouseEvent event) {
        onPlayButtonClickedHandler.run();
    }

    private void onZoomInButtonClicked(MouseEvent event) {
        onZoomInButtonClicked.run();
    }

    private void onZoomOutButtonClicked(MouseEvent event) {
        onZoomOutButtonClicked.run();
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

    @Override
    protected void setRoot(Parent root) {
        this.root = root;
    }

    @Override
    public Parent root() {
        return root;
    }
}
