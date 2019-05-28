package com.rootnetsec.cryptofile;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.image.Image;

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
    }
}