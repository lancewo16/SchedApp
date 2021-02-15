
package Controller;

import Utility.DBConnection;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * This class gives user access to customers, appointments, and reports views
 */
public class MainMenu implements Initializable {

    @FXML
    private Button customerAptButton;

    @FXML
    private Button contactSchedulesButton;

    @FXML
    private Button customerSchedulesButton;

    @FXML
    private Button customerButton;

    @FXML
    private Button aptButton;

    @FXML
    private Button logoutButton;

    @FXML
    private Button exitButton;

    @FXML
    void onActionExitApp(ActionEvent event) {
        Button sourceButton = (Button) event.getSource();
        exitButton.setText(sourceButton.getText());
        DBConnection.closeConnection();
        System.exit(0);
    }

    /**
     * This method gives the user access to the appointment table
     * @param event
     * @throws IOException
     */
    @FXML
    void onActionSceneAptMain(ActionEvent event) throws IOException {
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        Object scene = FXMLLoader.load(getClass().getResource("/View/AppointmentMain.fxml"));
        stage.setScene(new Scene((Parent) scene));
        stage.show();
    }
    /**
     * This method gives the user access to the customer table
     * @param event
     * @throws IOException
     */
    @FXML
    void onActionSceneCustomerMain(ActionEvent event) throws IOException {
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        Object scene = FXMLLoader.load(getClass().getResource("/View/CustomerMain.fxml"));
        stage.setScene(new Scene((Parent) scene));
        stage.show();
    }

    /**
     * This method gives the user access to the CustomerSchedules report
     * @param event
     * @throws IOException
     */
    @FXML
    void onActionSceneCustSchedReport(ActionEvent event) throws IOException {
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        Object scene = FXMLLoader.load(getClass().getResource("/View/CustomerSchedulesReport.fxml"));
        stage.setScene(new Scene((Parent) scene));
        stage.show();
    }
    /**
     * This method gives the user access to the Contact Schedules report
     * @param event
     * @throws IOException
     */
    @FXML
    void onActionSceneContSchedReport(ActionEvent event) throws IOException {
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        Object scene = FXMLLoader.load(getClass().getResource("/View/ContactSchedulesReport.fxml"));
        stage.setScene(new Scene((Parent) scene));
        stage.show();
    }

    /**
     * This method gives the user access to the Customer AppointmentSchedules report
     * @param event
     * @throws IOException
     */
    @FXML
    void onActionSceneCustAptReport(ActionEvent event) throws IOException {
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        Object scene = FXMLLoader.load(getClass().getResource("/View/CustomerAppointmentsReport.fxml"));
        stage.setScene(new Scene((Parent) scene));
        stage.show();
    }

    /**
     * This method gives the user access to the Login Screen
     * @param event
     * @throws IOException
     */
    @FXML
    void onActionSceneLogout(ActionEvent event) throws IOException {
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        Object scene = FXMLLoader.load(getClass().getResource("/View/Login.fxml"));
        stage.setScene(new Scene((Parent) scene));
        stage.show();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {


    }
}





