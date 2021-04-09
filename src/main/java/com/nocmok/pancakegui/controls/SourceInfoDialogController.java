package com.nocmok.pancakegui.controls;

import java.io.IOException;
import java.net.URL;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import com.nocmok.pancakegui.PancakeApp;
import com.nocmok.pancakegui.pojo.ImageInfo;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class SourceInfoDialogController implements Initializable {

    @FXML
    private ImageView imageOverview;

    @FXML
    private Label imageName;

    @FXML
    private Label imageResolution;

    @FXML
    private Label imageFormat;

    @FXML
    private VBox bandList;

    @FXML
    private Button addButton;

    private Map<Integer, String> mapping;

    private ImageInfo info;

    private Parent root;

    private BandMappingController[] mappingControllers;

    public static SourceInfoDialogController getNew() {
        FXMLLoader loader = new FXMLLoader(PancakeApp.getLayout("source_info_dialog_layout.fxml"));
        try {
            Parent root = loader.load();
            SourceInfoDialogController controller = loader.getController();
            controller.root = root;
            return controller;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public SourceInfoDialogController() {
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        addButton.addEventHandler(MouseEvent.MOUSE_CLICKED, this::onAddSourceClicked);
    }

    private Map<Integer, String> parseMapping() {
        if (mappingControllers == null) {
            return Collections.emptyMap();
        }
        Map<Integer, String> mapping = new HashMap<>();
        for (int i = 0; i < mappingControllers.length; ++i) {
            if (mappingControllers[i] == null) {
                continue;
            }
            if (!mappingControllers[i].isAnySelected()) {
                continue;
            }
            mapping.put(i, mappingControllers[i].getSelected());
        }
        return mapping;
    }

    private void onAddSourceClicked(MouseEvent event) {
        this.mapping = parseMapping();
        Node source = (Node) event.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
    }

    public void setInfo(ImageInfo info) {
        this.info = info;
        imageOverview.setImage(info.getOverview());
        imageName.setText(info.getPath().getName());
        imageResolution.setText(info.getXsize() + "x" + info.getYsize());
        imageFormat.setText(info.getImageFormat());

        mappingControllers = new BandMappingController[info.getnBands()];
        for (int i = 0; i < info.getnBands(); ++i) {
            mappingControllers[i] = BandMappingController.getNew();
            mappingControllers[i].setBandName("band#" + i);
            bandList.getChildren().add(mappingControllers[i].root());
        }
    }

    public ImageInfo getInfo() {
        return info;
    }

    public Map<Integer, String> mapping() {
        return mapping;
    }

    public Parent root() {
        return root;
    }
}
