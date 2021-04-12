package com.nocmok.pancakegui.controls;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.function.Function;

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

    private Function<String, Boolean> validator = (s) -> true;

    @Override
    public String getSelected() {
        if (textField.getStyleClass().size() > 1) {
            textField.getStyleClass().remove(textField.getStyleClass().size() - 1);
        }
        if (!isValid()) {
            textField.getStyleClass().add("pnk-input-warning");
            return null;
        }
        textField.getStyleClass().add("pnk-input");
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

    public InputOptionController setValidator(Function<String, Boolean> validator) {
        this.validator = Optional.ofNullable(validator).orElse((s) -> true);
        return this;
    }

    public boolean isValid() {
        return validator.apply(textField.getText());
    }
}
