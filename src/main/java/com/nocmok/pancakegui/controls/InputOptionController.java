package com.nocmok.pancakegui.controls;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class InputOptionController extends OptionControllerBase<String> {

    public static InputOptionController getNew() {
        return ControllerBase.getNew("input_option_layout.fxml");
    }

    @FXML
    private Label optionName;

    @FXML
    private TextField textField;

    @FXML
    private Parent root;

    @Override
    public String getSelected() {
        return textField.getText();
    }

    @Override
    protected void setRoot(Parent root) {
        this.root = root;
    }

    @Override
    public Parent root() {
        return root;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public InputOptionController setOptionName(String name) {
        optionName.setText(name);
        return this;
    }

    public InputOptionController setDefault(String value) {
        textField.setText(value);
        return this;
    }
}
