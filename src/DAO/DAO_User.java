package DAO;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.User;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
/**
 * This abstract class handles "users" table queries.
 * @author Batnomin Terbish
 */
public class DAO_User {
    private static User activeUser;
    private static final Connection c = DBConnection.getConnection();
    /**
     * Method that queries all users from the "users" table.
     * @return list of all users in ObservableList
     * @throws SQLException SQLException
     */
    public static ObservableList<User> queryUsers() throws SQLException {
        String queryStr = "SELECT * FROM users";
        ObservableList<User> allUsers = FXCollections.observableArrayList();
        PreparedStatement ps = c.prepareStatement(queryStr);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            int userID = rs.getInt("User_ID");
            String userName = rs.getString("User_Name");
            String password = rs.getString("Password");
            User newUser = new User(userID, userName, password);
            allUsers.add(newUser);
        }
        return allUsers;
    }
    /**
     * Method that sets the activeUser
     * @param user to set as activeUser
     */
    public static void setActiveUser(User user) {
        activeUser = user;
    }
    /**
     * Method that gets the activeUser
     * @return activeUser
     */
    public static User getActiveUser() {
        return activeUser;
    }
}
