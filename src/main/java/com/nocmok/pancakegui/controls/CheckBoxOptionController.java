package com.nocmok.pancakegui.controls;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;

public class CheckBoxOptionController extends OptionControllerBase<Boolean> {

    public static CheckBoxOptionController getNew() {
        return ControllerBase.getNew("checkbox_option_layout.fxml");
    }

    @FXML
    private Label optionName;

    @FXML
    private CheckBox checkBox;

    @FXML
    private Parent root;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

    @Override
    public Boolean getSelected() {
        return checkBox.isSelected();
    }

    @Override
    protected void setRoot(Parent root) {
        this.root = root;
    }

    @Override
    public Parent root() {
        return root;
    }

    public CheckBoxOptionController setOptionName(String name) {
        optionName.setText(name);
        return this;
    }

    public CheckBoxOptionController setDefault(Boolean value) {
        checkBox.setSelected(value);
        return this;
    }
}
