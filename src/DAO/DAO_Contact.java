package DAO;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Contact;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
/**
 * This abstract class handles "contacts" table queries.
 * @author Batnomin Terbish
 */
public class DAO_Contact {
    private static Connection c = DBConnection.getConnection();
    /**
     * Method that queries all contacts from the "contacts" table.
     * @return list of all contacts in ObservableList
     * @throws SQLException SQLException
     */
    public static ObservableList<Contact> queryContacts() throws SQLException {
        ObservableList<Contact> allContacts = FXCollections.observableArrayList();
        String queryStr = "SELECT * FROM contacts";
        PreparedStatement ps = c.prepareStatement(queryStr);
        ResultSet rs = ps.executeQuery();
        while(rs.next()){
            int id = rs.getInt("Contact_ID");
            String name = rs.getString("Contact_Name");
            String email = rs.getString("Email");
            Contact newContact = new Contact(id, name, email);
            allContacts.add(newContact);
        }
        return allContacts;
    }
}
