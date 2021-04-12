package com.nocmok.pancakegui.controls;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import com.nocmok.pancake.fusor.Brovey;
import com.nocmok.pancake.fusor.Fusor;
import com.nocmok.pancake.fusor.HPFM;
import com.nocmok.pancake.math.BoxHP;
import com.nocmok.pancake.math.Filter2D;
import com.nocmok.pancake.math.GaussianHP;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;

public class AlgorithmOptionController extends OptionControllerBase<Fusor> {

    public static AlgorithmOptionController getNew() {
        return ControllerBase.getNew("algorithm_option_layout.fxml");
    }

    public static final String BROVEY = "Brovey";

    public static final String HPFM = "HPFM";

    private static final List<String> fusors = List.of(BROVEY, HPFM);

    private static final String BOXCAR = "Boxcar";

    private static final String GAUSSIAN = "Gaussian";

    private static final List<String> filters = List.of(BOXCAR, GAUSSIAN);

    @FXML
    private DropDownOptionController<String> algoController;

    @FXML
    private DropDownOptionController<String> filterController;

    @FXML
    private InputOptionController rWeightController;

    @FXML
    private InputOptionController gWeightController;

    @FXML
    private InputOptionController bWeightController;

    @FXML
    private InputOptionController cutoffController;

    @FXML
    private Parent root;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        algoController.setOptionName("algorithm").setOptions(fusors).setDefault(BROVEY);
        algoController.setOnItemSelectedListener((s) -> {
            selectFusor(s);
        });
        rWeightController.setOptionName("R weight").setDefault("1");
        gWeightController.setOptionName("G weight").setDefault("1");
        bWeightController.setOptionName("B weight").setDefault("1");
        filterController.setOptionName("high pass filter").setOptions(filters).setDefault(GAUSSIAN);
        cutoffController.setOptionName("cutoff frequency");

        setDefault(BROVEY);
    }

    private void selectFusor(String name) {
        hideAll();
        switch (name) {
        case BROVEY:
            show(rWeightController.root());
            show(gWeightController.root());
            show(bWeightController.root());
            break;
        case HPFM:
            show(filterController.root());
            show(cutoffController.root());
            break;
        default:
            break;
        }
    }

    private void hide(Node node) {
        node.setVisible(false);
        node.setManaged(false);
    }

    private void show(Node node) {
        node.setVisible(true);
        node.setManaged(true);
    }

    private void hideAll() {
        hide(filterController.root());
        hide(rWeightController.root());
        hide(gWeightController.root());
        hide(bWeightController.root());
        hide(cutoffController.root());
    }

    private Fusor parseFusor() {
        switch (algoController.getSelected()) {
        case BROVEY:
            double rWeight = Double.parseDouble(rWeightController.getSelected());
            double gWeight = Double.parseDouble(gWeightController.getSelected());
            double bWeight = Double.parseDouble(bWeightController.getSelected());
            return new Brovey(rWeight, gWeight, bWeight);
        /** TODO */
        case HPFM:
            double cutoff = Double.parseDouble(cutoffController.getSelected());
            Filter2D filter = null;
            switch (filterController.getSelected()) {
            case BOXCAR:
                filter = new BoxHP(9);
                break;
            case GAUSSIAN:
                filter = new GaussianHP(3f);
                break;
            }
            if (filter == null) {
                return null;
            }
            return new HPFM(filter);
        default:
            return null;
        }
    }

    public AlgorithmOptionController setDefault(String name) {
        algoController.setDefault(name);
        selectFusor(name);
        return this;
    }

    @Override
    public Fusor getSelected() {
        return parseFusor();
    }

    @Override
    protected void setRoot(Parent root) {
        this.root = root;
    }

    @Override
    public Parent root() {
        return root;
    }

}