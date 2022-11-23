package model;
/**
 * This model class manages the Customer construction and getters.
 * @author Batnomin Terbish
 * */
public class Customer {
    private final int id, divID;
    private final String name, address, postalCode, phone, division, country;
    /**
     * Method to construct Customer
     * @param customerID to set
     * @param name of the customer to set
     * @param address of the customer to set
     * @param postalCode of the customer to set
     * @param phone of the customer to set
     * @param divID of the customer to set
     * @param division name to set
     * @param country name to set
     */
    public Customer(int customerID, String name, String address, String postalCode,
                    String phone, int divID, String division, String country) {
        this.id = customerID;
        this.name = name;
        this.address = address;
        this.postalCode = postalCode;
        this.phone = phone;
        this.divID = divID;
        this.division = division;
        this.country = country;

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
     * Method to get the address
     * @return address
     */
    public String getAddress() {
        return address;
    }
    /**
     * Method to get the postalCode
     * @return postalCode
     */
    public String getPostalCode() {
        return postalCode;
    }
    /**
     * Method to get the phone
     * @return phone
     */
    public String getPhone() {
        return phone;
    }
    /**
     * Method to get the divID
     * @return divID
     */
    public int getDivID() {
        return divID;
    }
    /**
     * Method to get the division
     * @return division
     */
    public String getDivision() {
        return division;
    }
    /**
     * Method to get the country
     * @return country
     */
    public String getCountry() {
        return country;
    }
    /**
     * Method to set the toString
     * @return customer in formatted string
     */
    public String toString() {
        return id + " - " + name;
    }
}
