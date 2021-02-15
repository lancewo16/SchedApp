package Utility;

import Model.Appointment;
import Model.Contact;
import Model.Customer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

/**
 * This class is collects information from the combobox selection and sends it to the Contact Schedules Report
 */

public class ReportsDB {

    private static Contact newContactSchedule;
    private static Customer newCustomerSchedule;


    public static void sendContactSelection(Contact contactSchedule) {

      //sets the contact object using the Contact ID to query the database/
        newContactSchedule = contactSchedule;
    }

    //The contact's appointments will be added to the following list/
    public static ObservableList<Appointment> contactSchedule = FXCollections.observableArrayList();

    /**
     * The following Selects all appointments where the Contact ID = the selection from the Combo Box and adds them to the contact schedule list
     */

    public static ObservableList<Appointment> getContactSchedule() throws SQLException {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        contactSchedule.clear();
        try {
            Connection conn = DBConnection.startConnection();
            ResultSet rb = conn.createStatement().executeQuery("SELECT * FROM appointments WHERE Contact_ID=" + newContactSchedule.getContactID());
            while (rb.next()) {
                contactSchedule.add(new Appointment(
                        rb.getInt("Appointment_ID"),
                        rb.getString("Title"),
                        rb.getString("Description"),
                        rb.getString("Type"),
                        rb.getTimestamp("Start").toInstant().atOffset(ZoneOffset.from(ZonedDateTime.now())).toLocalDateTime(),
                        rb.getTimestamp("End").toInstant().atOffset(ZoneOffset.from(ZonedDateTime.now())).toLocalDateTime(),
                        rb.getInt("Customer_ID")));

            }
            return contactSchedule;
        } catch (SQLException e) {
            java.util.logging.Logger.getLogger(e.toString());
        }
        return null;
    }
    /**
     * This comes from the Customer Schedules Report
     */
    public static void sendCustomerSelection(Customer customerSchedule) {
        //setting the contact object which will use their Contact ID to query the DB
        newCustomerSchedule = customerSchedule;
    }
    /**
     * Appointments for Customer will be added to this list
     */
    public static ObservableList<Appointment> customerSchedule = FXCollections.observableArrayList();

    /**
     * This selects all appointments where the Customer ID = the selection from the Combo Box and they're added to the customer schedule list
     */

    public static ObservableList<Appointment> getCustomerSchedule() throws SQLException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        customerSchedule.clear();
        try {
            Connection conn = DBConnection.startConnection();
            ResultSet rb = conn.createStatement().executeQuery("SELECT * FROM appointments WHERE Customer_ID=" + newCustomerSchedule.getCustomerID());
            while (rb.next()) {
                customerSchedule.add(new Appointment(
                        rb.getInt("Appointment_ID"),
                        rb.getString("Title"),
                        rb.getString("Description"),
                        rb.getString("Type"),
                        rb.getTimestamp("Start").toInstant().atOffset(ZoneOffset.from(ZonedDateTime.now())).toLocalDateTime(),
                        rb.getTimestamp("End").toInstant().atOffset(ZoneOffset.from(ZonedDateTime.now())).toLocalDateTime(),
                        rb.getInt("Customer_ID")));

            }
            return customerSchedule;
        } catch (SQLException e) {
            java.util.logging.Logger.getLogger(e.toString());
        }
        return null;
    }
}

