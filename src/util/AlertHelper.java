package util;

import javafx.scene.control.ButtonType;

import java.util.Optional;
/**
 * This is a helper class that provides alert structures, lke Yes-No confirmation or Error alerts.
 * @author Batnomin Terbish
 * */
public class AlertHelper {
    /**
     * Method that creates Yes-No Confirmation-Alert.
     * @param title alert title to set
     * @param header alert header to set
     * @param content alert content to set
     * @return boolean true if Yes
     * */
    public static boolean yesNoConfirmationAlert (String title, String header, String content) {
        javafx.scene.control.Alert confirmationAlert = new
                javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.CONFIRMATION, content, ButtonType.YES, ButtonType.NO);
        confirmationAlert.setTitle(title);
        confirmationAlert.setHeaderText(header);
        Optional<ButtonType> response = confirmationAlert.showAndWait();
        return (response.isPresent()) && (response.get() == ButtonType.YES);
    }
    /**
     * Method that creates Information-Alert
     * @param title alert title to set
     * @param header alert header to set
     * @param content alert content to set
     */
    public static void infoAlert (String title, String header, String content) {
        javafx.scene.control.Alert infoAlert = new
                javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.INFORMATION, content);
        infoAlert.setTitle(title);
        infoAlert.setHeaderText(header);
        infoAlert.showAndWait();
    }
    /***
     * Method that creates Error-Alert
     * @param title alert title to set
     * @param header alert header to set
     * @param content alert content to set
     */
    public static void errorAlert (String title, String header, String content) {
        javafx.scene.control.Alert infoAlert = new
                javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.ERROR, content);
        infoAlert.setTitle(title);
        infoAlert.setHeaderText(header);
        infoAlert.showAndWait();
    }
}
