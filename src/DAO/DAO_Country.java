package DAO;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Country;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
/**
 * This abstract class handles "countries" table queries.
 * @author Batnomin Terbish
 */
public class DAO_Country {
    private static Connection c = DBConnection.getConnection();
    /**
     * Method that queries all countries from the "countries" table.
     * @return list of all countries in ObservableList
     * @throws SQLException SQLException
     */
    public static ObservableList<Country> queryCountries() throws SQLException {
        String queryStr = "SELECT * FROM countries";
        ObservableList<Country> allCountries = FXCollections.observableArrayList();
        PreparedStatement ps = c.prepareStatement(queryStr);
        ResultSet rs = ps.executeQuery();
        while(rs.next()){
            int countryID = rs.getInt("Country_ID");
            String countryName = rs.getString("Country");
            Country newCountry = new Country(countryID, countryName);
            allCountries.add(newCountry);
        }
        return allCountries;
    }
}
