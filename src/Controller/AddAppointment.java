package Controller;

import Model.Appointment;
import Model.Contact;
import Model.User;
import Utility.AppointmentDB;
import Utility.DBConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.TimeZone;
import java.util.logging.Logger;

import static java.lang.Integer.valueOf;

/**
 * This class creates the controller for adding a new appointment to the database
 */
public class   AddAppointment implements Initializable {

    //Here is a formatter for configuring the DTG to be set properly and getters.
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
    DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    // Lastly, offsetToUTC is used to get the time difference between UTC and the user's operating system time zone
    Long offsetToUTC = (long) (ZonedDateTime.now().getOffset()).getTotalSeconds();

    @FXML
    private TextField aptIDTextField;

    @FXML
    private TextField aptTitleTextField;

    @FXML
    private TextField aptDescriptionTextField;

    @FXML
    private TextField aptLocationTextField;

    @FXML
    private ComboBox<String> typeField;

    @FXML
    private TextField aptCreatedByTextField;

    @FXML
    private TextField aptLastUpdatedTextField;

    @FXML
    private TextField aptCustomerIDTextField;

    @FXML
    private TextField aptUIDTextField;

    @FXML
    private TextField aptContactIDTextField;

    @FXML
    private TextField aptStartTextField;

    @FXML
    private TextField aptEndTextField;

    @FXML
    private TextField aptCreateDateTextField;

    @FXML
    private TextField aptLastUpdateTextField;

    @FXML
    private ComboBox<Contact> contactName;

    @FXML
    private Button addAptButton;

    @FXML
    private Button exitButton;

    /**
     * This method sets the contact ID field for an appointment based on the selection of the contact list combobox
     * @param event Sets the Contact ID
     * @throws IOException
     */

    @FXML
    private void onActionSetContactID(ActionEvent event) throws IOException {
        if (contactName.getSelectionModel().isEmpty()) {
        }
        else {
            Contact c = contactName.getSelectionModel().getSelectedItem();
            aptContactIDTextField.setText(String.valueOf(c.getContactID()));
        }
    }

    @FXML
    void onActionExitToMain(ActionEvent event) throws IOException {
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        Object scene = FXMLLoader.load(getClass().getResource("/View/AppointmentMain.fxml"));
        stage.setScene(new Scene((Parent) scene));
        stage.show();
    }


    ObservableList<Contact> contactList = FXCollections.observableArrayList();

    /**
     * This method checks the user's local system time and converts to UTC for storage in the database
     * @param event Add appointments
     * @see AppointmentDB#addAppointment(Integer, String, String, String, String, LocalDateTime, LocalDateTime, LocalDateTime, String, LocalDateTime, String, Integer, Integer, Integer)
     * @return Returns converted time
     * @throws IOException
     * @throws SQLException
     */
    @FXML
    void onActionAddApt(ActionEvent event) throws IOException, SQLException {
            try {
                //verifies that all fields are filled, otherwise gives an alert to enter fields

                if (aptTitleTextField.getText().isEmpty() ||
                        aptDescriptionTextField.getText().isEmpty() ||
                        aptLocationTextField.getText().isEmpty() ||
                        typeField.getValue().isEmpty() ||
                        aptStartTextField.getText().isEmpty() ||
                        aptEndTextField.getText().isEmpty() ||
                        aptCustomerIDTextField.getText().isEmpty() ||
                        aptCustomerIDTextField.getText().isEmpty() ||
                        aptContactIDTextField.getText().isEmpty()) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("ERROR");
                    alert.setContentText("Please ensure all fields are filled");
                    alert.showAndWait();
                }
                //gets the users TimeZone offsetToUTC to EST
                TimeZone MST = TimeZone.getTimeZone("America/Boise");
                Long offsetToEST = (long) (MST.getOffset(new Date().getTime()) / 1000 / 60);
                Integer appointmentID = valueOf(aptIDTextField.getText());
                String title = aptTitleTextField.getText();
                String description = aptDescriptionTextField.getText();
                String location = aptLocationTextField.getText();
                String type = typeField.getValue();

                LocalDateTime start = LocalDateTime.parse(aptStartTextField.getText(), formatter).minus(Duration.ofSeconds(offsetToUTC));
                LocalDateTime end = LocalDateTime.parse(aptEndTextField.getText(), formatter).minus(Duration.ofSeconds(offsetToUTC));
                LocalDateTime createDate = LocalDateTime.parse(aptCreateDateTextField.getText(), formatter).minus(Duration.ofSeconds(offsetToUTC));
                String createdBy = aptCreatedByTextField.getText();
                LocalDateTime lastUpdate = LocalDateTime.parse(aptLastUpdateTextField.getText(), formatter).minus(Duration.ofSeconds(offsetToUTC));
                String lastUpdatedBy = aptLastUpdatedTextField.getText();
                Integer customerID = valueOf(aptCustomerIDTextField.getText());
                Integer userID = valueOf(aptUIDTextField.getText());
                Integer contactID = valueOf(aptContactIDTextField.getText());

                //Compares the local time to Business hours, convert text field to z, sets business hours to z time, and
                //lastly, gets the time entered (user local) and sets it to utc
                LocalDateTime startTime = LocalDateTime.parse(aptStartTextField.getText(), formatter).minus(Duration.ofSeconds(offsetToUTC));

                //Sets the appointment start time to EST
                startTime = startTime.plus(Duration.ofMinutes(offsetToEST));

                //Gets the user's local time and sets it to utc
                LocalDateTime endTime = LocalDateTime.parse(aptEndTextField.getText(), formatter).minus(Duration.ofSeconds(offsetToUTC));

                //Sets the appointment end time to EST
                endTime = endTime.plus(Duration.ofMinutes(offsetToEST));

                //Compares the start time and end time between business hours of 8-22
                LocalTime businessHoursStart = LocalTime.of(8, 0);
                LocalTime businessHoursEnd = LocalTime.of(22, 0);

                //Checks to see if date time falls between other scheduled appointment
                LocalDateTime startDateTime = LocalDateTime.parse(aptStartTextField.getText(), formatter);
                LocalDateTime endDateTime = LocalDateTime.parse(aptEndTextField.getText(), formatter);

                //Checks for overlapping appointment times
                for (Appointment appointment : AppointmentDB.allAppointments) {
                    if ((startDateTime.isEqual(appointment.getStart()) || startDateTime.isAfter(appointment.getStart()) && startDateTime.isBefore(appointment.getEnd()))) {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("ERROR");
                        alert.setContentText("Please enter a time for an appointment start and end time that is not already taken");
                        alert.showAndWait();
                        return;
                    }
                }

                //Checks to see if time of start and end time are within the business hours
                if (startTime.toLocalTime().isBefore(businessHoursStart) || endTime.toLocalTime().isAfter(businessHoursEnd)) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("ERROR");
                    alert.setContentText("Appointment time entered must be between the business hours of 0800 MST and 1000 MST");
                    alert.showAndWait();
                    return;

                } else {
                    Appointment appointment = new Appointment(appointmentID, title, description, location, type, start, end, createDate, createdBy, lastUpdate, lastUpdatedBy, customerID, userID, contactID);

                    boolean appointmentCreated = AppointmentDB.addAppointment(appointmentID, title, description, location, type, start, end, createDate, createdBy, lastUpdate, lastUpdatedBy, customerID, userID, contactID);
                    if (appointmentCreated) {
                        AppointmentDB.getAllAppointments().add(appointment);

                        FXMLLoader loader = new FXMLLoader();
                        loader.setLocation(getClass().getResource("/View/AppointmentMain.fxml"));
                        Parent parent = loader.load();

                        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
                        Parent scene = loader.getRoot();
                        stage.setScene(new Scene(scene));
                        stage.show();
                    }
                }
            }
        catch (DateTimeParseException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR");
            alert.setContentText("Date and time fields must be in format: YYYY-MM-DD HH:MM");
            alert.showAndWait();
            return;
        }
    }


    /**
     * This method adds contacts to a list to be used in the combobox
     * @throws SQLException
     */
    public AddAppointment() throws SQLException {

        try {
            Connection conn = DBConnection.startConnection();
            ResultSet rs = conn.createStatement().executeQuery("SELECT * FROM contacts");
            while (rs.next()) {
                /*
                  Creates country objects for Country selection
                 */
                contactList.add(new Contact(rs.getInt("Contact_ID"),rs.getString("Contact_Name"),rs.getString("Email")));
            }
        } catch (SQLException ce) {
            Logger.getLogger(ce.toString());
        }
    }


    /**
     *This method auto-populates the "created by" field with the value of a valid user log in that is stored in the user object.
     */
    @FXML
    public void initialize(URL url, ResourceBundle resourceBundle) {

        aptCreatedByTextField.setText(String.valueOf(User.getUsername()));
        aptLastUpdatedTextField.setText(String.valueOf(User.getUsername()));
        typeField.setItems(Appointment.getTypes());

        try {

            //Establishes the connection to the database
            Connection conn = DBConnection.startConnection();

            //Selects the max Appointment ID from the appointments table and sets its as highest ID
            ResultSet rs = conn.createStatement().executeQuery("SELECT MAX(Appointment_ID) AS highestID FROM appointments");
            while (rs.next()) {

                //Creates a temporary variable for appointment ID
                int tempID = rs.getInt("highestID");

                //Sets the temporary variable appointment ID to  increment by 1
                aptIDTextField.setText(String.valueOf(tempID + 1));
                System.out.println(rs.getInt(tempID));
            }
        } catch (Exception exc) {
            exc.printStackTrace();
        }
    contactName.setItems(contactList);
    }
}


