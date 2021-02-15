package Model;

import Utility.AppointmentDB;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * This class is used for Appointment objects
 */
public class Appointment {
    
    private int appointmentID;
    private String title;
    private String description;
    private String location;
    private String type;
    private LocalDateTime start;
    private LocalDateTime end;
    private LocalDateTime createDate;
    private String createdBy;
    private LocalDateTime lastUpdate;
    private String lastUpdatedBy;
    private int customerID;
    private int userID;
    private int contactID;

    public Appointment(int appointmentID, String title, String description, String location, String type, LocalDateTime start, LocalDateTime end, LocalDateTime createDate, String createdBy, LocalDateTime lastUpdate, String lastUpdatedBy, int customerID, int userID, int contactID) {
        this.appointmentID=appointmentID;
        this.title=title;
        this.description=description;
        this.location=location;
        this.type=type;
        this.start=start;
        this.end=end;
        this.createDate=createDate;
        this.createdBy=createdBy;
        this.lastUpdate=lastUpdate;
        this.lastUpdatedBy=lastUpdatedBy;
        this.customerID=customerID;
        this.userID=userID;
        this.contactID=contactID;
    }

    /**
     * The following is the Constructor For Appointment Schedules (weekly and monthly)
     */
    public Appointment(int appointmentID, String title, String description, String location, String type, LocalDateTime start, LocalDateTime end, int customerID, int contactID) {
        this.appointmentID=appointmentID;
        this.title=title;
        this.description=description;
        this.location=location;
        this.type=type;
        this.start=start;
        this.end=end;
        this.customerID=customerID;
        this.contactID=contactID;
    }

    /**
     * The following is the Constructor for the Contact schedule and Customer Schedule Reports scenes
     * @param appointmentID
     * @param title
     * @param description
     * @param type
     * @param start
     * @param end
     * @param customerID
     */
    public Appointment(int appointmentID, String title, String description, String type, LocalDateTime start, LocalDateTime end, int customerID) {
        this.appointmentID=appointmentID;
        this.title=title;
        this.description=description;
        this.type=type;
        this.start=start;
        this.end=end;
        this.customerID=customerID;

    }

    public int getAppointmentID() {
        return appointmentID;
    }

    public void setAppointmentID(int appointmentID) {
        this.appointmentID = appointmentID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public LocalDateTime getStart() {
      return start;
    }

    public void setStart(LocalDateTime start) {
        this.start = start;
    }

    public LocalDateTime getEnd() {
        return end;
    }

    public void setEnd(LocalDateTime end) {
        this.end = end;
    }

    public LocalDateTime getCreateDate() {
        return createDate;
    }

    public void setCreateDate(LocalDateTime createDate) {
        this.createDate = createDate;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public LocalDateTime getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(LocalDateTime lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public String getLastUpdatedBy() {
        return lastUpdatedBy;
    }

    public void setLastUpdatedBy(String lastUpdatedBy) {
        this.lastUpdatedBy = lastUpdatedBy;
    }

    public int getCustomerID() {
        return customerID;
    }

    public void setCustomerID(int customerID) {
        this.customerID = customerID;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public int getContactID() {
        return contactID;
    }

    public void setContactID(int contactID) {
        this.contactID = contactID;
    }

    public static ObservableList<Appointment> appointmentByMonth(int month) throws SQLException {
        ObservableList<Appointment> allAppointments = AppointmentDB.getAllAppointments();
        ObservableList<Appointment> appointmentsOfMonth = FXCollections.observableArrayList();

        for (Appointment a : allAppointments){
            LocalDateTime start = a.getStart();
            LocalDate date = start.toLocalDate();

            if (date.getMonth().getValue() == month){
                appointmentsOfMonth.add(a);
            }
        }
        return appointmentsOfMonth;
    }

    /**
     * Populates an ObservableList of String with all possible Type options
     * used to populate a combobox in the application.
     *
     * @return ObservableList of all possible Types
     */
    public static ObservableList<String> getTypes(){
        ObservableList<String> types = FXCollections.observableArrayList();

        types.add("Planning Session");
        types.add("De-Briefing");
        types.add("Sales");
        types.add("Marketing");
        types.add("Miscellaneous");

        return types;
    }

    public static ObservableList<Appointment> appointmentByType(String type) throws SQLException {

        ObservableList<Appointment> appointmentsOfType = AppointmentDB.getAllAppointments().filtered(appointment -> {
            if (appointment.getType().equals(type))
                return true;
            return false;
        });
        return appointmentsOfType;
    }
}
