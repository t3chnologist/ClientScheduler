package DAO;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Appointment;
import model.Customer;

import java.sql.*;
/**
 * This abstract class handles "customers" table queries.
 * @author Batnomin Terbish
 */
public class DAO_Customer {
    private static final Connection c = DBConnection.getConnection();
    private static final Timestamp currentTimeStamp = new Timestamp(System.currentTimeMillis());
    /**
     * Method that queries all customers from the "customers" table.
     * @return list of all customers in ObservableList
     * @throws SQLException SQLException
     */
    public static ObservableList<Customer> queryCustomers() throws SQLException {
        ObservableList<Customer> allCustomers = FXCollections.observableArrayList();
        String queryStr = "SELECT customers.Customer_ID, customers.Customer_Name, customers.Address, \n" +
                "\tcustomers.Postal_Code, customers.Phone, customers.Division_ID, \n" +
                "\tfirst_level_divisions.Division, countries.Country\n" +
                "FROM customers, first_level_divisions, countries \n" +
                "WHERE customers.Division_ID=first_level_divisions.Division_ID \n" +
                "AND first_level_divisions.Country_ID=countries.Country_ID;";
        try {
            PreparedStatement ps = c.prepareStatement(queryStr);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                int customerID = rs.getInt("Customer_ID");
                String name = rs.getString("Customer_Name");
                String address = rs.getString("Address");
                String postalCode = rs.getString("Postal_Code");
                String phone = rs.getString("Phone");
                int divID = rs.getInt("Division_ID");
                String division = rs.getString("Division");
                String country = rs.getString("Country");
                Customer newCustomer = new Customer(customerID, name, address, postalCode,
                        phone, divID, division, country);
                allCustomers.add(newCustomer);
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        return allCustomers;
    }
    /**
     * Method that adds customer record to "customers" table.
     * @param customerName to add
     * @param customerAddress to add
     * @param postalCode to add
     * @param customerPhone to add
     * @param divisionID to add
     * @return boolean true if successfully added
     * @throws SQLException SQLException
     */
    public static boolean addCustomer(String customerName, String customerAddress, String postalCode,
                                      String customerPhone, int divisionID) throws SQLException {
        String queryStr = "INSERT INTO customers (Customer_Name, Address, Postal_Code, Phone, Create_Date, " +
                "Created_By, Last_Update, Last_Updated_By, Division_ID)" +
                "VALUES (?,?,?,?,?,?,?,?,?)";
        PreparedStatement ps = c.prepareStatement(queryStr);

        ps.setString(1, customerName);
        ps.setString(2, customerAddress);
        ps.setString(3, postalCode);
        ps.setString(4, customerPhone);
        ps.setTimestamp(5, currentTimeStamp);
        ps.setString(6, DAO_User.getActiveUser().getUserName());
        ps.setTimestamp(7, currentTimeStamp);
        ps.setString(8, DAO_User.getActiveUser().getUserName());
        ps.setInt(9, divisionID);

        return ps.executeUpdate() > 0;
    }
    /**
     * Method that updates customer record to "customers" table.
     * @param customerID of the customer to update
     * @param customerName to add
     * @param customerAddress to add
     * @param postalCode to add
     * @param customerPhone to add
     * @param divisionID to add
     * @return boolean true if successfully updated
     * @throws SQLException SQLException
     */
    public static boolean updateCustomer(int customerID, String customerName, String customerAddress,
                                         String postalCode, String customerPhone, int divisionID) throws SQLException {
        String queryStr = "UPDATE customers " +
                "SET Customer_Name = ?, Address = ?, Postal_Code = ?, Phone = ?, " +
                "Last_Update = ?, Last_Updated_By = ?, Division_ID = ? " +
                "WHERE Customer_ID = ?";

        PreparedStatement ps = c.prepareStatement(queryStr);
        ps.setString(1, customerName);
        ps.setString(2, customerAddress);
        ps.setString(3, postalCode);
        ps.setString(4, customerPhone);
        ps.setTimestamp(5, currentTimeStamp);
        ps.setString(6, DAO_User.getActiveUser().getUserName());
        ps.setInt(7, divisionID);
        ps.setInt(8, customerID);

        return ps.executeUpdate() > 0;
    }
    /**
     * Method that deletes customer record from "customers" table
     * @param customerID of the customer to delete
     * @return boolean true if successfully deleted
     * @throws SQLException SQLException
     */
    public static boolean deleteCustomer(int customerID) throws SQLException {
        //removing appointments first due to foreign key restrictions
        if (DAO_Appointment.deleteByCustomerID(customerID)) {
            System.out.println("Appointment removed.");
        }
        //removing customer
        String queryStr = "DELETE FROM customers WHERE Customer_ID = ?";
        PreparedStatement ps = c.prepareStatement(queryStr);
        ps.setInt(1, customerID);
        return ps.executeUpdate() > 0;
    }
}
