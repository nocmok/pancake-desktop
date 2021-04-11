package com.nocmok.pancakegui.controls;

import java.net.URL;
import java.util.Collection;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;

public class DropDownOptionController<T> extends ControllerBase implements Initializable {

    public static <T> DropDownOptionController<T> getNew() {
        return ControllerBase.getNew("drop_down_option_layout.fxml");
    }

    @FXML
    private Label optionName;

    @FXML
    private ComboBox<T> dropdown;

    private Parent root;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

    public void setOptionName(String name) {
        optionName.setText(name);
    }

    public void setOptions(Collection<? extends T> options) {
        dropdown.setItems(FXCollections.observableArrayList(options));
    }

    public void setDefault(T value) {
        dropdown.getSelectionModel().select(value);
    }

    public boolean isAnySelected() {
        return dropdown.getValue() != null;
    }

    public T getSelected() {
        return dropdown.getValue();
    }

    public Parent root() {
        return root;
    }

    @Override
    protected void setRoot(Parent root) {
        this.root = root;
    }
}
