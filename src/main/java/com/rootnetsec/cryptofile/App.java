package com.rootnetsec.cryptofile;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import javafx.scene.image.Image;

import java.io.IOException;

/**
 * Main JavaFX class.
 */
public class App extends Application {

    /** The main method.
     * @param args Call arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

    /**
     * @param stage Initializes and runs the JavaFX scene based on a fxml file.
     * @throws IOException Throws an exception when fails to read the fxml file
     */
    @Override
    public void start(Stage stage) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/layout.fxml"));

        Scene scene = new Scene(root);

        stage.setTitle("Files encryptor");
        stage.getIcons().add(new Image("/icon.png"));
        stage.setScene(scene);
        stage.show();


        stage.setOnCloseRequest(windowEvent -> {
            if (MainController.isCryptoThreadAlive()) {
                YesNoAlert alert = new YesNoAlert("Are you sure?",
                        "The program is still working. The results may be incomplete.\nProceed?",
                        Alert.AlertType.CONFIRMATION);
                alert.showAndWait();
                if (alert.answerWasYes()) {
                    System.exit(0);
                } else {
                    windowEvent.consume();
                }
            }
        });
    }
}