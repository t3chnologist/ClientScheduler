package model;
/**
 * This model class manages the User construction and getters.
 * @author Batnomin Terbish
 * */
public class User {
    private final int userID;
    private final String userName;
    private final String password;
    /**
     * Method to construct User
     * @param userID to set
     * @param userName to set
     * @param password to set
     */
    public User(int userID, String userName, String password) {
        this.userID = userID;
        this.userName = userName;
        this.password = password;
    }
    /**
     * Method to get the userID
     * @return userID
     */
    public int getUserID() {
        return userID;
    }
    /**
     * Method to get the userName
     * @return userName
     */
    public String getUserName() {
        return userName;
    }
    /**
     * Method to get the password
     * @return password
     */
    public String getPassword() {
        return password;
    }
    /**
     * Method to set the toString
     * @return user in formatted string
     */
    public String toString() {
        return userID + " - " + userName;
    }
}
