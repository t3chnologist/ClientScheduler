package controller;
import DAO.DAO_Appointment;
import DAO.DAO_Contact;
import DAO.DAO_Customer;
import DAO.DAO_User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.Appointment;
import model.Contact;
import model.Customer;
import model.User;
import util.AlertHelper;
import util.InputValidation;
import util.ComboBoxItems;
import java.net.URL;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.stream.Stream;
import static util.InputValidation.*;

/**
 * This class is the controller for the "Appointment" screen (/view/View_Appointment.fxml).
 * Class also provides basic client-side input validations using focus listeners and onAction triggers.
 * @author Batnomin Terbish
 * */
public class Controller_Appointment implements Initializable {
    private int appointmentID;
    @FXML private ComboBox<User> userCB;
    @FXML private ComboBox<String> typeCB;
    @FXML private TextField appointmentIDField, titleField, descriptionField, locationField;
    @FXML private ComboBox<Contact> contactCB;
    @FXML private ComboBox<Customer> customerCB;
    @FXML private DatePicker appointmentDatePicker;
    @FXML private ComboBox<String> startHourCB, startMinCB, endHourCB, endMinCB;
    @FXML private Label customPageLabel, titleErrorMsg, descriptionErrorMsg, locationErrorMsg,
            dateErrorMsg, startTimeErrorMsg, endTimeErrorMsg, bottomErrorLabel;

    /**
     * Method call to initialize a controller after its root elements have been processed completely.
     * @param url The location for resolving relative paths for the root object
     * @param resourceBundle The resource localizing the root object
     * */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            contactCB.getItems().addAll(DAO_Contact.queryContacts());
            customerCB.getItems().addAll(DAO_Customer.queryCustomers());
            userCB.getItems().addAll(DAO_User.queryUsers());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        typeCB.getItems().addAll(ComboBoxItems.getSampleAppointmentTypes());
        startHourCB.getItems().addAll(ComboBoxItems.getHours());
        endHourCB.getItems().addAll(ComboBoxItems.getHours());
        startMinCB.getItems().addAll(ComboBoxItems.getMinutes());
        endMinCB.getItems().addAll(ComboBoxItems.getMinutes());

        customPageLabel.setText("New appointment");
        appointmentID = 0;
        textFieldListener(descriptionField, descriptionErrorMsg);
        textFieldListener(titleField, titleErrorMsg);
        textFieldListener(descriptionField, descriptionErrorMsg);
        textFieldListener(locationField, locationErrorMsg);
        datePickerListener(appointmentDatePicker, dateErrorMsg);

        comboBoxListener(typeCB);
        comboBoxListener(contactCB);
        comboBoxListener(customerCB);
        comboBoxListener(userCB);
        comboBoxListener(startHourCB);
        comboBoxListener(startMinCB);
        comboBoxListener(endHourCB);
        comboBoxListener(endMinCB);
    }

    /**
     * Method that adds or updates appointment record. It takes the user inputs, via textFields
     * and comboBoxes, and updates/inserts it into "appointments" table after input validations.
     * This method is called by a button click ActionEvent on "Save" button.
     * @param actionEvent used for closing the stage
     * @throws SQLException for Appointment queries
     * */
    public void onSaveButtonClick(ActionEvent actionEvent) throws SQLException {


        if(noMissingInput() && noInvalidDateTime()) {

            Timestamp start = getStart();
            Timestamp end = getEnd();

            boolean validated = InputValidation.checkAppointmentInput(appointmentID, customerCB, start, end);

            if (validated) {

                String title = titleField.getText();
                String description = descriptionField.getText();
                String location = locationField.getText();
                String type = typeCB.getSelectionModel().getSelectedItem();
                int contactID = contactCB.getSelectionModel().getSelectedItem().getId();
                int customerID = customerCB.getSelectionModel().getSelectedItem().getId();
                int userID = userCB.getSelectionModel().getSelectedItem().getUserID();

                boolean successfulDBUpdate;

                if (customPageLabel.getText().contains("New") || appointmentID == 0) {
                    successfulDBUpdate = DAO_Appointment.addNewAppointment(title, description, location,
                            type, contactID, customerID, userID, start, end);
                    if (successfulDBUpdate) {
                        System.out.println("Inserted record!");
                    }
                }
                else {
                    successfulDBUpdate = DAO_Appointment.updateAppointment(appointmentID, title, description, location,
                            type, contactID, customerID, userID, start, end);
                    if (successfulDBUpdate) {
                        System.out.println("Updated record!");
                    }
                }
                Stage stage = (Stage) ((Control) actionEvent.getSource()).getScene().getWindow();
                stage.close();
            }
            else {
                AlertHelper.errorAlert("Input error:",
                        InputValidation.getAlertHeader(), InputValidation.getAlertContent());
            }
        }
        else {
            AlertHelper.errorAlert("Input error", "All fields are required", "");
        }
    }

    /**
     * Method that closes the current "Appointment" view. It will close the view without prompt if
     * all fields and comboBoxes are null/blank. Or it will prompt for confirmation if at least
     * one field is inputted. This method is called by a button ActionEvent on "Cancel" button.
     * Using lambda for more compact and readable code.
     * @param actionEvent used for getting the stage
     * */
    public void onCancelButtonClick(ActionEvent actionEvent) {
        //Check if all fields are blank and all comboBoxes are null
        //Using Stream to make it more compact and readable
        if (Stream.of(titleField.getText(), descriptionField.getText(), locationField.getText())
                .allMatch(String::isBlank) &&
                Stream.of(contactCB.getSelectionModel().getSelectedItem(), customerCB.getSelectionModel().getSelectedItem(),
                                userCB.getSelectionModel().getSelectedItem(), startHourCB.getSelectionModel().getSelectedItem(),
                                startMinCB.getSelectionModel().getSelectedItem(), endHourCB.getSelectionModel().getSelectedItem(),
                                endMinCB.getSelectionModel().getSelectedItem(), appointmentDatePicker.getValue())
                        .allMatch(Objects::isNull)) {

            Stage stage = (Stage) ((Control) actionEvent.getSource()).getScene().getWindow();
            stage.close();
        }
        else {
            boolean confirmed = AlertHelper.yesNoConfirmationAlert("Confirmation",
                    "Are you sure you want to cancel?", "Any unsaved changes will be discarded.");
            if (confirmed) {
                Stage stage = (Stage) ((Control) actionEvent.getSource()).getScene().getWindow();
                stage.close();
            }
        }
    }

    /**
     * Method that checks for missing user inputs.
     * Using lambda for more compact and readable code.
     * @return boolean. true if all field & comboBox objects are nonNull "AND" none of the fields are blank
     * */
    private boolean noMissingInput() {
        //Using Stream to make it more compact and readable
        return Stream.of(titleField, descriptionField, locationField, appointmentDatePicker.getValue(),
                        typeCB.getSelectionModel().getSelectedItem(), contactCB.getSelectionModel().getSelectedItem(),
                        customerCB.getSelectionModel().getSelectedItem(), userCB.getSelectionModel().getSelectedItem(),
                        startHourCB.getSelectionModel().getSelectedItem(), startMinCB.getSelectionModel().getSelectedItem(),
                        endHourCB.getSelectionModel().getSelectedItem(), endMinCB.getSelectionModel().getSelectedItem())
                .allMatch(Objects::nonNull)
                &&
                Stream.of(titleField.getText(), descriptionField.getText(), locationField.getText())
                        .noneMatch(String::isBlank);
    }

    /**
     * Method that receives appointment object from "Controller_Main" and
     * sets "Edit appointment" fields and comboBoxes with its values.
     * Using lambda for more compact and readable code.
     * @param selectedAppointment Appointment object received from "Controller_Main".
     * @throws SQLException for Customer and Contact queries
     * */
    public void passAppointment(Appointment selectedAppointment) throws SQLException {
        customPageLabel.setText("Edit appointment");
        appointmentID = selectedAppointment.getAppointmentID();
        appointmentIDField.setText(String.valueOf(appointmentID));
        titleField.setText(selectedAppointment.getTitle());
        descriptionField.setText(selectedAppointment.getDescription());
        locationField.setText(selectedAppointment.getLocation());
        typeCB.setValue(selectedAppointment.getType());
        //Using Stream to make it more compact and readable
        Contact contactFound = DAO_Contact.queryContacts().stream()
                .filter(contact -> contact.getId() == selectedAppointment.getContactID())
                .findFirst().orElseThrow();
        contactCB.getSelectionModel().select(contactFound);
        //Using Stream to make it more compact and readable
        Customer customerFound = DAO_Customer.queryCustomers().stream()
                .filter(customer -> customer.getId() == selectedAppointment.getCustomerID())
                .findFirst().orElseThrow();
        customerCB.getSelectionModel().select(customerFound);
        //Using Stream to make it more compact and readable
        User userFound = DAO_User.queryUsers().stream()
                .filter(user -> user.getUserID() == selectedAppointment.getUserID())
                .findFirst().orElseThrow();
        userCB.getSelectionModel().select(userFound);
        appointmentDatePicker.setValue(selectedAppointment.getStartLDT().toLocalDate());
        startHourCB.setValue(String.valueOf(selectedAppointment.getStartLDT().getHour()));
        startMinCB.setValue(String.valueOf(selectedAppointment.getStartLDT().getMinute()));
        endHourCB.setValue(String.valueOf(selectedAppointment.getEndLDT().getHour()));
        endMinCB.setValue(String.valueOf(selectedAppointment.getEndLDT().getMinute()));
    }


    /**
     * Method that checks if error message labels are empty.
     * Using lambda for more compact and readable code.
     * @return boolean. true if all labels have empty text.
     * */
    private boolean noInvalidDateTime() {
        //Using Stream to make it more compact and readable
        return Stream.of(titleErrorMsg.getText(), descriptionErrorMsg.getText(),
                        locationErrorMsg.getText(), dateErrorMsg.getText(),
                        startTimeErrorMsg.getText(), endTimeErrorMsg.getText(),
                        bottomErrorLabel.getText())
                .allMatch(String::isEmpty);
    }
    /** Method that returns startTime in Timestamp format. */
    private Timestamp getStart() {
        return Timestamp.valueOf(LocalDateTime.of(appointmentDatePicker.getValue(),
                LocalTime.of(Integer.parseInt(startHourCB.getValue()),
                        Integer.parseInt(startMinCB.getValue()))));
    }
    /** Method that returns endTime in Timestamp format. */
    private Timestamp getEnd() {
        return Timestamp.valueOf(LocalDateTime.of(appointmentDatePicker.getValue(),
                LocalTime.of(Integer.parseInt(endHourCB.getValue()),
                        Integer.parseInt(endMinCB.getValue()))));
    }

    /**
     * Method that performs client-side validation on date-time entries.
     * Triggered by "date - hour - min" comboBox selections.
     * Checks for invalid dates and invalid start/end time.
     * Checks local start/end hours against business hours in EST.
     * */
    public void onDateTimeSelection() {

        if (appointmentDatePicker.getValue() != null) {
            if (appointmentDatePicker.getValue().isBefore(LocalDate.now())) {
                setDatePickerStyleForError(appointmentDatePicker);
                dateErrorMsg.setText("Past date.");
            }
            else {
                appointmentDatePicker.setStyle(null);
                dateErrorMsg.setText("");
            }
        }
        //check to see if all datetime comboBoxes are selected (nonNull)
        if (Stream.of(appointmentDatePicker.getValue(), startHourCB.getSelectionModel().getSelectedItem(),
                startMinCB.getSelectionModel().getSelectedItem(), endHourCB.getSelectionModel().getSelectedItem(),
                endMinCB.getSelectionModel().getSelectedItem()).allMatch(Objects::nonNull)) {

            //assigns the datetime check result (string) to bottomErrorLabel
            String dateTimeCheckResult = dateTimeCheck(getStart(), getEnd());
            if (!dateTimeCheckResult.isEmpty()) {
                bottomErrorLabel.setText(dateTimeCheckResult);
                startTimeErrorMsg.setText("Check Start/End");
                endTimeErrorMsg.setText("Check Start/End");
                setComboBoxStyleForError(startHourCB);
                setComboBoxStyleForError(startMinCB);
                setComboBoxStyleForError(endHourCB);
                setComboBoxStyleForError(endMinCB);
            }
            else {
                bottomErrorLabel.setText("");
                startTimeErrorMsg.setText("");
                endTimeErrorMsg.setText("");
                startHourCB.setStyle(null);
                startMinCB.setStyle(null);
                endHourCB.setStyle(null);
                endMinCB.setStyle(null);
            }
        }
    }
}
