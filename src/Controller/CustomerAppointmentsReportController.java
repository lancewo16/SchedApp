package Controller;

import Utility.DBConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import Model.Appointment;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

/**
 * Controller for the Reports screen. Includes methods
 * for button clicks and displaying reports in TextArea.
 */
public class CustomerAppointmentsReportController implements Initializable {

    @FXML
    private TextArea totalAptsMonth;

    @FXML
    public TextArea totalAptsType;

    @FXML
    private Button exitButton;

    @FXML
    private Button backToMain;


    /**
     * Method called with Reports screen is opened. Sets the reports in the appropriate
     * TextArea's for displaying the report data.
     *
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        try {
            totalAptsMonth.setText(aptsByMonth());
            totalAptsType.setText(aptsByType());

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public String aptsByType() throws SQLException {
        ObservableList<String> types = Appointment.getTypes();
        ObservableList<Appointment> aptsByType;
        String appointmentString = "Number of Appointments by Type: \n";

        for (String s: types){
            aptsByType = Appointment.appointmentByType(s);
            appointmentString = appointmentString + "\n" + s + ": \n" + aptsByType.size() + "\n";

        }
        return appointmentString;
    }

    /**
     * Creates a string list of number of appointments in each month of year.
     *
     * @return String of report data to display
     * @throws SQLException
     */
    public String aptsByMonth() throws SQLException {
        ObservableList<Appointment> appointmentOfMonth;
        String appointmentString = "Number of Appointments by Month: \n";

        for (int i = 1; i <= 12; i++) {
            appointmentOfMonth = Appointment.appointmentByMonth(i);
            appointmentString = appointmentString + "\n" + i + ": " + appointmentOfMonth.size() + "\n";
        }
        return appointmentString;
    }

    @FXML
    void onActionExitToMain(ActionEvent event) throws IOException {
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        Object scene = FXMLLoader.load(getClass().getResource("/View/MainMenu.fxml"));
        stage.setScene(new Scene((Parent) scene));
        stage.show();
    }
    @FXML
    void onActionExitApp(ActionEvent event) {
        Button sourceButton = (Button) event.getSource();
        exitButton.setText(sourceButton.getText());
        DBConnection.closeConnection();
        System.exit(0);
    }
}