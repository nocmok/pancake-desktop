package com.nocmok.pancakegui.controls;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import com.nocmok.pancake.Spectrum;
import com.nocmok.pancakegui.PancakeApp;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;

public class BandMappingController implements Initializable {

    public static BandMappingController getNew() {
        FXMLLoader loader = new FXMLLoader(PancakeApp.getLayout("band_mapping_layout.fxml"));
        try {
            Parent root = loader.load();
            BandMappingController controller = loader.getController();
            controller.root = root;
            return controller;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
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
}
