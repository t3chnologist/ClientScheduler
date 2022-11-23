package model;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
/**
 * This model class manages the Appointment construction and getters.
 * @author Batnomin Terbish
 * */
public class Appointment {
    private final int appointmentID, contactID, customerID, userID, countryID;
    private final String title, description, location, type;
    private final Timestamp start, end;
    private final LocalTime startTime, endTime;
    private final LocalDate appointmentDate;
    private final String customerName, contactName;
    /**
     * Method to construct Appointment
     * @param appointmentID to set
     * @param contactID to set
     * @param customerID to set
     * @param userID to set
     * @param countryID to set
     * @param title to set
     * @param description to set
     * @param location to set
     * @param type to set
     * @param start time to set
     * @param end time to set
     * @param customerName to set
     * @param contactName to set
     */
    public Appointment(int appointmentID, int contactID, int customerID, int userID, int countryID,
                       String title, String description, String location, String type,
                       Timestamp start, Timestamp end, String customerName, String contactName) {
        this.appointmentID = appointmentID;
        this.contactID = contactID;
        this.customerID = customerID;
        this.userID = userID;
        this.countryID = countryID;
        this.title = title;
        this.description = description;
        this.location = location;
        this.type = type;
        this.appointmentDate = start.toLocalDateTime().toLocalDate();
        this.start = start;
        this.startTime = start.toLocalDateTime().toLocalTime();
        this.end = end;
        this.endTime = end.toLocalDateTime().toLocalTime();
        this.customerName = customerName;
        this.contactName = contactName;
    }
    /**
     * Method to get the appointmentID
     * @return appointmentID
     */
    public int getAppointmentID() {
        return appointmentID;
    }
    /**
     * Method to get the contactID
     * @return contactID
     */
    public int getContactID() {
        return contactID;
    }
    /**
     * Method to get the customerID
     * @return customerID
     */
    public int getCustomerID() {
        return customerID;
    }
    /**
     * Method to get the userID
     * @return userID
     */
    public int getUserID() {
        return userID;
    }
    /**
     * Method to get the title
     * @return title
     */
    public String getTitle() {
        return title;
    }
    /**
     * Method to get the description
     * @return description
     */
    public String getDescription() {
        return description;
    }
    /**
     * Method to get the location
     * @return location
     */
    public String getLocation() {
        return location;
    }
    /**
     * Method to get the type
     * @return type
     */
    public String getType() {
        return type;
    }
    /**
     * Method to get the formatted getAppointmentDate
     * @return formatted getAppointmentDate
     */
    public String getAppointmentDate () {
        return appointmentDate.format(DateTimeFormatter.ofPattern("MMM-dd-yyyy"));
    }
    /**
     * Method to get the start Timestamp
     * @return start Timestamp
     */
    public Timestamp getStart() {
        return start;
    }
    /**
     * Method to get the StartLDT
     * @return StartLDT
     */
    public LocalDateTime getStartLDT() {
        return start.toLocalDateTime();
    }
    /**
     * Method to get the formatted startTime
     * @return formatted startTime
     */
    public String getStartTime() {
        return startTime.format(DateTimeFormatter.ofPattern("HH:mm"));
    }
    /**
     * Method to get the end Timestamp
     * @return end Timestamp
     */
    public Timestamp getEnd() {
        return end;
    }
    /**
     * Method to get the end in LDT
     * @return end in LDT
     */
    public LocalDateTime getEndLDT() {
        return end.toLocalDateTime();
    }
    /**
     * Method to get the formatted endTime
     * @return formatted endTime
     */
    public String getEndTime() {
        return endTime.format(DateTimeFormatter.ofPattern("HH:mm"));
    }
    /**
     * Method to get the customerName
     * @return customerName
     */
    public String getCustomerName() {
        return customerName;
    }
    /**
     * Method to get the contactName
     * @return contactName
     */
    public String getContactName() {
        return contactName;
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
     * @return appointment in formatted string
     */
    public String toString() {

        return "ID: " + getAppointmentID() +
                " | Start: " + getStartLDT().format(DateTimeFormatter.ofPattern("MM/dd/yy HH:mm")) +
                " | End: " + getEndLDT().format(DateTimeFormatter.ofPattern("MM/dd/yy HH:mm"));
    }
}
