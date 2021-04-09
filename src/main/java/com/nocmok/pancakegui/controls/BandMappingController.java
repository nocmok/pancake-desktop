package com.nocmok.pancakegui.controls;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import com.nocmok.pancake.Spectrum;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;

public class BandMappingController extends ControllerBase implements Initializable {

    public static BandMappingController getNew() {
        return ControllerBase.getNew("band_mapping_layout.fxml");
    }

    @FXML
    private Label bandName;

    @FXML
    private ComboBox<String> spectrumDropdown;

    private Parent root;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        List<String> values = new ArrayList<>();
        for (Spectrum spect : Spectrum.all()) {
            values.add(spect.name());
        }
        spectrumDropdown.setItems(FXCollections.observableArrayList(values));
        spectrumDropdown.getSelectionModel().select(Spectrum.NONE.name());
    }

    public void setBandName(String name) {
        bandName.setText(name);
    }

    public boolean isAnySelected() {
        return spectrumDropdown.getValue() != null
                && !spectrumDropdown.getValue().equalsIgnoreCase(Spectrum.NONE.name());
    }

    public String getSelected() {
        return spectrumDropdown.getValue();
    }

    public Parent root() {
        return root;
    }

    @Override
    protected void setRoot(Parent root) {
        this.root = root;
    }
}
