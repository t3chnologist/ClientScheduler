package controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import util.InputValidation;
import util.SceneHelper;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.stream.Stream;
import static util.InputValidation.setLabelStyleForError;
import static util.InputValidation.setTextFieldStyleForError;
/**
 * This class is the controller for the "Login" screen (/view/View_Login.fxml).
 * Class also provides basic client-side input validations using focus listeners and onAction triggers.
 * @author Batnomin Terbish
 * */
public class Controller_Login implements Initializable {
    @FXML private Label loginLabelTop, errorMessageLabelTop, zoneIdLabel;

    @FXML private TextField userNameField;
    @FXML private PasswordField passwordField;
    @FXML private Button loginButtonLabel;
    @FXML private final ResourceBundle rb = ResourceBundle.getBundle("util/Login", Locale.getDefault());

    /**
     * Method call to initialize a controller after its root elements have been processed completely.
     * @param url The location for resolving relative paths for the root object
     * @param resourceBundle The resource localizing the root object
     * */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loginLabelTop.setText(rb.getString("loginLabelTop"));
        userNameField.setPromptText(rb.getString("username"));
        passwordField.setPromptText(rb.getString("password"));
        loginButtonLabel.setText(rb.getString("login"));
        zoneIdLabel.setText(String.valueOf(ZoneId.systemDefault()));
        loginFieldFocusListener(userNameField);
        loginFieldFocusListener(passwordField);
    }
    /**
     * Method that checks username and password, and, calls the SceneHelper.showMainView() method if inputs are valid.
     * Triggered by ActionEvent on Login button, Username and Password textFields.
     * @throws Exception exception
     * */
    public void onLoginAttempt() throws Exception {
        //check for blank field(s)
        boolean noEmptyField = Stream.of(userNameField.getText(), passwordField.getText()).noneMatch(String::isBlank);

        if (noEmptyField) {
            //validate username & password
            boolean valid = InputValidation.loginCheck(userNameField, passwordField);
            if (valid) {
                SceneHelper.showMainView();
            }
            else {
                errorMessageLabelTop.setText(rb.getString("invalidCredential"));
                setLabelStyleForError(errorMessageLabelTop);
            }
        }
        else {
            errorMessageLabelTop.setText(rb.getString("blankFieldError"));
            setLabelStyleForError(errorMessageLabelTop);
        }
    }

    /**
     * Method that records login attempts.
     * @param username to record
     * @param successful login status to record
     * */
    public static void recordLoginAttempt(String username, Boolean successful) {
        try {
            File logFile = new File("login_activity.txt");

            if (logFile.createNewFile()) {
                System.out.println("File created: " + logFile.getName() + ".");
            }

            try (FileWriter fileWriter = new FileWriter(logFile, true)) {
                fileWriter.write(String.format("%s: %s login for %s",
                        LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH))
                                + " " + ZoneId.systemDefault().getDisplayName(TextStyle.SHORT, Locale.getDefault()),
                        successful ? "Successful" : "Failed", username + "\"\n"));
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Method that sets Focus Listeners for textFields
     * @param textField to set focused listener
     * @lambda using lambda for more compact and readable code
     * */
    void loginFieldFocusListener(TextField textField) {
        textField.focusedProperty().addListener((obsValue, oldVal, newVal) -> {
            if (newVal) {
                textField.setStyle(null);
                errorMessageLabelTop.setText("");
                errorMessageLabelTop.setStyle(null);
            } else {
                if (textField.getText().isBlank()) {
                    setTextFieldStyleForError(textField);
                }
            }
        });
    }
}
