package Controller;

import Model.Appointment;
import Utility.AppointmentDB;
import Utility.DBConnection;
import Utility.UserDB;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * This class creates the main login view. The class verifies the user exists in the DB and logs attempts to login_activity.txt via the UserDB class.
 */

public class Login implements Initializable {

    private String loginError;
    private String enterCorrectUorP;
    private String errorHeader;

    @FXML
    private Button exitButton;

    @FXML
    private Button loginButton;

    @FXML
    private Label locationLabel;

    @FXML
    private Label detectedLabel;

    @FXML
    private Label gcoLabel;

    @FXML
    private Label userIDLabel;

    @FXML
    private Label passwordLabel;

    @FXML
    private TextField userIDTextField;

    @FXML
    private PasswordField passwordField;

    /**
     * This method take the username input and auto-fills the "created by" and "last updated by" fields for the
     * addAppointment and addCustomer views.
     * @param event
     */

    @FXML
    void onActionHandleLogin(ActionEvent event) throws IOException, SQLException {

        //Gets the username and password entered
        String username = userIDTextField.getText();
        String password = passwordField.getText();

        //verifies the user against database users
        boolean verifiedUser = UserDB.login(username, password);
        if (verifiedUser) {
            boolean isFound = true;
            AppointmentDB.getAllAppointments();
            //Runs a for each lambda loop through the observable list of all the appointments and returns an alert
            for (Appointment appointment : AppointmentDB.allAppointments) {
                LocalDateTime within15Minutes = LocalDateTime.now();
                isFound = true;
                //Compares the system time to the appointment start times and to see if the start is within 15 minutes of all start times
                if (within15Minutes.isAfter(appointment.getStart().minusMinutes(15)) && within15Minutes.isBefore(appointment.getStart())) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("UPCOMING APPOINTMENT");
                    alert.setContentText("Appointment: " + appointment.getAppointmentID() + " starts at " + appointment.getStart());
                    alert.showAndWait();
                    break;
                } else {
                    isFound = false;
                }
            }
            if (!isFound) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("No upcoming appointments!");
                alert.setContentText("You have no upcoming appointments in the next 15 minutes");
                alert.showAndWait();
            }

            Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            Object scene = FXMLLoader.load(getClass().getResource("/View/MainMenu.fxml"));
            stage.setScene(new Scene((Parent) scene));
            stage.show();
        }
          else if (true || userIDTextField.getText().isEmpty() || passwordField.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle(loginError);
            alert.setHeaderText(errorHeader);
            alert.setContentText(enterCorrectUorP);
            alert.showAndWait();
            }

        }

    /**
     * This method exits the application when the exitButton is pushed
     * @param event
     */
    @FXML
    void onActionExitApp(ActionEvent event) throws IOException {
        Button sourceButton = (Button) event.getSource();
        exitButton.setText(sourceButton.getText());
        DBConnection.closeConnection();
        System.exit(0);
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        locationLabel.setText(String.valueOf(Locale.getDefault()));
        try {
            rb = ResourceBundle.getBundle("Resources/Nat", Locale.getDefault());

            if (Locale.getDefault().getLanguage().equals("fr") || Locale.getDefault().getLanguage().equals("en")){
                gcoLabel.setText(rb.getString("GCO"));
                userIDLabel.setText(rb.getString("UserIDLabel"));
                passwordLabel.setText(rb.getString("EnterPasswordLabel"));
                loginButton.setText(rb.getString("Login"));
                exitButton.setText(rb.getString("Exit"));
                loginError = rb.getString("loginError");
                enterCorrectUorP = rb.getString("enterCorrectUorP");
                errorHeader = rb.getString(errorHeader);
                detectedLabel.setText(rb.getString("Location"));
            }

        } catch (Exception e) {
        }
    }
}