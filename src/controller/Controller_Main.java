package controller;

import DAO.*;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import model.Appointment;
import model.Contact;
import model.Country;
import model.Customer;
import util.AlertHelper;
import util.ComboBoxItems;
import util.SceneHelper;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.WeekFields;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import static util.InputValidation.setTableViewForSearchResult;
import static util.InputValidation.setTextFieldStyleForError;

/**
 * This class is the controller for the "Main" screen (/view/View_Main.fxml).
 * @author Batnomin Terbish
 * */
public class Controller_Main implements Initializable {
    private ObservableList<Appointment> appointmentObservableList = null;
    @FXML private Label upcomingAppointmentLabel, activeUserLabel, reportOneCombinedResult,
            reportOneResultByType, reportOneResultByMonth, reportThreeComboResult, reportThreeMostApt;
    @FXML private TextField appointmentSearchField;
    @FXML private TextField customerSearchField;

    @FXML private RadioButton defaultViewRadioButton, currentWeekRadioButton, currentMonthRadioButton;
    @FXML private ComboBox<String> reportOneTypeCB, reportOneMonthCB;
    @FXML private ComboBox<Contact> reportOneContactCB;
    @FXML private ComboBox<Country> reportThreeCountryCB;
    @FXML private HBox aptMessageHBox;
    @FXML private TableView<Appointment> appointmentTableView, reportTwoAptTableView;
    @FXML private TableColumn<Appointment, Integer> aptIDCol, reportTwoAptIDCol;
    @FXML private TableColumn<Appointment, String> aptTitleCol, aptDescriptionCol, aptLocationCol, aptContactCol,
            aptTypeCol, reportTwoAptTitleCol, reportTwoDescriptionCol,
            reportTwoLocationCol, reportTwoContactCol, reportTwoTypeCol;
    @FXML private TableColumn<Appointment, LocalDate> aptDateCol, reportTwoDateCol;
    @FXML private TableColumn<Appointment, LocalTime> aptStartTimeCol, aptEndTimeCol,
            reportTwoStartTimeCol, reportTwoEndTimeCol;
    @FXML private TableColumn<Appointment, Integer> aptCustomerCol, reportTwoCustomerCol;
    @FXML private TableView<Customer> customerTableView;
    @FXML private TableColumn<Customer, Integer> customerIDCol;
    @FXML private TableColumn<Appointment, Integer> aptUserIDCol, reportTwoUserIDCol;
    @FXML private TableColumn<Customer, String> customerNameCol, customerAddressCol, postalCodeCol,
            phoneNumberCol, stateProvinceCol;
    private Object source;;
    private KeyEvent keyEvent;

    /**
     * Method call to initialize a controller after its root elements have been processed completely.
     * @param url The location for resolving relative paths for the root object
     * @param resourceBundle The resource localizing the root object
     * */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        activeUserLabel.setText("Logged in user:  \"" + DAO_User.getActiveUser() + "\"");

        //Setting up  customer tableView items
        try {
            customerTableView.setItems(DAO_Customer.queryCustomers());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        customerIDCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        customerNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        customerAddressCol.setCellValueFactory(new PropertyValueFactory<>("address"));
        postalCodeCol.setCellValueFactory(new PropertyValueFactory<>("postalCode"));
        stateProvinceCol.setCellValueFactory(new PropertyValueFactory<>("division"));
        phoneNumberCol.setCellValueFactory(new PropertyValueFactory<>("phone"));

        //Setting up Appointment tableView items
        try {
            ObservableList<Appointment> obsList = DAO_Appointment.queryAppointments();
            setAppointmentTableViewItems(appointmentTableView, obsList, aptIDCol, aptTitleCol, aptDescriptionCol,
                    aptLocationCol, aptContactCol, aptCustomerCol, aptUserIDCol, aptTypeCol, aptDateCol, aptStartTimeCol, aptEndTimeCol);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        //Setting up "Report One" items
        reportOneTypeCB.getItems().addAll(ComboBoxItems.getSampleAppointmentTypes());
        reportOneMonthCB.getItems().addAll(ComboBoxItems.getMonths());

        //Setting up "Report Two" items
        try {
            reportOneContactCB.getItems().addAll(DAO_Contact.queryContacts());

            setAppointmentTableViewItems(reportTwoAptTableView, null, reportTwoAptIDCol, reportTwoAptTitleCol,
                    reportTwoDescriptionCol, reportTwoLocationCol, reportTwoContactCol, reportTwoCustomerCol,
                    reportTwoUserIDCol, reportTwoTypeCol, reportTwoDateCol, reportTwoStartTimeCol, reportTwoEndTimeCol);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        //Setting up "Report Three" items
        try {
            reportThreeCountryCB.setItems(DAO_Country.queryCountries());
            reportThreeMostApt.setText(getCountryWithMostApt());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        try {
            refreshData();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    /**
     * Method that sets the appointmentTableView items.
     * @param tableView TableView to set
     * @param obsList ObservableList with Appointment(s) in it for tableView
     * @param aptTitleCol Title column to set
     * @param aptDescriptionCol Description column to set
     * @param aptLocationCol Location column to set
     * @param aptContactCol Contact column to set
     * @param aptCustomerCol Customer column to set
     * @param aptUserIDCol User column to set
     * @param aptTypeCol Type column to set
     * @param aptDateCol Date column to set
     * @param aptStartTimeCol Start time column to set
     * @param aptEndTimeCol End time column to set
     * */
    public void setAppointmentTableViewItems (TableView<Appointment> tableView, ObservableList<Appointment> obsList,
                                              TableColumn<Appointment, Integer> aptIDCol,
                                              TableColumn<Appointment, String> aptTitleCol,
                                              TableColumn<Appointment, String> aptDescriptionCol,
                                              TableColumn<Appointment, String> aptLocationCol,
                                              TableColumn<Appointment, String> aptContactCol,
                                              TableColumn<Appointment, Integer> aptCustomerCol,
                                              TableColumn<Appointment, Integer> aptUserIDCol,
                                              TableColumn<Appointment, String> aptTypeCol,
                                              TableColumn<Appointment, LocalDate> aptDateCol,
                                              TableColumn<Appointment, LocalTime> aptStartTimeCol,
                                              TableColumn<Appointment, LocalTime>  aptEndTimeCol) {
        tableView.setItems(obsList);
        aptIDCol.setCellValueFactory(new PropertyValueFactory<>("appointmentID"));
        aptTitleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
        aptDescriptionCol.setCellValueFactory(new PropertyValueFactory<>("description"));
        aptLocationCol.setCellValueFactory(new PropertyValueFactory<>("location"));
        aptContactCol.setCellValueFactory(new PropertyValueFactory<>("contactName"));
        aptCustomerCol.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        aptUserIDCol.setCellValueFactory(new PropertyValueFactory<>("userID"));
        aptTypeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
        aptDateCol.setCellValueFactory(new PropertyValueFactory<>("appointmentDate"));
        aptStartTimeCol.setCellValueFactory(new PropertyValueFactory<>("startTime"));
        aptEndTimeCol.setCellValueFactory(new PropertyValueFactory<>("endTime"));
    }

    /**
     * Method that calls the SceneHelper.show() method to show the Customer page.
     * Triggered by actionEvent on "Add new customer" button under Customer tab.
     * @param event actionEvent
     * @throws Exception exception
     * */
    public void onAddNewCustomerButtonClick(ActionEvent event) throws Exception {
        SceneHelper.show(event, "/view/View_Customer.fxml");
        refreshData();
    }

    /**
     * Method that calls the util/SceneHelper method to show the Appointment page.
     * Triggered by actionEvent on "Add new appointment" button under Appointment tab.
     * @param event actionEvent
     * @throws IOException exception
     * @throws SQLException exception
     * */
    public void onAddNewAppointmentButtonClick(ActionEvent event) throws IOException, SQLException {
        SceneHelper.show(event, "/view/View_Appointment.fxml");
        refreshData();
    }
    /**
     * Method that calls the SceneHelper.passAndShow() with actionEvent and selectedCustomer.
     * Triggered by actionEvent on "Edit customer" button.
     * @param event actionEvent
     * @throws IOException exception
     * @throws SQLException exception
     * */
    public void onEditCustomerButtonClick(ActionEvent event) throws IOException, SQLException {
        Customer selectedCustomer = customerTableView.getSelectionModel().getSelectedItem();
        if (selectedCustomer != null) {
            SceneHelper.passAndShow(event, selectedCustomer);
            refreshData();
        }
        else {
            AlertHelper.errorAlert("Edit error: No customer selected",
                    "Please select the customer first", "No customer selected");
        }
    }
    /**
     * Method that calls the SceneHelper.passAndShow() with actionEvent and selectedAppointment.
     * Triggered by actionEvent on "Edit appointment" button.
     * @param event actionEvent
     * @throws IOException exception
     * @throws SQLException exception
     * */
    public void onEditAppointmentButtonClick(ActionEvent event) throws SQLException, IOException {
        Appointment selectedAppointment = appointmentTableView.getSelectionModel().getSelectedItem();
        if (selectedAppointment != null) {
            SceneHelper.passAndShow(event, selectedAppointment);
            refreshData();
        }
        else {
            AlertHelper.errorAlert("Edit error: No appointment selected",
                    "Please select the appointment first", "No appointment selected");
        }

    }
    /**
     * Method that deletes the selected customer and its appointment(s).
     * Triggered by actionEvent on "Delete customer" button under Customer tab.
     * @throws SQLException exception
     * */
    public void onDeleteCustomerButtonClick() throws SQLException {
        Customer selectedCustomer = customerTableView.getSelectionModel().getSelectedItem();
        if (selectedCustomer != null) {
            String customerName = selectedCustomer.getName();
            boolean confirmed = AlertHelper.yesNoConfirmationAlert("Delete confirmation",
                    "Are you sure you want to delete?\n\n\tCustomer:  " + customerName,
                    "Existing customer appointment(s) will be deleted!");
            if (confirmed) {
                boolean deleted = DAO_Customer.deleteCustomer(selectedCustomer.getId());
                refreshData();
                if (deleted) {
                    String infoContent = "Deleted customer: " + customerName;
                    AlertHelper.infoAlert("Success", "Deletion successful!", infoContent);
                }
            }
        }
        else {
            AlertHelper.errorAlert("Delete error: No customer selected",
                    "Please select the customer first", "No customer selected");
        }
    }
    /**
     * Method that deletes the selected appointment.
     * Triggered by actionEvent on "Cancel appointment" button under Appointment tab.
     * @throws SQLException exception
     * */
    @FXML private void onDeleteAppointmentButtonClick() throws SQLException {
        Appointment selectedAppointment = appointmentTableView.getSelectionModel().getSelectedItem();
        if (selectedAppointment != null) {
            int aptID = selectedAppointment.getAppointmentID();
            String aptType = selectedAppointment.getType();
            String alertContent = "Appointment ID \"" +  aptID + "\", type \"" + aptType + "\"";
            boolean confirmed = AlertHelper.yesNoConfirmationAlert(
                    "Appointment cancellation",
                    "Are you sure you want to cancel this appointment?",
                    "\t" + alertContent);
            if (confirmed) {
                boolean deleted = DAO_Appointment.deleteByAppointmentID(selectedAppointment.getAppointmentID());
                refreshData();
                if (deleted) {
                    AlertHelper.infoAlert("Success!",
                            alertContent + ", has been deleted.","");
                }
            }
        }
        else {
            AlertHelper.errorAlert("Delete error: No appointment selected",
                    "Please select the appointment first", "No appointment selected");
        }
    }

    /**
     * Method that checks for upcoming appointment by user.
     * Lambda Using Stream to make it more compact and readable
     * @return the upcomingAppointment in String format
     * @throws SQLException for Appointment queries
     * */
    public static String upcomingAppointment() throws SQLException {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime nowPlus15Min = LocalDateTime.now().plusMinutes(15);
        int activeUserID = DAO_User.getActiveUser().getUserID();

        // Returns the upcomingAppointment in String format
        // Using Stream to make it more compact and readable
        return DAO_Appointment.queryAppointments().stream()
                .filter(a -> (a.getUserID() == activeUserID &&
                        (a.getStartLDT().isAfter(now) && a.getStartLDT().isBefore(nowPlus15Min) ||
                                (a.getEndLDT().isAfter(now) && a.getEndLDT().isBefore(nowPlus15Min)))))
                .findFirst().map(Object::toString).orElse("");
    }

    /**
     * Method that disconnects the DB connection and exits the platform after confirmation.
     * Triggered by actionEvent (click) on the Exit button.
     * */
    public void onExitButtonClick() {
        boolean confirmed = AlertHelper.yesNoConfirmationAlert("Confirm exit",
                "Are you sure you want to exit?", "");
        if (confirmed) {
            DBConnection.closeConnection();
            Platform.exit();
        }
    }
    /**
     * Method that calls refreshData() method.
     * Triggered by actionEvent on the Main view radioButton select.
     * @throws SQLException for Appointment queries
     * */
    @FXML private void onRadioButtonSelect() throws SQLException {
        refreshData();
    }

    /**
     * Method that returns the total number of customer appointments by type, month, or both type and month.
     * Triggered by actionEvent on ReportOne "Country and Month" comboBoxes under Report tab.
     * @lambda Using Stream to make it more compact and readable
     * @throws SQLException for Appointment queries
     * */
    @FXML private void onReportOneTypeMonthSelect() throws SQLException {
        if (reportOneTypeCB.getSelectionModel().getSelectedItem() != null) {
            //Using Stream to make it more compact and readable
            reportOneResultByType.setText(String.valueOf(DAO_Appointment.queryAppointments().stream()
                    .filter(a -> a.getType().equals(reportOneTypeCB.getValue())).count()));
        }

        if (reportOneMonthCB.getSelectionModel().getSelectedItem() != null) {
            //Using Stream to make it more compact and readable
            reportOneResultByMonth.setText(String.valueOf(DAO_Appointment.queryAppointments().stream().
                    filter(a -> a.getStartLDT().getMonthValue() == Integer.parseInt(reportOneMonthCB.getValue())).count()));
        }

        if (reportOneTypeCB.getSelectionModel().getSelectedItem() != null &&
                reportOneMonthCB.getSelectionModel().getSelectedItem() != null) {

            reportOneCombinedResult.setText(String.valueOf(DAO_Appointment.queryAppointments().stream().
                    filter(a -> (a.getType().equals(reportOneTypeCB.getValue())) &&
                            (a.getStartLDT().getMonthValue() == Integer.parseInt(reportOneMonthCB.getValue()))).count()));
        }
    }

    /**
     * Method that filters appointments by contact for "reportTwoAptTableView" tableView.
     * Triggered by actionEvent on ReportTwo "Contact" comboBox under Report tab.
     * @lambda Using Stream to make it more compact and readable
     * @throws SQLException for Appointment queries
     * */
    @FXML private void onReportTwoContactCBSelect() throws SQLException {
        reportTwoAptTableView.setItems(DAO_Appointment.queryAppointments().stream()
                .filter(a -> a.getContactID() == reportOneContactCB.getSelectionModel().getSelectedItem().getId())
                .collect(Collectors.toCollection(FXCollections::observableArrayList)));
    }
    /**
     * Method that returns the total number of appointments by countries.
     * Triggered by actionEvent on ReportThree "Country" comboBox under Report tab.
     * @lambda Using Stream to make it more compact and readable
     * @throws SQLException for Appointment queries
     * */
    @FXML private void onReportThreeCountryCBSelect() throws SQLException {
        reportThreeMostApt.setText(getCountryWithMostApt());
        int countryID = reportThreeCountryCB.getSelectionModel().getSelectedItem().getId();
        String countryName = reportThreeCountryCB.getSelectionModel().getSelectedItem().getName();
        long aptCountByCountry = DAO_Appointment.queryAppointments().stream()
                .filter(a -> a.getCountryID() == countryID).count();
        String result = "Found \"" + aptCountByCountry + "\" appointment(s) with customer(s) from " + countryName + ".";
        reportThreeComboResult.setText(result);
    }

    /**
     * Method that returns the country with the greatest number of customer appointments.
     * @lambda Using Stream to make it more compact and readable
     * @throws SQLException for Appointment queries
     * */
    private String getCountryWithMostApt () throws SQLException {
        Country countryWithMostApt = null;
        long appointmentCount;
        long mostAppointment = 0;
        String result = "";
        for (Country country : DAO_Country.queryCountries()) {
            appointmentCount = DAO_Appointment.queryAppointments().stream()
                    .filter(a -> a.getCountryID() == country.getId()).count();
            if (appointmentCount >= mostAppointment) {
                mostAppointment = appointmentCount;
                countryWithMostApt = country;
            }
        }
        if (countryWithMostApt != null) {
            return countryWithMostApt.getName() + " with " + mostAppointment + " appointment(s).";
        }
        return result;
    }

    /**
     * Search method that sets the appointmentTableView based on the search text
     * */
    public void onAppointmentsSearchEvent() {

        String searchValue = appointmentSearchField.getText().toLowerCase();
        appointmentSearchField.setStyle(null);
        appointmentTableView.setStyle(null);

        if (!searchValue.isBlank() && !appointmentObservableList.isEmpty()) {

            if (searchValue.matches("[0-9.]+")) {
                int searchNum = Integer.parseInt(searchValue);
                ObservableList<Appointment> searchResult = appointmentObservableList.stream()
                        .filter(a -> a.getAppointmentID() == searchNum ||
                                a.getUserID() == searchNum)
                        .collect(Collectors.toCollection(FXCollections::observableArrayList));

                appointmentTableView.setItems(searchResult);
            }
            else {
                ObservableList<Appointment> searchResult = appointmentObservableList.stream()
                        .filter(a -> Integer.toString(a.getAppointmentID()).contains(searchValue) ||
                                a.getTitle().toLowerCase().contains(searchValue) ||
                                a.getDescription().toLowerCase().contains(searchValue) ||
                                a.getLocation().toLowerCase().contains(searchValue) ||
                                a.getContactName().toLowerCase().contains(searchValue) ||
                                a.getCustomerName().toLowerCase().contains(searchValue) ||
                                Integer.toString(a.getUserID()).contains(searchValue) ||
                                a.getType().toLowerCase().contains(searchValue) ||
                                a.getAppointmentDate().toString().contains(searchValue))
                        .collect(Collectors.toCollection(FXCollections::observableArrayList));

                appointmentTableView.setItems(searchResult);

                if (searchResult.isEmpty()) {
                    setTextFieldStyleForError(appointmentSearchField);
                    appointmentTableView.setPlaceholder(new Label("We couldn't find any results for \"" +
                            searchValue + "\""));
                }
            }

            setTableViewForSearchResult(appointmentTableView);
        }
    }
    /**
     * Search method that sets the customerTableView based on the search text
     * @lambda Using Stream to make it more compact and readable
     * @throws SQLException for Customer queries
     * */

    public void onCustomerSearchEvent(ActionEvent actionEvent) throws SQLException {

        String searchText = customerSearchField.getText().toLowerCase();
        ObservableList<Customer> customers = DAO_Customer.queryCustomers();

        if (!searchText.isBlank() && !customers.isEmpty()) {
            ObservableList<Customer> searchResult = customers.stream()
                    .filter(c -> Integer.toString(c.getId()).contains(searchText) ||
                                c.getName().toLowerCase().contains(searchText) ||
                                c.getAddress().toLowerCase().contains(searchText) ||
                                c.getPostalCode().toLowerCase().contains(searchText) ||
                                c.getDivision().toLowerCase().contains(searchText) ||
                                c.getPhone().contains(searchText))
                    .collect(Collectors.toCollection(FXCollections::observableArrayList));

            customerTableView.setItems(searchResult);

            if (searchResult.isEmpty()) {
                setTextFieldStyleForError(customerSearchField);
                customerTableView.setPlaceholder(new Label("We couldn't find any results for \"" +
                        searchText + "\""));
            }
        }
    }

    /**
     * KeyEvent method that listens for ENTER and BACK_SPACE keys for search field
     * @lambda Using Stream to make it more compact and readable
     * @throws SQLException for Appointment queries
     * */
    public void onKeyPressedAtSearch(KeyEvent keyEvent) throws SQLException {
        if (((Node) keyEvent.getSource()).getId().equals(appointmentSearchField.getId())) {
            if (appointmentSearchField.getText().isBlank()) {
                refreshData();
                appointmentTableView.setStyle(null);
                appointmentSearchField.setStyle(null);
            }
        }
        else if (((Node) keyEvent.getSource()).getId().equals(customerSearchField.getId())) {
            if (customerSearchField.getText().isBlank()) {
                customerSearchField.setStyle(null);
                customerTableView.setPlaceholder(null);
                customerTableView.setItems(DAO_Customer.queryCustomers());
            }
        }
    }

    /**
     * Method that sets tableView items and upcomingAppointment information on the Main view.
     * Lambda Using Stream to make it more compact and readable.
     * @throws SQLException SQLException
     * */
    protected void refreshData() throws SQLException {

        appointmentTableView.setPlaceholder(null);

        if (currentMonthRadioButton.isSelected()) {
            //Using Stream to make it more compact and readable
            appointmentObservableList = DAO_Appointment.queryAppointments().stream()
                    .filter(a -> a.getStartLDT().getMonthValue() == LocalDateTime.now().getMonthValue())
                    .collect(Collectors.toCollection(FXCollections::observableArrayList));
        } else if (currentWeekRadioButton.isSelected()) {
            LocalDateTime startOfWeek = LocalDate.now().with(WeekFields.ISO.getFirstDayOfWeek())
                    .atStartOfDay().minusDays(1);
            LocalDateTime endOfWeek = startOfWeek.plusWeeks(1);
            //Using Stream to make it more compact and readable
            appointmentObservableList = DAO_Appointment.queryAppointments().stream()
                    .filter(a -> a.getStartLDT().isAfter(startOfWeek) && a.getEndLDT().isBefore(endOfWeek))
                    .collect(Collectors.toCollection(FXCollections::observableArrayList));

        } else if (defaultViewRadioButton.isSelected()) {
            appointmentObservableList = DAO_Appointment.queryAppointments();
        }

        appointmentTableView.setItems(appointmentObservableList);

        if (!appointmentSearchField.getText().isBlank()) {
            onAppointmentsSearchEvent();
        }

        // set upcoming appointment message text and style
        if (!upcomingAppointment().isEmpty()) {
            upcomingAppointmentLabel.setText(upcomingAppointment());
            aptMessageHBox.setStyle("-fx-background-color: #BFD4DB; -fx-background-radius: 5");
        } else {
            upcomingAppointmentLabel.setText("There is no upcoming appointment.");
            aptMessageHBox.setStyle(null);
        }
        customerTableView.setItems(DAO_Customer.queryCustomers());
        reportThreeMostApt.setText(getCountryWithMostApt());
    }
}