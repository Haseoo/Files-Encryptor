package com.rootnetsec.cryptofile;

import com.rootnetsec.cryptofile.cipher.Cipher;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import javafx.stage.Window;

import java.io.File;
import java.nio.file.Files;

/**
 * Main controller of the application window
 */
public class MainController {
    /**
     * The field that holds input file path
     */
    @FXML
    private TextField inputFile;
    /**
     * The field that holds output file path
     */
    @FXML
    private TextField outputFile;
    /**
     * The choicebox group that allows the user to choose an encryption algorithm
     */
    @FXML
    private ChoiceBox encryptionAlgorithm;
    /**
     * The main pane of the application window.
     */
    @FXML
    private BorderPane mainPane;
    @FXML
    /**
     * The checkbox group that allows the user to choose whether to encrypt or decrypt a file
     */
    private ToggleGroup mode;
    /**
     * The field that holds user's password
     */
    @FXML
    private PasswordField password;
    /**
     * The text label that displays information about the application current state
     */
    @FXML
    private Label statusText;
    /**
     * Indicates whether the application is encrypting or decrypting a file
     */
    @FXML
    private ProgressIndicator working;
    /**
     * The radio button that indicates encryption mode
     */
    @FXML
    private Toggle encRadio;
    /**
     * The button that starts an encryption or decryption process
     */
    @FXML
    private Button startButton;
    /**
     * The bar that displays the progress
     */
    @FXML
    private ProgressBar processProgress;

    /**
     * The thread object that creates a daemon thread for encrypting or decrypting
     */
    private static Thread cryptoThread = null;


    /** Check whether the encryption/decryption process is currently running
     * @return Returns true if the encryption/decryption process is currently running otherwise returns false
     */
    public static boolean isCryptoThreadAlive() {
        return (cryptoThread != null && cryptoThread.isAlive());
    }

    /** Creates a JavaFX alert dialog object
     * @param type The type of dialog alert
     * @param title The title of the alert dialog
     * @param text The text that will be displayed in the dialog
     * @return
     */
    private static Alert getAlert(Alert.AlertType type, String title, String text) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(text);
        return alert;

    }


    /**
     * Opens the file chooser dialog after clicking on the browse button
     */
    @FXML
    public void inputFileBrowse() {
        Window mainWindow = mainPane.getScene().getWindow();
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open source file");
        if (!inputFile.getText().equals("")) {
            File file = new File(inputFile.getText());
            if (file.exists()) {
                File dir = ((file.isAbsolute() && !file.isDirectory()) ? file.getParentFile() : file);
                fileChooser.setInitialDirectory(dir);
            }
        }
        File file = fileChooser.showOpenDialog(mainWindow);
        if (file != null) {
            inputFile.setText(file.getPath());
        }
    }

    /**
     * Opens the file chooser dialog after clicking on the browse button
     */
    @FXML
    public void outputFileBrowse() {
        Window mainWindow = mainPane.getScene().getWindow();
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open destination file");
        if (!outputFile.getText().equals("")) {
            File file = new File(outputFile.getText());
            if (file.exists()) {
                if  (file.isAbsolute() && !file.isDirectory()) {
                    fileChooser.setInitialFileName(file.getName());
                    fileChooser.setInitialDirectory(file.getParentFile());
                }
                else if (file.isDirectory()) {
                    fileChooser.setInitialDirectory(file);
                }
            }
        }
        File file = fileChooser.showSaveDialog(mainWindow);
        if (file != null) {
            outputFile.setText(file.getPath());
        }
    }

    /**
     * Unsets disable flag on the encryption algorithm choicebox on clicking the encryption mode radiobutton
     */
    @FXML
    public void onEncRadio() {
        encryptionAlgorithm.setDisable(false);
    }

    /**
     * Sets disable flag on the encryption algorithm choicebox on clicking the decryption mode radiobutton
     */
    @FXML
    public void onDecRadio() {
        encryptionAlgorithm.setDisable(true);
    }

    /**
     * Starts the encryption/decryption process on clicking the start button
     */
    @FXML
    public void onStart() {
        String inputFilePath  = inputFile.getText(),
               outputFilePath = outputFile.getText(),
               passwordText   = password.getText(),
               encryptionText  = ((encryptionAlgorithm.getValue() == null)? null : encryptionAlgorithm.getValue().toString());

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
                    e.printStackTrace();
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
