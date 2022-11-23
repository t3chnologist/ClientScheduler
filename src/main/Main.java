package main;

import DAO.DBConnection;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import util.AlertHelper;
import java.util.Locale;
import java.util.Objects;
import java.util.ResourceBundle;

/**
 * Software Development Capstone - C868:  Scheduling Application for Financial Consulting Firm
 *
 * Javadoc files can be found at C868_SoftwareCapstone/src/javadoc
 * @author Batnomin Terbish
 * @version 1.0
 * */
public class Main extends Application {
    static Stage stage;
    @FXML private final ResourceBundle rb = ResourceBundle.getBundle("util/Login", Locale.getDefault());
    /**
     * This method loads the Login page (View_Login.fxml)
     * @param stage The primary stage
     * @throws Exception exception
     * */
    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/view/View_Login.fxml")));
        Scene scene = new Scene(root);
        Main.stage = stage;
        stage.setOnCloseRequest(WindowEvent -> {
            boolean confirmed = AlertHelper.yesNoConfirmationAlert(
                    rb.getString("confirmExit"),
                    rb.getString("areYouSureExit"), "");
            if (confirmed) DBConnection.closeConnection();
            else WindowEvent.consume();
        });
        stage.setScene(scene);
        DBConnection.openConnection();
        stage.show();
    }
    /**
     * Method to get the stage
     * @return the stage
     * */
    public static Stage getStage() {
        return stage;
    }
    /** This is the main method. This is the first method that gets called when you run the program.
     * @param args An empty point*/
    public static void main(String[] args) {
        launch(args);
    }
}