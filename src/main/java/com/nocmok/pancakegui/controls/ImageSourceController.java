package com.nocmok.pancakegui.controls;

import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.Map;
import java.util.ResourceBundle;

import com.nocmok.pancake.Spectrum;
import com.nocmok.pancakegui.PancakeApp;
import com.nocmok.pancakegui.pojo.ImageInfo;
import com.nocmok.pancakegui.pojo.SourceInfo;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;

public class ImageSourceController implements Initializable {

    public static ImageSourceController getNew() {
        FXMLLoader loader = new FXMLLoader(PancakeApp.getLayout("image_source_layout.fxml"));
        try {
            Parent root = loader.load();
            ImageSourceController controller = loader.getController();
            controller.root = root;
            return controller;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @FXML
    private ImageView imageOverview;

    @FXML
    private Label imageName;

    @FXML
    private Label imageResolution;

    @FXML
    private Label imagePhotometry;

    private Parent root;

    public ImageSourceController() {
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    private String mappingToString(Map<Integer, Spectrum> mapping) {
        StringBuilder photometry = new StringBuilder();
        Integer[] bands = mapping.keySet().toArray(new Integer[0]);
        Arrays.sort(bands);
        for (Integer band : bands) {
            photometry.append(band);
            photometry.append("[");
            photometry.append(mapping.get(band).getName().toUpperCase());
            photometry.append("]");
        }
        return photometry.toString();
    }

    public void setSourceInfo(SourceInfo info) {
        imageName.setText(info.path().getName());
        imagePhotometry.setText(mappingToString(info.mapping()));
    }

    public void setImageInfo(ImageInfo info) {
        imageResolution.setText(info.getXsize() + "x" + info.getYsize());
        imageOverview.setImage(info.getOverview());
    }

    public Parent root() {
        return root;
    }
}
