package com.rootnetsec.cryptofile;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import javafx.scene.image.Image;

import java.util.Optional;

public class App extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/layout.fxml"));

        Scene scene = new Scene(root);

        stage.setTitle("Files encryptor");
        stage.getIcons().add(new Image("/icon.png"));
        stage.setScene(scene);
        stage.show();


        stage.setOnCloseRequest(windowEvent -> {
            if (MainController.isCryptoThreadAlive()) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Are you sure?");
                alert.setHeaderText(null);
                alert.setContentText("The program is still working. The results may be incomplete.\nProceed?");
                ButtonType okButton = new ButtonType("Yes", ButtonBar.ButtonData.YES);
                ButtonType noButton = new ButtonType("No", ButtonBar.ButtonData.NO);
                alert.getButtonTypes().setAll(okButton, noButton);
                Optional<ButtonType> result = alert.showAndWait();
                if (result.get().getButtonData() == ButtonBar.ButtonData.YES) {
                    System.exit(0);
                } else {
                    windowEvent.consume();
                }
            }
        });
    }
}