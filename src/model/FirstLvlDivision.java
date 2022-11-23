package model;
/**
 * This model class manages the First Level Division construction and getters.
 * @author Batnomin Terbish
 * */
public class FirstLvlDivision {
    private final int divisionID;
    private final String division;
    private final int countryID;
    /**
     * Method to construct First Level Division
     * @param divisionID to set
     * @param division name to set
     * @param countryID to set
     */
    public FirstLvlDivision(int divisionID, String division, int countryID) {
        this.divisionID = divisionID;
        this.division = division;
        this.countryID = countryID;
    }
    /**
     * Method to get the divisionID
     * @return divisionID
     */
    public int getDivisionID() {
        return divisionID;
    }
    /**
     * Method to get the division
     * @return division
     */
    public String getDivision() {
        return division;
    }
    /**
     * Method to get the countryID
     * @return countryID
     */
    public int getCountryID() {
        return countryID;
    }
    /**
     * Method to set the toString
     * @return firstLevelDivision in formatted string
     */
    public String toString() {
        return divisionID + " - " + division;
    }

}
