package HomeForm;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.IOException;
import java.lang.reflect.Executable;

public class Main extends Application {

    static public Stage pS;

    @Override
    public void start(Stage primaryStage) {
        try {
            pS = primaryStage;
            pS.setOnCloseRequest(t -> {
                Platform.exit();
                System.exit(0);
            });
            Parent root = FXMLLoader.load(getClass().getResource("HomeForm.fxml"));
            primaryStage.setTitle("Coursework - Discrete Structures - Multitape Turing Machine - O. Kryvenko ");
            primaryStage.setScene(new Scene(root));
            primaryStage.setResizable(false);
            primaryStage.show();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }

}
