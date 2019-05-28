package com.rootnetsec.cryptofile.cipher;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import javafx.stage.Window;

import java.io.File;

public class MainController {
    @FXML
    public TextField inputFile;
    @FXML
    public TextField outputFile;
    @FXML
    public ChoiceBox encryptionType;
    @FXML
    public BorderPane mainPane;
    @FXML
    public ToggleGroup mode;
    @FXML
    public PasswordField password;
    @FXML
    public Label statusText;
    @FXML
    public ProgressIndicator working;

    @FXML
    public void inputFileBrowse() {
        Window mainWindow = mainPane.getScene().getWindow();
        FileChooser fc = new FileChooser();
        fc.setTitle("Open source file");
        File owo = fc.showOpenDialog(mainWindow);
        System.out.println(mainWindow);
        if (owo != null) {
            inputFile.setText(owo.getPath());
        }
    }

    @FXML
    public void outputFileBrowse() {
        Window mainWindow = mainPane.getScene().getWindow();
        FileChooser fc = new FileChooser();
        fc.setTitle("Open destination file");
        fc.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Encrypted Files", "*.enc"));
        File owo = fc.showSaveDialog(mainWindow);
        System.out.println(mainWindow);
        if (owo != null) {
            outputFile.setText(owo.getPath());
        }
    }

    @FXML
    public void onEncRadio() {
        encryptionType.setDisable(false);
    }

    @FXML
    public void onDecRadio() {
        encryptionType.setDisable(true);
    }

    @FXML
    public void onStart() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        String text = String.format("S:%s, D:%s, H%s, M:%s, T:%s", inputFile.getText(), outputFile.getText(),password.getText(), mode.getSelectedToggle(), encryptionType.getValue());
        alert.setTitle("Information Dialog");
        alert.setHeaderText(null);
        alert.setContentText(text);
        System.out.println(text);

        alert.showAndWait();
        statusText.setText("Status: working");
        working.setVisible(true);

    }



}
