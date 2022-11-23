package DAO;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
/**
 * This class creates the database connection to "client_schedule" database.
 * @author Batnomin Terbish
 */
public abstract class DBConnection {
    private static final String protocol = "jdbc";
    private static final String vendor = ":mysql:";
    private static final String location = "//localhost/";
    private static final String databaseName = "client_schedule";
    private static final String jdbcUrl = protocol + vendor + location + databaseName + "?connectionTimeZone = SERVER";
    private static final String driver = "com.mysql.cj.jdbc.Driver";
    private static final String userName = "sqlUser";
    public static Connection connection;

    /**
     * Method that opens the database connection using the URL string
     */
    public static void openConnection()
    {
        try {
            Class.forName(driver);
            connection = DriverManager.getConnection(jdbcUrl, userName, "Passw0rd!");
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
    /**
     * Method that gets the active Connection
     * @return the connection
     * */
    public static Connection getConnection() {
        return connection;
    }
    /**
     * Method that closes the Active Connection
     */
    public static void closeConnection() {
        try {
            connection.close();
        }
        catch(SQLException e)
        {
            e.printStackTrace();
        }
    }
}
