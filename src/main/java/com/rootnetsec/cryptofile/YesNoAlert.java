package com.rootnetsec.cryptofile;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import java.util.Optional;

/**
 * Simple Yes/No JavaFX alert
 */
public class YesNoAlert {
    /**JavaFX alert dialog object*/
    private Alert alert;
    /**User's answer*/
    private Optional<ButtonType> result;

    /** Constructor that makes an alert dialog with specified title, message and type
     * @param title The title of the alert dialog
     * @param context The message showed in the alert dialog
     * @param type The type of the alert dialog
     */
    YesNoAlert(String title, String context, Alert.AlertType type) {
        alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(context);
        ButtonType okButton = new ButtonType("Yes", ButtonBar.ButtonData.YES);
        ButtonType noButton = new ButtonType("No", ButtonBar.ButtonData.NO);
        alert.getButtonTypes().setAll(okButton, noButton);
    }

    /**
     * Shows the alert window and save user's answer in object filed
     */
    public void showAndWait() {
        result = alert.showAndWait();
    }

    /** Tells if the user's selected yes button.
     * @return Returns true if user selected yes button.
     */
    public boolean answerWasYes() {
        return (result.get().getButtonData() == ButtonBar.ButtonData.YES);
    }
}
