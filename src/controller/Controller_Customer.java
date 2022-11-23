package controller;

import DAO.DAO_Country;
import DAO.DAO_Customer;
import DAO.DAO_FirstLvlDivision;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.Country;
import model.Customer;
import model.FirstLvlDivision;
import util.AlertHelper;
import util.InputValidation;
import java.net.URL;
import java.sql.SQLException;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import static util.InputValidation.*;

/**
 * This class is the controller for the "Customer" screen (/view/View_Customer.fxml).
 * Class also provides basic client-side input validations using focus listeners and onAction triggers.
 * @author Batnomin Terbish
 * */
public class Controller_Customer implements Initializable  {
    @FXML private Label customPageLabel, nameErrorMsg, addressErrorMsg, postalErrorMsg, phoneErrorMsg;
    @FXML private TextField idField, nameField, addressField, postalCodeField, phoneNumberField;
    @FXML private ComboBox<Country> countryCB;
    @FXML private ComboBox<FirstLvlDivision> stateProvinceCB;

    /**
     * Method call to initialize a controller after its root elements have been processed completely.
     * @param url The location for resolving relative paths for the root object
     * @param resourceBundle The resource localizing the root object
     * */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            countryCB.getItems().addAll(DAO_Country.queryCountries());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            stateProvinceCB.getItems().addAll(DAO_FirstLvlDivision.queryDivisions());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        customPageLabel.setText("New customer");
        textFieldListener(nameField, nameErrorMsg);
        textFieldListener(addressField, addressErrorMsg);
        postalCodeFieldListener(postalCodeField, postalErrorMsg);
        phoneFieldListener(phoneNumberField, phoneErrorMsg);
        comboBoxListener(countryCB);
        comboBoxListener(stateProvinceCB);
    }

    /**
     * Method that adds or updates customer record. It takes the user inputs, via textFields
     * and comboBoxes, and updates/inserts it into "customers" table after input validations.
     * This method is called by a button click ActionEvent on "Save" button.
     * @param actionEvent used for closing the stage
     * @throws SQLException for Appointment queries
     * */
    public void onSaveButtonClick(ActionEvent actionEvent) throws Exception {
        if (noMissingInput()) {
            boolean inputValidated = InputValidation.checkCustomerViewInput(nameField, addressField,
                    postalCodeField, phoneNumberField, countryCB, stateProvinceCB);

            if (inputValidated) {
                boolean result;
                if (customPageLabel.getText().contains("New")) {
                    result = DAO_Customer.addCustomer(nameField.getText(), addressField.getText(),
                            postalCodeField.getText(), phoneNumberField.getText(),
                            stateProvinceCB.getValue().getDivisionID());
                    if (result) {
                        System.out.println("Added new customer record!");
                    }
                }
                else {
                    result = DAO_Customer.updateCustomer(Integer.parseInt(idField.getText()),
                            nameField.getText(), addressField.getText(), postalCodeField.getText(),
                            phoneNumberField.getText(), stateProvinceCB.getValue().getDivisionID());
                    if (result) {
                        System.out.println("Customer record update successful!");
                    }
                }
                Stage stage = (Stage) ((Control) actionEvent.getSource()).getScene().getWindow();
                stage.close();
            }
            else {
                AlertHelper.errorAlert("Input error:", InputValidation.getAlertHeader(),
                        InputValidation.getAlertContent());
            }
        }
        else {
            AlertHelper.errorAlert("Input error", "All fields are required!", "");
        }
    }

    /**
     * Method that closes the current "Customer" view. It will close the view without prompt if
     * all fields and comboBoxes are null/blank. Or it will prompt for confirmation if at least
     * one field is inputted. This method is called by a button ActionEvent on "Cancel" button.
     * Using Stream to make it more compact and readable
     * @param actionEvent used for getting the stage
     * */
    public void onCancelButtonClick(ActionEvent actionEvent) {
        //Check if all fields are blank and all comboBoxes are null
        //Using Stream to make it more compact and readable
        if (Stream.of(nameField.getText(), addressField.getText(),
                        postalCodeField.getText(), phoneNumberField.getText())
                .allMatch(String::isBlank)
                &&
                Stream.of(countryCB.getSelectionModel().getSelectedItem(),
                                stateProvinceCB.getSelectionModel().getSelectedItem())
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
     * @Lambda Using Stream to make it more compact and readable
     * @return boolean. true if all field & comboBox objects are nonNull "AND" none of the fields are blank.
     * */
    private boolean noMissingInput() {
        //Using Stream to make it more compact and readable
        return Stream.of(nameField, addressField, postalCodeField, phoneNumberField,
                        countryCB.getSelectionModel().getSelectedItem(),
                        stateProvinceCB.getSelectionModel().getSelectedItem())
                .allMatch(Objects::nonNull)
                &&
                Stream.of(nameField.getText(), addressField.getText(),
                                postalCodeField.getText(), phoneNumberField.getText())
                        .noneMatch(String::isBlank);
    }
    /**
     * Method that receives customer object from "Controller_Main" and
     * sets "Edit customer" fields and comboBoxes with its values.
     * Lambda using Stream to make it more compact and readable
     * @param selectedCustomer Customer object received from "Controller_Main".
     * @throws SQLException SQLException
     * */
    public void passCustomer(Customer selectedCustomer) throws SQLException {
        customPageLabel.setText("Edit customer");
        idField.setText(String.valueOf(selectedCustomer.getId()));
        nameField.setText(selectedCustomer.getName());
        addressField.setText(selectedCustomer.getAddress());
        postalCodeField.setText(selectedCustomer.getPostalCode());
        phoneNumberField.setText(selectedCustomer.getPhone());
        //Using Stream to make it more compact and readable
        FirstLvlDivision divisionFound = DAO_FirstLvlDivision.queryDivisions().stream()
                .filter(div -> div.getDivisionID() == selectedCustomer.getDivID())
                .findFirst().orElseThrow();
        stateProvinceCB.getSelectionModel().select(divisionFound);
        //Using Stream to make it more compact and readable
        Country countryFound = DAO_Country.queryCountries().stream()
                .filter(country -> country.getId() == divisionFound.getCountryID())
                .findFirst().orElseThrow();
        countryCB.getSelectionModel().select(countryFound);
    }


    /**
     * Method that filters stateProvinceComboBox items based on countryComboBox value.
     * Triggered by countryComboBox selection.
     * Using Stream to make it more compact and readable.
     * @throws SQLException SQLException
     * */
    public void onCountryComboBoxSelect() throws SQLException {
        int countryID = countryCB.getSelectionModel().getSelectedItem().getId();

        //Using Stream to make it more compact and readable
        stateProvinceCB.setItems(DAO_FirstLvlDivision.queryDivisions().stream()
                .filter(division -> division.getCountryID() == countryID)
                .collect(Collectors.toCollection(FXCollections::observableArrayList)));

        stateProvinceCB.getSelectionModel().clearAndSelect(0);
    }
}
