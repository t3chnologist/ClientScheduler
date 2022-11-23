package model;
/**
 * This model class manages the Country construction and getters.
 * @author Batnomin Terbish
 * */
public class Country {
    private final int id;
    private final String name;
    /**
     * Method to construct Country
     * @param id of the country to set
     * @param name of the country to set
     */
    public Country(int id, String name) {
        this.id = id;
        this.name = name;
    }
    /**
     * Method to get the id
     * @return id
     */
    public int getId() {
        return id;
    }
    /**
     * Method to get the name
     * @return name
     */
    public String getName() {
        return name;
    }
    /**
     * Method to set the toString
     * @return country in formatted string
     */
    public String toString() {
        return id + " - " + name;
    }
}
