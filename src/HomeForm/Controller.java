package HomeForm;

import BruteForce.BruteForce;
import TuringMachines.MultiTapesTuringMachine;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Service;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.chart.*;
import javafx.scene.control.*;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBoxBuilder;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Observable;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Controller {

    private MultiTapesTuringMachine twoTapesTuringMachine;
    private StringBuilder logs;

    @FXML
    TextArea codeTuringMachine;
    @FXML
    TextField firstLine;
    @FXML
    Label inputData, firstTape, twoTape, firstTapeCurrentPosition, twoTapeCurrentPosition, workInfo;
    @FXML
    Slider speed;
    @FXML
    Button saveLogs, start, stop;
    @FXML
    BarChart graphic;
    @FXML
    CategoryAxis bottom;
    @FXML
    NumberAxis left;

    public void initialize() {
        twoTapesTuringMachine = new MultiTapesTuringMachine(2, "01#λ+-");
        workInfo.setText("Current rule: " + twoTapesTuringMachine.nextRule() + "\t\tSteps performed: " + 0);
        saveLogs.setDisable(true);
        try {
            BufferedReader reader = new BufferedReader(new FileReader("instructions.tmi"));
            String line;
            List<String> lines = new ArrayList<String>();
            try {
                while ((line = reader.readLine()) != null) {
                    lines.add(line);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            String [] linesAsArray = lines.toArray(new String[lines.size()]);
            for (String i : linesAsArray) codeTuringMachine.setText(codeTuringMachine.getText() + i + '\n');
        } catch (FileNotFoundException ignored) { }

    }

    private void displayErrorWindow(String message) {
        final Stage dialogStage = new Stage();
        dialogStage.setWidth(200);
        dialogStage.setHeight(130);
        dialogStage.initModality(Modality.WINDOW_MODAL);
        Button ok = new Button();
        Text text = new Text(message);
        ok.setText("OK");
        ok.setPrefWidth(70);
        ok.setOnAction(event -> dialogStage.close());
        Scene scene = new Scene(VBoxBuilder.create().
                children(text, new Text(""), ok).
                alignment(Pos.CENTER).padding(new Insets(5)).build());
        dialogStage.setScene(scene);
        dialogStage.show();
    }

    private void setStatusMTElements(boolean fl) {
        saveLogs.setDisable(fl);
        start.setDisable(fl);
        speed.setDisable(fl);
        codeTuringMachine.setDisable(fl);
        stop.setDisable(!fl);
    }

    public void toStart() {
        twoTapesTuringMachine.clearRules();
        twoTapesTuringMachine.setDefaultCurrentRule();
        try {
            twoTapesTuringMachine.addAllRule(codeTuringMachine.getText().split(" *\n+ *"));
        } catch (IllegalArgumentException e) {
            setStatusMTElements(false);
            displayErrorWindow("Incorrect input rule!");
            return;
        }
        if(!firstLine.getText().matches("[01]*")) {
            setStatusMTElements(false);
            displayErrorWindow("Incorrect input tape!");
            return;
        }
        String[] tapes = new String[1];
        tapes[0] = firstLine.getText();
        try {
            twoTapesTuringMachine.setTapes(tapes);
        } catch (Exception e) {
            setStatusMTElements(false);
            displayErrorWindow("Incorrect input tape!");
            return;
        }
        logs = new StringBuilder();
        setStatusMTElements(true);
        startScheduledExecutorService();

    }

    private void startScheduledExecutorService(){
        final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        scheduler.scheduleAtFixedRate(
                new Runnable() {
                    int i = 1;

                    @Override
                    public void run() {
                        if (twoTapesTuringMachine.run()) {
                            Platform.runLater(() -> {
                                stop.setOnAction(event -> {
                                    scheduler.shutdown();
                                    setStatusMTElements(false);
                                });
                                logs.append(twoTapesTuringMachine);
                                workInfo.setText("Current rule: " + twoTapesTuringMachine.nextRule() + "\t\tSteps performed: " + i);
                                firstTape.setText(twoTapesTuringMachine.getTapes()[0]);
                                firstTapeCurrentPosition.setText(twoTapesTuringMachine.getCurrentPosition()[0]);
                                twoTape.setText(twoTapesTuringMachine.getTapes()[1]);
                                twoTapeCurrentPosition.setText(twoTapesTuringMachine.getCurrentPosition()[1]);
                                i++;
                            });
                        } else {
                            scheduler.shutdown();
                            Platform.runLater(() -> setStatusMTElements(false));
                        }
                    }
                },
                100, 2501 - speed.valueProperty().intValue(), TimeUnit.MILLISECONDS);
    }


    public void toSaveLogs() {
        FileChooser fc = new FileChooser();
        fc.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Logs file", "*.log"),
                new FileChooser.ExtensionFilter("Text file", "*.txt"),
                new FileChooser.ExtensionFilter("Turing machine file", ".tmf")
        );
        try {
            String address = fc.showSaveDialog(Main.pS).toString();
            File file = new File(address);
            try {
                boolean fileOpen = file.exists() || file.createNewFile();
                if (!fileOpen) {
                    displayErrorWindow("Logs file don't available!");
                }
                else {
                    try (PrintWriter out = new PrintWriter(file.getAbsoluteFile())) {
                        out.print(logs);
                    }
                }
            } catch(IOException e) {
                throw new RuntimeException(e);
            }
        }  catch (Exception ignored) { }
    }


    @FXML public void check() { }

    public void onDisplayGraphic() {

        graphic.getData().clear();
        left.setLabel("Value");
        bottom.setLabel("Length of word");
        twoTapesTuringMachine.clearRules();
        try {
            twoTapesTuringMachine.addAllRule(codeTuringMachine.getText().split(" *\n+ *"));
        } catch (IllegalArgumentException e) {
            displayErrorWindow("Incorrect input rule!");
            return;
        }
        int[] test = new int[16];

        ArrayList<String> bottomCategories = new ArrayList<String>();

        final XYChart.Series<String, Number> seriesExperimental = new XYChart.Series<String, Number>();
        seriesExperimental.setName("Experimental");
        final XYChart.Series<String, Number> seriesAnalytic = new XYChart.Series<String, Number>();
        seriesAnalytic.setName("Analytic");

        final Timeline tl = new Timeline();
        tl.getKeyFrames().add(
                new KeyFrame(Duration.millis(100),
                        actionEvent -> {
                            left.setAnimated(true);
                            for (XYChart.Data<String, Number> series : seriesExperimental.getData()) {
                                for (XYChart.Data<String, Number> data : seriesAnalytic.getData()) {
                                    data.setXValue("" + (Math.random() * 10));
                                }
                            }
                        }
                ));
        tl.setCycleCount(Animation.INDEFINITE);
        tl.setAutoReverse(true);
        tl.play();

        for (int i = 0; i < test.length; i++) {
            test[i] = 0;
            bottomCategories.add(i+"");
            int count;
            String[] insetWords = BruteForce.go(i, "10");
            for (int j = 0; j < insetWords.length; j++) {
                twoTapesTuringMachine.setDefaultCurrentRule();
                String[] tapes = new String[1];
                tapes[0] = insetWords[j];
                twoTapesTuringMachine.setTapes(tapes);
                count = 0;
                while (twoTapesTuringMachine.run()) count++;
                if (count > test[i]) test[i] = count;

            }

            seriesExperimental.getData().add(new XYChart.Data<>(i + "", test[i]));
            seriesAnalytic.getData().add(new XYChart.Data<>(i + "",
                    (i == 0) ? 2 : (i % 3 == 0) ? (8 * i + 18) / 3 : (i % 3 == 1) ? (5 * i + 13) / 3 : (i % 3 == 2) ? (5 * i + 11) / 3 : 0
            ));

        }

        bottom.setCategories(FXCollections.<String>observableArrayList(bottomCategories));

        graphic.getData().addAll(seriesExperimental, seriesAnalytic);

    }

    public void onStop() {}

}
