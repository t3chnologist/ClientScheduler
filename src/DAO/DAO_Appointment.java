package DAO;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Appointment;
import java.sql.*;
/**
 * This abstract class handles "appointments" table queries.
 * @author Batnomin Terbish
 */
public abstract class DAO_Appointment {
    private static final Connection c = DBConnection.getConnection();
    private static final Timestamp currentTimeStamp = new Timestamp(System.currentTimeMillis());
    /**
     * Method that queries all appointments from the "appointments" table.
     * Inner joined with customers, contacts, first_level_divisions, and countries tables.
     * @return list of all appointments in ObservableList
     * @throws SQLException SQLException
     */
    public static ObservableList<Appointment> queryAppointments() throws SQLException {
        ObservableList<Appointment> allAppointments = FXCollections.observableArrayList();
        String queryStr = "SELECT appointments.Appointment_ID, appointments.Description, \n" +
                "contacts.Contact_ID, customers.Customer_ID, first_level_divisions.Division_ID, \n" +
                "countries.Country_ID, countries.Country, appointments.User_ID, appointments.Title, \n" +
                "appointments.Location, appointments.Type, appointments.Start, appointments.End, \n" +
                "customers.Customer_Name, contacts.Contact_Name FROM appointments\n" +
                "INNER JOIN customers ON appointments.Customer_ID = customers.Customer_ID\n" +
                "INNER JOIN contacts ON appointments.Contact_ID = contacts.Contact_ID\n" +
                "INNER JOIN first_level_divisions ON customers.Division_ID = first_level_divisions.Division_ID\n" +
                "INNER JOIN countries ON first_level_divisions.Country_ID = countries.Country_ID";

        PreparedStatement ps = c.prepareStatement(queryStr);
        ResultSet rs = ps.executeQuery();
        while(rs.next()){
            int aptID = rs.getInt("Appointment_ID");
            int contactID = rs.getInt("Contact_ID");
            int customerID = rs.getInt("Customer_ID");
            int userID = rs.getInt("User_ID");
            int countryID = rs.getInt("Country_ID");
            String title = rs.getString("Title");
            String description = rs.getString("Description");
            String location = rs.getString("Location");
            String type = rs.getString("Type");
            Timestamp start = rs.getTimestamp("Start");
            Timestamp end = rs.getTimestamp("End");
            String customerName = rs.getString("Customer_Name");
            String contactName = rs.getString("Contact_Name");

            Appointment newApt = new Appointment(aptID, contactID, customerID, userID, countryID,
                    title, description, location, type, start, end, customerName, contactName);
            allAppointments.add(newApt);
        }
        return allAppointments;
    }
    /**
     * Method that deletes appointment record by appointmentID from "appointments" table
     * @param appointmentID of the appointment to delete
     * @return boolean true if successfully deleted
     * @throws SQLException SQLException
     */
    public static boolean deleteByAppointmentID(int appointmentID) throws SQLException {
        String queryStr = "DELETE FROM appointments WHERE Appointment_ID = ?";
        PreparedStatement ps = c.prepareStatement(queryStr);
        ps.setInt(1, appointmentID);
        return ps.executeUpdate() > 0;
    }

    /**
     * Method that deletes appointment record by appointmentID from "appointments" table
     * @param customerID of the appointment to delete
     * @return boolean true if successfully deleted
     * @throws SQLException SQLException
     */
    public static boolean deleteByCustomerID(int customerID) throws SQLException {
        String queryStr = "DELETE FROM appointments WHERE Customer_ID = ?";
        PreparedStatement ps = c.prepareStatement(queryStr);
        ps.setInt(1, customerID);
        return ps.executeUpdate() > 0;
    }
    /**
     * Method that adds new appointment record to "appointments" table.
     * @param title to add
     * @param description to add
     * @param location to add
     * @param type to add
     * @param contactID to add
     * @param customerID to add
     * @param userID to add
     * @param start time to add
     * @param end time to add
     * @return boolean true if successfully added
     * @throws SQLException SQLException
     */
    public static boolean addNewAppointment(String title, String description, String location,
                                            String type, int contactID, int customerID,
                                            int userID, Timestamp start, Timestamp end) throws SQLException {

        String queryStr = "INSERT INTO appointments (Title, Description, Location, Type, Start, End, Create_Date, " +
                "Created_By, Last_Update, Last_Updated_By, Customer_ID, User_ID, Contact_ID)" +
                "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        PreparedStatement ps = c.prepareStatement(queryStr);

        ps.setString(1, title);
        ps.setString(2, description);
        ps.setString(3, location);
        ps.setString(4, type);
        ps.setTimestamp(5, start);
        ps.setTimestamp(6, end);
        ps.setTimestamp(7, currentTimeStamp);
        ps.setString(8, DAO_User.getActiveUser().getUserName());
        ps.setTimestamp(9, currentTimeStamp);
        ps.setString(10, DAO_User.getActiveUser().getUserName());
        ps.setInt(11, customerID);
        ps.setInt(12, userID);
        ps.setInt(13, contactID);

        return ps.executeUpdate() > 0;
    }
    /**
     * Method that updates appointment record to "appointments" table.
     * @param appointmentID to update
     * @param title to update
     * @param description to update
     * @param location to update
     * @param type to update
     * @param contactID to update
     * @param customerID to update
     * @param userID to update
     * @param start time to update
     * @param end time to update
     * @return boolean true if successfully updated
     * @throws SQLException SQLException
     */
    public static boolean updateAppointment(int appointmentID, String title, String description,
                                            String location, String type, int contactID, int customerID,
                                            int userID, Timestamp start, Timestamp end) throws SQLException {
        String queryStr = "UPDATE appointments SET Title = ?, Description = ?, Location = ?, Type = ?, " +
                "Start = ?, End = ?, Last_Update = ?, Last_Updated_By = ?, Customer_ID = ?, " +
                "User_ID = ?, Contact_ID = ? WHERE Appointment_ID = ?";

        PreparedStatement ps = c.prepareStatement(queryStr);

        ps.setString(1, title);
        ps.setString(2, description);
        ps.setString(3, location);
        ps.setString(4, type);
        ps.setTimestamp(5, start);
        ps.setTimestamp(6, end);
        ps.setTimestamp(7, currentTimeStamp);
        ps.setString(8, DAO_User.getActiveUser().getUserName());
        ps.setInt(9, customerID);
        ps.setInt(10, userID);
        ps.setInt(11, contactID);
        ps.setInt(12, appointmentID);

        return ps.executeUpdate() > 0;
    }
}