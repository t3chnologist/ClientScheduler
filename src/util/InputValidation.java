package util;

import DAO.DAO_Appointment;
import DAO.DAO_User;
import javafx.scene.control.*;
import model.*;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.Locale;
import static controller.Controller_Login.recordLoginAttempt;
import static util.RegEx.*;
/**
 * This is a helper class that checks user inputs from Appointment and Customer pages.
 * @author Batnomin Terbish
 */

public class InputValidation {
    private static String alertHeader;
    private static String alertContent;

    /**
     * Method that checks username and password from Login page.
     * @param userNameField username to check
     * @param passwordField password to check
     * @return boolean true if both username and password are valid.
     * @throws SQLException SQLException
     */
    public static boolean loginCheck(TextField userNameField, PasswordField passwordField) throws SQLException {
        //match username with userNameRegex (letters, numbers, and underscores)
        if (userNameField.getText().matches(getUserNameRegex())) {
            //search for matching userName
            User matchingUser = DAO_User.queryUsers().stream()
                    .filter(u -> u.getUserName().equals(userNameField.getText()))
                    .findFirst().orElse(null);

            if (matchingUser != null) {
                //search for matching password
                if (matchingUser.getPassword().equals(passwordField.getText())) {
                    //set activeUser
                    DAO_User.setActiveUser(matchingUser);
                    //record successful login
                    recordLoginAttempt("user \"" + matchingUser, true);
                    return true;
                }
                else {
                    recordLoginAttempt("user \"" + matchingUser, false);
                    return false;
                }
            }
            else {
                recordLoginAttempt("<unknown> user \"" + userNameField.getText(), false);
            }
        }
        else {
            recordLoginAttempt("<unknown> user \"" + userNameField.getText() +
                    "\" - <RegEx mismatch>", false);
        }
        return false;
    }

    /**
     * Method that checks user inputs from Appointment page.
     * Method first calls the dateTimeCheck() method with start and end times,
     * then checks for overlapping appointment.
     * @param appointmentID username to check
     * @param customerCB customer to check
     * @param start time to check
     * @param end time to check
     * @return boolean true if there is no overlapping appointment.
     */
    public static boolean checkAppointmentInput(int appointmentID, ComboBox<Customer> customerCB,
                                                Timestamp start, Timestamp end) throws SQLException {
        int errorCount = 0;
        String dateTimeResult;
        alertHeader = "";
        alertContent = "";

        dateTimeResult = dateTimeCheck(start, end);

        if (!dateTimeResult.isEmpty()) {
            ++errorCount;
            alertHeader += "\n" + errorCount + ": Invalid start/end time.\n";
            alertContent += "\n" + errorCount + ": " + dateTimeResult + "\n";
        }
        //overlapping appointment check
        Appointment overlappingAppointment = DAO_Appointment.queryAppointments().stream()
                .filter(a -> ((a.getAppointmentID() != appointmentID) &&
                        (a.getCustomerID() == customerCB.getValue().getId())) &&
                        (start.equals(a.getStart()) || end.equals(a.getEnd()) ||
                                (start.after(a.getStart()) && start.before(a.getEnd())) ||
                                (end.after(a.getStart()) && end.before(a.getEnd()))))
                .findFirst().orElse(null);

        if (overlappingAppointment != null) {
            ++errorCount;
            alertHeader += "\n" + errorCount + ": Customer already has appointment during this time. See below:" +
                    "\n\n\t" + overlappingAppointment + " | Customer: " + overlappingAppointment.getCustomerName() + "\n";
        }

        return alertHeader.isBlank();
    }
    /**
     * Method that checks the start and end times. Method checks if end time
     * is before start time, and if start or end time is outside business hours.
     * @param start time to check
     * @param end time to check
     * @return the errorMessage
     */
    public static String dateTimeCheck(Timestamp start, Timestamp end) {

        Timestamp now = Timestamp.valueOf(LocalDateTime.now());
        LocalTime startEST = LocalTime.ofInstant(start.toInstant(), ZoneId.of("America/New_York"));
        LocalTime endEST = LocalTime.ofInstant(end.toInstant(),ZoneId.of("America/New_York"));
        LocalTime businessStart = LocalTime.of(8, 0);
        LocalTime businessEnd = LocalTime.of(22, 0);

        String errorMessage = "";

        // check if Start is before End, OR start is in the past
        if (end.before(start) || end.equals(start) || start.before(now)) {
            errorMessage = "Meeting time cannot be in the past. And \"Start\" must be before \"End\"." +
                    "\nCurrent time: " + LocalDateTime.now().format(DateTimeFormatter.ofPattern
                    ("MM-dd-yyyy HH:mm", Locale.ENGLISH)) + "\n";
        }
        // check if meeting is during business hours
        if ((startEST.isBefore(businessStart) || startEST.isAfter(businessEnd)) ||
                endEST.isBefore(businessStart) || endEST.isAfter(businessEnd)) {
            errorMessage = "Start/end is outside of business hours (8:00AM to 10:00PM EST)." +
                    "\nTimezone detected: " + ZoneId.systemDefault().getDisplayName(TextStyle.SHORT, Locale.getDefault());
        }
        return errorMessage;
    }

    /**
     * Method that checks user inputs from Customer page.
     * @param nameField to check
     * @param addressField to check
     * @param postalCodeField to check
     * @param phoneNumberField to check
     * @param countryComboBox country to check
     * @param stateProvinceComboBox first level division to check
     * @return boolean true if all field and comboBox values are valid.
     */
    public static boolean checkCustomerViewInput(TextField nameField, TextField addressField,
                                                 TextField postalCodeField, TextField phoneNumberField,
                                                 ComboBox<Country> countryComboBox,
                                                 ComboBox<FirstLvlDivision> stateProvinceComboBox) {
        int errorCount = 0;
        alertHeader = "";
        alertContent = "";

        if (!nameField.getText().matches(getTextRegex())) {
            ++errorCount;
            alertHeader += errorCount + ": Please enter a valid \"Name\"\n";
            setTextFieldStyleForError(nameField);
        }
        else {
            nameField.setStyle(null);
        }
        if (!addressField.getText().matches(getTextRegex())) {
            ++errorCount;
            alertHeader += errorCount + ": Please enter a valid \"Address\"\n";
            setTextFieldStyleForError(addressField);
        }
        else {
            addressField.setStyle(null);
        }

        if (!postalCodeField.getText().matches(getPostcodeRegex())) {
            ++errorCount;
            alertHeader += errorCount + ": Invalid \"postcode\" format.\n";
            setTextFieldStyleForError(postalCodeField);
        }
        else {
            postalCodeField.setStyle(null);
        }
        if (!phoneNumberField.getText().matches(getPhoneRegex())) {
            ++errorCount;
            alertHeader += errorCount + ": Invalid phone format.\n";
            setTextFieldStyleForError(phoneNumberField);
        }
        else {
            phoneNumberField.setStyle(null);
        }

        if (countryComboBox.getValue().getId() != stateProvinceComboBox.getValue().getCountryID()) {
            ++errorCount;
            alertHeader += errorCount + ": Invalid state/province selection";
            alertContent += errorCount + ": Please select the country first to filter all states/province.";
            setComboBoxStyleForError(countryComboBox);
        }
        else {
            countryComboBox.setStyle(null);
        }
        return alertHeader.isBlank();
    }

    /**
     * Method to get alertHeader string
     * @return alertHeader string
     * */
    public static String getAlertHeader() {
        return alertHeader;
    }
    /**
     * Method to get alertContent string
     * @return alertContent string
     * */
    public static String getAlertContent() {
        return alertContent;
    }

    /**
     * Method that sets Focus Listener for comboBox.
     * Using lambda for more compact and readable code.
     * @param comboBox to set focus listener
     */
    public static void comboBoxListener(ComboBox<?> comboBox) {
        comboBox.focusedProperty().addListener((obsValue, oldVal, newVal) -> {
            if (newVal) {
                comboBox.setStyle(null);
            } else if (oldVal) {
                if (comboBox.getSelectionModel().getSelectedItem() == null) {
                    setComboBoxStyleForError(comboBox);
                }
            }
        });
    }
    /**
     * Method that sets Focus Listener for textField. Using lambda for more compact and readable code.
     * @param textField to set focus listener
     * @param errorLabel to set text and style
     * */
    public static void textFieldListener(TextField textField, Label errorLabel) {
        textField.focusedProperty().addListener((obsValue, oldVal, newVal) -> {
            if (newVal) {
                textField.setStyle(null);
                errorLabel.setText("");
            } else {
                if (textField.getText().isBlank()) {
                    setTextFieldStyleForError(textField);
                } else if (!textField.getText().matches(RegEx.getTextRegex())) {
                    setTextFieldStyleForError(textField);
                    errorLabel.setText("Invalid character. Use hyphen, dot, comma.");
                }
            }
        });
    }
    /**
     * Method that sets Focus Listener for textField.
     * Using lambda for more compact and readable code.
     * @param textField to set focus listener
     * @param errorLabel to set text and style
     */
    public static void postalCodeFieldListener(TextField textField, Label errorLabel) {
        textField.focusedProperty().addListener((obsValue, oldVal, newVal) -> {
            if (newVal) {
                textField.setStyle(null);
                errorLabel.setText("");
            } else {
                if (textField.getText().isBlank()) {
                    setTextFieldStyleForError(textField);
                } else if (!textField.getText().matches(RegEx.getPostcodeRegex())) {
                    setTextFieldStyleForError(textField);
                    errorLabel.setText("Only letters & numbers allowed");
                }
            }
        });
    }
    /**
     * Method that sets Focus Listener for textField.
     * Using lambda for more compact and readable code.
     * @param textField to set focus listener
     * @param errorLabel to set text and style
     */
    public static void phoneFieldListener(TextField textField, Label errorLabel) {
        textField.focusedProperty().addListener((obsValue, oldVal, newVal) -> {
            if (newVal) {
                textField.setStyle(null);
                errorLabel.setText("");
            } else {
                if (textField.getText().isBlank()) {
                    setTextFieldStyleForError(textField);
                } else if (!textField.getText().matches(RegEx.getPhoneRegex())) {
                    setTextFieldStyleForError(textField);
                    errorLabel.setText("Only numbers and hyphens allowed.");
                }
            }
        });
    }
    /**
     *
     * Method that sets Focus Listener for datePicker.
     * using lambda for more compact and readable code.
     * @param datePicker to set focus listener.
     * @param errorLabel to set text and style.
     *
     * */
    public static void datePickerListener(DatePicker datePicker, Label errorLabel) {
        datePicker.focusedProperty().addListener((obsValue, oldVal, newVal) -> {
            if (newVal) {
                datePicker.setStyle(null);
                errorLabel.setText("");
            } else {
                if (datePicker.getValue() == null) {
                    setDatePickerStyleForError(datePicker);
                } else if (datePicker.getValue().isBefore(LocalDate.now())) {
                    setDatePickerStyleForError(datePicker);
                    errorLabel.setText("Past date");
                }
            }
        });
    }

    /**
     * Method that sets tableView style for blue
     * @param tableView to set
     * */
    public static void setTableViewForSearchResult(TableView tableView) {
        tableView.setStyle("-fx-background-color: rgba(255,239,239,0.97); " +
                "-fx-border-color: #7fc6d4; ");
    }

    /**
     * Method that sets TextField style for error/pink
     * @param textField to set
     * */
    public static void setTextFieldStyleForError(TextField textField) {
        textField.setStyle("-fx-background-color: rgba(255,239,239,0.97); " +
                "-fx-border-color: #ce9393; " +
                "-fx-border-radius: 5; -fx-background-radius: 5");
    }
    /**
     * Method that sets ComboBox style for error/pink
     * @param comboBox to set
     * */
    public static void setComboBoxStyleForError(ComboBox<?> comboBox) {
        comboBox.setStyle("-fx-background-color: rgba(255,239,239,0.97); " +
                "-fx-border-color: #ce9393; " +
                "-fx-border-radius: 5; -fx-background-radius: 5");
    }
    /**
     * Method that sets DatePicker style for error/pink
     * @param datePicker to set
     * */
    public static void setDatePickerStyleForError(DatePicker datePicker) {
        datePicker.setStyle("-fx-background-color: rgba(255,239,239,0.97); " +
                "-fx-border-color: #ce9393; " +
                "-fx-border-radius: 5; -fx-background-radius: 5");
    }
    /**
     * Method that sets Label style for error/pink
     * @param label to set
     * */
    public static void setLabelStyleForError(Label label) {
        label.setStyle("-fx-background-color: rgba(255,217,217,0.97); " +
                "-fx-border-color: #ce9393; " +
                "-fx-border-radius: 5; -fx-background-radius: 5");
    }
}
