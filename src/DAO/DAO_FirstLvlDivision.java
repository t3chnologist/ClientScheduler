package DAO;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.FirstLvlDivision;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
/**
 * This abstract class handles "first_level_divisions" table queries.
 * @author Batnomin Terbish
 */
public class DAO_FirstLvlDivision {
    private static final Connection c = DBConnection.getConnection();
    /**
     * Method that queries "first_level_divisions" table.
     * @param queryStr to query
     * @return list of first_level_divisions in ObservableList
     * @throws SQLException SQLException
     */
    public static ObservableList<FirstLvlDivision> queryAllDivisions(String queryStr) throws SQLException {
        ObservableList<FirstLvlDivision> allDivisions = FXCollections.observableArrayList();
        PreparedStatement ps = c.prepareStatement(queryStr);
        ResultSet rs = ps.executeQuery();
        while(rs.next()){
            int divID = rs.getInt("Division_ID");
            String divName = rs.getString("Division");
            int countryID = rs.getInt("Country_ID");
            FirstLvlDivision newDivision = new FirstLvlDivision(divID, divName, countryID);
            allDivisions.add(newDivision);
        }
        return allDivisions;
    }
    /**
     * Method that queries all first_level_divisions from the "first_level_divisions" table.
     * @return list of all first_level_divisions in ObservableList
     */
    public static ObservableList<FirstLvlDivision> queryDivisions () throws SQLException {
        return queryAllDivisions("SELECT * FROM first_level_divisions");
    }
}
