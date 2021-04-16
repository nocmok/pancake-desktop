package com.nocmok.pancakegui.controls;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.Function;

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
    private InputOptionController niWeightController;

    @FXML
    private InputOptionController cutoffController;

    @FXML
    private Parent root;

    private List<OptionControllerBase<?>> controls;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        controls = new ArrayList<>();
        controls.add(algoController);
        controls.add(filterController);
        controls.add(rWeightController);
        controls.add(gWeightController);
        controls.add(bWeightController);
        controls.add(niWeightController);
        controls.add(cutoffController);

        algoController.setOptionName("algorithm").setOptions(fusors).setDefault(BROVEY);
        algoController.setOnItemSelectedListener((s) -> {
            selectFusor(s);
        });
        Function<String, Boolean> doubleValidator = new Function<String, Boolean>() {
            @Override
            public Boolean apply(String s) {
                if (s == null) {
                    return false;
                }
                try {
                    double value = Double.parseDouble(s);
                    return (value >= 0d) && (value <= 1d);
                } catch (NumberFormatException e) {
                    return false;
                }
            }
        };
        Function<String, Boolean> integerValidator = new Function<String, Boolean>() {
            @Override
            public Boolean apply(String s) {
                if (s == null) {
                    return false;
                }
                try {
                    int value = Integer.parseInt(s);
                    return (value % 2 == 1) && (value >= 3) && (value <= 100);
                } catch (NumberFormatException e) {
                    return false;
                }
            }
        };
        rWeightController.setOptionName("R weight").setDefault("1").setValidator(doubleValidator);
        gWeightController.setOptionName("G weight").setDefault("1").setValidator(doubleValidator);
        bWeightController.setOptionName("B weight").setDefault("1").setValidator(doubleValidator);
        niWeightController.setOptionName("NI weight").setDefault("0").setValidator(doubleValidator);
        filterController.setOptionName("high pass filter").setOptions(filters).setDefault(GAUSSIAN);
        cutoffController.setOptionName("kernel size").setDefault("3").setValidator(integerValidator);

        setDefault(BROVEY);
    }

    private void selectFusor(String name) {
        hideAll();
        switch (name) {
        case BROVEY:
            show(rWeightController.root());
            show(gWeightController.root());
            show(bWeightController.root());
            show(niWeightController.root());
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
        hide(niWeightController.root());
        hide(cutoffController.root());
    }

    private Fusor parseFusor() {
        switch (algoController.getSelected()) {
        case BROVEY:
            double rWeight = Double.parseDouble(rWeightController.getSelected());
            double gWeight = Double.parseDouble(gWeightController.getSelected());
            double bWeight = Double.parseDouble(bWeightController.getSelected());
            double niWeight = Double.parseDouble(niWeightController.getSelected());
            return new Brovey(rWeight, gWeight, bWeight, niWeight);
        case HPFM:
            int size = Integer.parseInt(cutoffController.getSelected());
            Filter2D filter = null;
            switch (filterController.getSelected()) {
            case BOXCAR:
                filter = BoxHP.ofSize(size);
                break;
            case GAUSSIAN:
                filter = GaussianHP.ofSize(size);
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

    private boolean validate() {
        boolean result = true;
        for (OptionControllerBase<?> control : controls) {
            if (control.getSelected() == null) {
                result = false;
            }
        }
        return result;
    }

    public AlgorithmOptionController setDefault(String name) {
        algoController.setDefault(name);
        selectFusor(name);
        return this;
    }

    @Override
    public Fusor getSelected() {
        if (!validate()) {
            return null;
        }
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
