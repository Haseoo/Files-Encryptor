package com.rootnetsec.cryptofile;

import com.rootnetsec.cryptofile.cipher.Cipher;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import javafx.stage.Window;

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
    private Button startButton;
    @FXML
    private ProgressBar processProgress;

    private static Thread cryptoThread = null;

    private static Alert getAlert(Alert.AlertType type, String title, String text) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(text);
        return alert;
        
    }

    public static boolean isCryptoThreadAlive() {
        return (cryptoThread != null && cryptoThread.isAlive());
    }


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
            getAlert(Alert.AlertType.ERROR, "Fatal", "You haven't enter necessary information!").showAndWait();
            return;
        }
        if (workingMode == 0 && PasswordService.searchHaveIBeenPwnedDatabase(passwordText)) {
            YesNoAlert alert = new YesNoAlert("Password",
                    "Your password is weak!\nContinue?",
                    Alert.AlertType.WARNING);
            alert.showAndWait();
            if (!alert.answerWasYes()) {
                return;
            }
        }
        statusText.setText("Status: WORKING");
        working.setVisible(true);
        startButton.setDisable(true);

        Task task = new Task<Void>() {
            private String exceptionString = null;
            private boolean wasAnException = false;

            private void doWork(Cipher cipher, int workingMode, String inputFilePath, String outputFilePath, String passwordText) {
                try {
                    if (workingMode == 1) {
                        cipher.decryptFile(inputFilePath, outputFilePath, passwordText);
                    } else {
                        cipher.encryptFile(inputFilePath, outputFilePath, passwordText);
                    }
                } catch (Exception e) {
                    exceptionString = e.getMessage();
                    wasAnException = true;
                }
            }
            @Override
            protected Void call() throws Exception {
                try {
                    Cipher cipher;
                    if (workingMode == 1) {
                        cipher = Cipher.getInstance(inputFilePath);
                    } else {
                        cipher = Cipher.getInstance(Cipher.TYPE_PARSE_MAP.get(encryptionText));
                    }

                    Thread workThread = new Thread(() -> doWork(cipher, workingMode, inputFilePath, outputFilePath, passwordText));
                    workThread.setDaemon(true);
                    workThread.start();
                    while (!cipher.isWorkDone()) {
                        processProgress.setProgress(cipher.getCurrentChunk() / (double) cipher.getNumberOfChunks());
                        Thread.sleep(10);
                    }
                    workThread.join();

                    processProgress.setProgress(1);
                }catch (Throwable e) {
                    exceptionString = e.toString();
                   //TODO LOGGER e.printStackTrace();
                    throw new Exception(e.getMessage());
                }
                if (wasAnException) {
                    throw new Exception(exceptionString);
                }
                return null;
            }

            @Override
            protected void succeeded() {
                super.succeeded();
                getAlert(Alert.AlertType.INFORMATION, "Success", "Success!").showAndWait();

                statusText.setText("Status: OK");
                working.setVisible(false);
                startButton.setDisable(false);

            }

            @Override
            protected void failed() {
                getAlert(Alert.AlertType.ERROR, "Fatal", exceptionString).showAndWait();

                statusText.setText("Status: FAILED");
                working.setVisible(false);
                startButton.setDisable(false);

            }
        };
        cryptoThread = new Thread(task);
        cryptoThread.setDaemon(true);
        cryptoThread.start();
    }

}
