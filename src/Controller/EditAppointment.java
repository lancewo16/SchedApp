package Controller;
/**
 * Allows the user to edit an appointment using the AppointmentDB class to modify the DB
 */

import Model.Appointment;
import Model.Contact;
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
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.TimeZone;
import java.util.logging.Logger;

/**
 * This class creates a view where appointments can be edited
 */

public class EditAppointment implements Initializable {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
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
    private TextField aptCreateByTextField;

    @FXML
    private TextField aptLastUpdateByTextField;

    @FXML
    private TextField aptCustomerIDTextField;

    @FXML
    private TextField aptUIDTextField;

    @FXML
    private TextField aptContactIDTextField;

    @FXML
    private Button editAptButton;

    @FXML
    private Button exitButton;

    @FXML
    private TextField aptStartTextField;

    @FXML
    private TextField aptEndTextField;

    @FXML
    private TextField aptCreateDateTextField;

    @FXML
    private TextField aptLastUpdateTextField;

    @FXML
    private ComboBox<Contact> contactNameComboBox;
    ObservableList<Contact> contactList = FXCollections.observableArrayList();

    /**
     * This method converts country cbjects into Strings for country selection
     * @throws SQLException
     */
    public EditAppointment() throws SQLException {

        try {
            Connection conn = DBConnection.startConnection();
            ResultSet rs = conn.createStatement().executeQuery("SELECT * FROM contacts");
            while (rs.next()) {

                contactList.add(new Contact(rs.getInt("Contact_ID"),rs.getString("Contact_Name"),rs.getString("Email")));

            }
        } catch (SQLException ce) {
            Logger.getLogger(ce.toString());
        }
    }

    @FXML
    private void onActionSetContactID(ActionEvent event) throws IOException {

    }

    /**
     * This method sets the fields based on selection from the tableview in the Appointment Main scene
     * @see AppointmentMain#onActionSceneEditApt(ActionEvent)
     * @param modifyAppointment
     */

    @FXML
    public void sendAppointment(Appointment modifyAppointment)
    {
        ObservableList<String> types = Appointment.getTypes();
        String selectedType = null;
        //Setting type
        for (String s : types){
            Appointment.getTypes();
        }
        aptIDTextField.setText(String.valueOf(modifyAppointment.getAppointmentID()));
        aptTitleTextField.setText(modifyAppointment.getTitle());
        aptDescriptionTextField.setText(modifyAppointment.getDescription());
        aptLocationTextField.setText(modifyAppointment.getLocation());
        typeField.setValue(selectedType);
        typeField.setItems(types);
        aptStartTextField.setText(modifyAppointment.getStart().format(formatter));
        aptEndTextField.setText(modifyAppointment.getEnd().format(formatter));
        aptLastUpdateByTextField.setText(modifyAppointment.getLastUpdatedBy());
        aptLastUpdateTextField.setText(modifyAppointment.getLastUpdate().format(formatter));
        aptCreateByTextField.setText(modifyAppointment.getCreatedBy());
        aptCreateDateTextField.setText(modifyAppointment.getCreateDate().format(formatter));
        aptCustomerIDTextField.setText(String.valueOf(modifyAppointment.getCustomerID()));
        aptUIDTextField.setText(String.valueOf(modifyAppointment.getUserID()));
        aptContactIDTextField.setText(String.valueOf(modifyAppointment.getContactID()));
        int comboBoxPreset = modifyAppointment.getContactID();
        Contact c = new Contact(comboBoxPreset);
        contactNameComboBox.setValue(c);
    }

    @FXML
    void onActionSceneMain(ActionEvent event) throws IOException {
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        Object scene = FXMLLoader.load(getClass().getResource("/View/AppointmentMain.fxml"));
        stage.setScene(new Scene((Parent) scene));
        stage.show();
    }

    /**
     * This method gets the contact object and sets the Id text field based on name selection in combobox
     * @param event
     */
    @FXML
    void onActionFillContactID(ActionEvent event) {
        contactNameComboBox.setItems(contactList);
        Contact c = contactNameComboBox.getSelectionModel().getSelectedItem();
        aptContactIDTextField.setText(String.valueOf(c.getContactID()));
    }


    /**
     * This method sends the edited appointment to the database
     * @see AppointmentDB#editAppointment(Integer, String, String, String, String, LocalDateTime, LocalDateTime, LocalDateTime, String, LocalDateTime, String, Integer, Integer, Integer)
     * @param event
     * @return
     * @throws SQLException
     * @throws IOException
     */
    @FXML
    void onActionEditApt(ActionEvent event) throws SQLException, IOException {

        try {
            //verifies that all fields are filled, otherwise gives an alert to enter fields

            if (aptTitleTextField.getText().isEmpty() ||
                    aptDescriptionTextField.getText().isEmpty() ||
                    aptLocationTextField.getText().isEmpty() ||
                    typeField.getSelectionModel().getSelectedItem().isEmpty() ||
                    aptStartTextField.getText().isEmpty() ||
                    aptEndTextField.getText().isEmpty() ||
                    aptCreateDateTextField.getText().isEmpty() ||
                    aptCreateByTextField.getText().isEmpty() ||
                    aptLastUpdateTextField.getText().isEmpty() ||
                    aptLastUpdateByTextField.getText().isEmpty() ||
                    aptCustomerIDTextField.getText().isEmpty() ||
                    aptCustomerIDTextField.getText().isEmpty() ||
                    aptContactIDTextField.getText().isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("ERROR");
                alert.setContentText("Please ensure all fields are filled");
                alert.showAndWait();
                return;
            }
                TimeZone MST = TimeZone.getTimeZone("America/Boise");
                Long offsetToEST = (long) (MST.getOffset(new Date().getTime()) / 1000 / 60);
                LocalDateTime startTime = LocalDateTime.parse(aptStartTextField.getText(), formatter).minus(Duration.ofSeconds(offsetToUTC));
                //Sets the start time to EST
                startTime = startTime.plus(Duration.ofMinutes(offsetToEST));
                //Gets the time entered (user local) and set it to utc
                LocalDateTime endTime = LocalDateTime.parse(aptEndTextField.getText(), formatter).minus(Duration.ofSeconds(offsetToUTC));
                //Sets the end time to EST
                endTime = endTime.plus(Duration.ofMinutes(offsetToEST));

                //Compares the startTime and endTime between business hours of 8-22
                LocalTime businessHoursStart = LocalTime.of(8, 00);
                LocalTime businessHoursEnd = LocalTime.of(22, 00);

                //Checks if date time falls between other scheduled appointments
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

                //Checks if time of start and end are within the business hours
                if (startTime.toLocalTime().isBefore(businessHoursStart) || endTime.toLocalTime().isAfter(businessHoursEnd)) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("ERROR");
                    alert.setContentText("Appointment time entered must be between the business hours of 0800 MST and 1000 MST");
                    alert.showAndWait();
                    return;

                } else {
                    String description = aptDescriptionTextField.getText();
                    int appointmentID = Integer.parseInt(aptIDTextField.getText());
                    String title = aptTitleTextField.getText();
                    String location = aptLocationTextField.getText();
                    String type = typeField.getValue();
                    int customerID = Integer.parseInt(aptCustomerIDTextField.getText());
                    LocalDateTime end = LocalDateTime.parse(aptEndTextField.getText(), formatter).minus(Duration.ofSeconds(offsetToUTC));
                    LocalDateTime start = LocalDateTime.parse(aptStartTextField.getText(), formatter).minus(Duration.ofSeconds(offsetToUTC));
                    LocalDateTime createDate = LocalDateTime.parse(aptCreateDateTextField.getText(), formatter).minus(Duration.ofSeconds(offsetToUTC));
                    String createdBy = aptCreateByTextField.getText();
                    LocalDateTime lastUpdate = LocalDateTime.parse(aptLastUpdateTextField.getText(), formatter).minus(Duration.ofSeconds(offsetToUTC));
                    String lastUpdatedBy = aptLastUpdateByTextField.getText();
                    int contactID = Integer.parseInt(aptContactIDTextField.getText());
                    int userID = Integer.parseInt(aptUIDTextField.getText());
                    AppointmentDB.editAppointment(appointmentID, title, description, location, type, start, end, createDate, createdBy, lastUpdate, lastUpdatedBy, customerID, userID, contactID);

                    FXMLLoader loader = new FXMLLoader();
                    loader.setLocation(getClass().getResource("/View/AppointmentMain.fxml"));
                    Parent parent = loader.load();

                    Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
                    Parent scene = loader.getRoot();
                    stage.setScene(new Scene(scene));
                    stage.show();
                }
        }
        /**
         * @exception DateTimeParseException e if date time fields are not formatted correctly this is caught to alert the user to modify them correctly
         */ catch (DateTimeParseException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR");
            alert.setContentText("Date and time fields must be in format: YYYY-MM-DD HH:MM");
            alert.showAndWait();
            return;
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        contactNameComboBox.setItems(contactList);

    }
}


