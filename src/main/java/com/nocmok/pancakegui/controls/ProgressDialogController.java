package com.nocmok.pancakegui.controls;

import java.net.URL;
import java.util.ResourceBundle;

import com.nocmok.pancakegui.PancakeApp;

import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class ProgressDialogController extends ControllerBase {

    public static ProgressDialogController getNew() {
        return ControllerBase.getNew("progress_dialog_layout.fxml");
    }

    @FXML
    private ProgressBar progressBar;

    @FXML
    private Label logs;

    @FXML
    private Button cancelButton;

    @FXML
    private Parent root;

    private Stage dialog;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cancelButton.addEventHandler(MouseEvent.MOUSE_CLICKED, (e) -> closeDialog());
    }

    @Override
    protected void setRoot(Parent root) {
        // TODO Auto-generated method stub

    }

    @Override
    public Parent root() {
        return root;
    }

    public void runDialog() {
        dialog = new Stage();
        dialog.setScene(new Scene(root));
        dialog.setResizable(false);
        dialog.initStyle(StageStyle.UTILITY);
        dialog.initOwner(PancakeApp.app().primaryStage());
        dialog.initModality(Modality.WINDOW_MODAL);
        dialog.show();
    }

    public void closeDialog() {
        if (dialog != null) {
            dialog.close();
        }
    }

    private double clamp(double value) {
        return Double.max(0f, Double.min(1f, value));
    }

    public void setProgress(double progress) {
        progressBar.setProgress(clamp(progress));
    }

    public void setMessage(String message) {
        logs.setText(message);
    }
}
