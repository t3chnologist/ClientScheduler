package util;

import DAO.DAO_User;
import controller.Controller_Appointment;
import controller.Controller_Customer;
import controller.Controller_Main;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Control;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.Appointment;
import model.Customer;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Objects;

/**
 * This is a helper class for setting up scenes and stages.
 * @author Batnomin Terbish
 */
public class SceneHelper {
    /**
     * Method that shows the scene.
     * @param actionEvent event
     * @param viewPath view path
     * @throws IOException exception
     * */
    public static void show(ActionEvent actionEvent, String viewPath) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(SceneHelper.class.getResource(viewPath));
        loader.load();
        setSceneAndShow(actionEvent, loader);
    }

    /**
     * Method that passes the appointment or customer object, and calls the setSceneAndShow() method.
     * @param actionEvent actionEvent
     * @param object appointment or customer object passed from the Main page
     * @throws IOException IOException
     * @throws SQLException SQLException
     * */
    public static void passAndShow (ActionEvent actionEvent, Object object) throws IOException, SQLException {
        FXMLLoader loader = new FXMLLoader();
        if (object instanceof Appointment) {
            String viewPath= "/view/View_Appointment.fxml";
            loader.setLocation(SceneHelper.class.getResource(viewPath));
            loader.load();
            Controller_Appointment c = loader.getController();
            c.passAppointment((Appointment) object);
        }
        else if (object instanceof Customer) {
            String viewPath = "/view/View_Customer.fxml";
            loader.setLocation(SceneHelper.class.getResource(viewPath));
            loader.load();
            Controller_Customer cc = loader.getController();
            cc.passCustomer((Customer) object);
        }
        setSceneAndShow(actionEvent, loader);
    }

    /**
     * Method that sets the scene and shows the stage.
     * @param actionEvent actionEvent
     * @param loader FXMLoader
     * */
    private static void setSceneAndShow(ActionEvent actionEvent, FXMLLoader loader) {
        Stage stage = new Stage();
        Scene scene = new Scene(loader.getRoot());
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(((Control) actionEvent.getSource()).getScene().getWindow());
        stage.setScene(scene);
        stage.showAndWait();
    }

    /**
     * Method that displays the Main view.
     * @throws IOException IOException
     */
    public static void showMainView() throws IOException {
        if (DAO_User.getActiveUser() != null) {
            String viewPath = "/view/View_Main.fxml";
            Scene scene = new Scene(FXMLLoader.load(Objects.requireNonNull(SceneHelper.class.getResource(viewPath))));
            Stage stage = main.Main.getStage();
            stage.setScene(scene);
            stage.show();

            // upcoming appointments within 15 min
            try {
                if (!Controller_Main.upcomingAppointment().isEmpty()) {
                    AlertHelper.infoAlert("Appointment reminder",
                            "You have an upcoming appointment",
                            Controller_Main.upcomingAppointment());
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
