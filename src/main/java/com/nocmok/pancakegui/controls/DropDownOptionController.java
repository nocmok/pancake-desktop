package com.nocmok.pancakegui.controls;

import java.net.URL;
import java.util.Collection;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.function.Consumer;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;

public class DropDownOptionController<T> extends OptionControllerBase<T> {

    public static <T> DropDownOptionController<T> getNew() {
        return ControllerBase.getNew("drop_down_option_layout.fxml");
    }

    @FXML
    private Label optionName;

    @FXML
    private ComboBox<T> dropdown;

    @FXML
    private Parent root;

    private Consumer<T> onItemSelectedListener = (t) -> {
    };

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        dropdown.getSelectionModel().selectedItemProperty().addListener((a, b, c) -> {
            invokeListener();
        });
    }

    private void invokeListener() {
        onItemSelectedListener.accept(dropdown.getValue());
    }

    public void setOnItemSelectedListener(Consumer<T> listener) {
        this.onItemSelectedListener = Optional.ofNullable(listener).orElse((t) -> {
        });
    }

    public DropDownOptionController<T> setOptionName(String name) {
        optionName.setText(name);
        return this;
    }

    public DropDownOptionController<T> setOptions(Collection<? extends T> options) {
        dropdown.setItems(FXCollections.observableArrayList(options));
        return this;
    }

    public DropDownOptionController<T> setDefault(T value) {
        dropdown.getSelectionModel().select(value);
        return this;
    }

    public boolean isAnySelected() {
        return dropdown.getValue() != null;
    }

    @Override
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
