package model;
/**
 * This model class manages the Contact construction and getters.
 * @author Batnomin Terbish
 * */
public class Contact {
    private final int id;
    private final String name, email;
    /**
     * Method to construct Contact
     * @param id to set
     * @param name to set
     * @param email to set
     * */
    public Contact(int id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
    }
    /**
     * Method to get the id
     * @return id
     * */
    public int getId() {
        return id;
    }
    /**
     * Method to get the name
     * @return name
     * */

    public String getName() {
        return name;
    }
    /**
     * Method to get the email
     * @return email
     */
    public String getEmail() {
        return email;
    }
    /**
     * Method to set the toString
     * @return contact in formatted string
     */

    public String toString() {
        return id + " - " + name;
    }
}
