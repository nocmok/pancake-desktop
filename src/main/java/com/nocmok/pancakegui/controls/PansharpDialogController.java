package com.nocmok.pancakegui.controls;

import java.net.URL;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.function.Function;

import com.nocmok.pancake.Bilinear;
import com.nocmok.pancake.Compression;
import com.nocmok.pancake.Cubic;
import com.nocmok.pancake.Formats;
import com.nocmok.pancake.Nearest;
import com.nocmok.pancake.Pancake;
import com.nocmok.pancake.PansharpJobBuilder;
import com.nocmok.pancake.Resampler;
import com.nocmok.pancake.fusor.Fusor;
import com.nocmok.pancakegui.PancakeApp;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class PansharpDialogController extends ControllerBase {

    private static final int FUSOR = 0;

    private static final int OUTPUT_FORMAT = 1;

    private static final int OUTPUT_DATATYPE = 2;

    private static final int RESAMPLER = 3;

    private static final int COMPRESSION = 4;

    private static final int COMPRESSION_QUALITY = 5;

    private static final int HIST_MATCHING = 6;

    private static final String uint8 = "uint8";

    private static final String int16 = "int16";

    private static final String uint16 = "uint16";

    private static final Map<String, Integer> dataTypes = new HashMap<>();

    private static final String NEAREST = "nearest";

    private static final String CUBIC = "cubic";

    private static final String BILINEAR = "bilinear";

    private static final Map<String, Resampler> resamplers = new HashMap<>();

    static {
        dataTypes.put(uint8, Pancake.TYPE_BYTE);
        dataTypes.put(int16, Pancake.TYPE_INT_16);
        dataTypes.put(uint16, Pancake.TYPE_UINT_16);

        resamplers.put(NEAREST, new Nearest());
        resamplers.put(CUBIC, new Cubic());
        resamplers.put(BILINEAR, new Bilinear());
    }

    public static PansharpDialogController getNew() {
        return ControllerBase.getNew("pansharp_dialog_layout.fxml");
    }

    @FXML
    private VBox optionsVBox;

    @FXML
    private Button pansharpButton;

    private Parent root;

    private Map<Integer, OptionControllerBase<?>> controls;

    private PansharpJobBuilder jobBuilder;

    public PansharpDialogController() {
        controls = new LinkedHashMap<>();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        controls.put(FUSOR, AlgorithmOptionController.getNew());
        controls.put(OUTPUT_DATATYPE, DropDownOptionController.<String>getNew().setOptionName("output datatype")
                .setOptions(dataTypes.keySet()).setDefault(uint8));
        controls.put(OUTPUT_FORMAT, DropDownOptionController.<Formats>getNew().setOptionName("output format")
                .setOptions(List.of(Formats.values())).setDefault(Formats.GTiff));
        controls.put(RESAMPLER, DropDownOptionController.<String>getNew().setOptionName("resampler")
                .setOptions(resamplers.keySet()).setDefault(NEAREST));
        controls.put(COMPRESSION, DropDownOptionController.<Compression>getNew().setOptionName("compression")
                .setOptions(List.of(Compression.values())).setDefault(Compression.NONE));
        controls.put(COMPRESSION_QUALITY, InputOptionController.getNew().setOptionName("compression quality")
                .setDefault("50").setValidator(new Function<String, Boolean>() {
                    @Override
                    public Boolean apply(String s) {
                        if (s == null) {
                            return false;
                        }
                        try {
                            int quality = Integer.parseInt(s);
                            return (quality >= 0) && (quality <= 100);
                        } catch (NumberFormatException e) {
                            return false;
                        }
                    }
                }));
        controls.put(HIST_MATCHING,
                CheckBoxOptionController.getNew().setOptionName("apply hist matching").setDefault(true));
        for (OptionControllerBase<?> control : controls.values()) {
            optionsVBox.getChildren().add(control.root());
        }
        pansharpButton.addEventHandler(MouseEvent.MOUSE_CLICKED, this::onPansharpButtonClicked);
    }

    public PansharpJobBuilder runDialog() {
        Stage dialog = new Stage();
        dialog.setScene(new Scene(root));
        dialog.setResizable(false);
        dialog.initStyle(StageStyle.UTILITY);
        dialog.initOwner(PancakeApp.app().primaryStage());
        dialog.initModality(Modality.WINDOW_MODAL);
        dialog.showAndWait();
        return jobBuilder;
    }

    private void onPansharpButtonClicked(MouseEvent event) {
        if (!validate()) {
            return;
        }
        jobBuilder = parseBuilder();
        Node source = (Node) event.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
    }

    private boolean validate() {
        boolean result = true;
        for (OptionControllerBase<?> controller : controls.values()) {
            if (controller.getSelected() == null) {
                result = false;
            }
        }
        return result;
    }

    private PansharpJobBuilder parseBuilder() {
        PansharpJobBuilder jobBuilder = new PansharpJobBuilder();
        jobBuilder.withFusor((Fusor) (controls.get(FUSOR)).getSelected());
        jobBuilder.withCompression((Compression) controls.get(COMPRESSION).getSelected());
        jobBuilder.withOutputFormat((Formats) controls.get(OUTPUT_FORMAT).getSelected());
        jobBuilder.withOutputDatatype(dataTypes.get(((String) controls.get(OUTPUT_DATATYPE).getSelected())));
        jobBuilder.withResampler(resamplers.get((String) controls.get(RESAMPLER).getSelected()));
        jobBuilder.withCompressionQuality(Integer.parseInt((String) controls.get(COMPRESSION_QUALITY).getSelected()));
        jobBuilder.useHistMatching((Boolean) controls.get(HIST_MATCHING).getSelected());
        return jobBuilder;
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
