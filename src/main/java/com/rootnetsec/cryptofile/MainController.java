package com.rootnetsec.cryptofile.cipher;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import javafx.stage.Window;
import com.rootnetsec.cryptofile.cipher.Cipher.EncryptionType;
import com.rootnetsec.cryptofile.cipher.Cipher;

import java.io.File;

public class MainController {
    @FXML
    private TextField inputFile;
    @FXML
    private TextField outputFile;
    @FXML
    private ChoiceBox encryptionType;
    @FXML
    private BorderPane mainPane;
    @FXML
    private ToggleGroup mode;
    @FXML
    private PasswordField password;
    @FXML
    private Label statusText;
    @FXML
    private ProgressIndicator working;
    @FXML
    private Toggle encRadio;

    @FXML
    public void inputFileBrowse() {
        Window mainWindow = mainPane.getScene().getWindow();
        FileChooser fc = new FileChooser();
        fc.setTitle("Open source file");
        File owo = fc.showOpenDialog(mainWindow);
        if (owo != null) {
            inputFile.setText(owo.getPath());
        }
    }

    @FXML
    public void outputFileBrowse() {
        Window mainWindow = mainPane.getScene().getWindow();
        FileChooser fc = new FileChooser();
        fc.setTitle("Open destination file");
        //fc.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Encrypted Files", "*.enc"));
        File owo = fc.showSaveDialog(mainWindow);
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
        String inputFilePath  = inputFile.getText(),
               outputFilePath = outputFile.getText(),
               passwordText   = password.getText(),
               encryptionText  = ((encryptionType.getValue() == null)? null : encryptionType.getValue().toString());

        int workingMode = ((mode.getSelectedToggle() == encRadio) ? 0 : 1);
        if (inputFilePath.isEmpty() || outputFilePath.isEmpty() || passwordText.isEmpty() || (workingMode == 0 && encryptionText == null)){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Fatal");
            alert.setHeaderText(null);
            alert.setContentText("You haven't enter necessary information!");
            alert.showAndWait();
            return;
        }
        statusText.setText("Status: working");
        working.setVisible(true);
        try {
            if (workingMode == 1) {
                Cipher.getInstance(inputFilePath).decryptFile(inputFilePath, outputFilePath, passwordText);
            } else {
                Cipher.getInstance(Cipher.TYPE_PARSE_MAP.get(encryptionText)).encryptFile(inputFilePath, outputFilePath, passwordText);
            }
        } catch(Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Fatal");
            alert.setHeaderText(null);
            alert.setContentText(e.getMessage());
            alert.showAndWait();
            statusText.setText("Status: ERROR");
            working.setVisible(false);
            return;
        }

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Success");
            alert.setHeaderText(null);
            alert.setContentText("Success!");
            alert.showAndWait();

        statusText.setText("Status: OK");
        working.setVisible(false);
    }





}
