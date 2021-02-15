package Controller;

import Model.Appointment;
import Model.Customer;
import Utility.CustomerDB;
import Utility.DBConnection;
import Utility.ReportsDB;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ResourceBundle;

/**
 * This class creates a report of the total customer appointments.
 * They are displayed here by month and by type.
 */

public class CustomerSchedulesReport implements Initializable {

    @FXML
    private ComboBox<Customer> customerComboBox;

    @FXML
    private TableView<Appointment> customerAptTableView;

    @FXML
    private TableColumn<Appointment, Integer> appointmentID;

    @FXML
    private TableColumn<Appointment, String> aptTitleTableColumn;

    @FXML
    private TableColumn<Appointment, String> aptTypeTableColumn;

    @FXML
    private TableColumn<Appointment, String> aptDescriptionTableColumn;

    @FXML
    private TableColumn<Appointment, LocalDateTime> aptStartTableColumn;

    @FXML
    private TableColumn<Appointment, LocalDateTime> aptEndTableColumn;

    @FXML
    private TableColumn<Appointment, Integer> aptCustomerIDTableColumn;

    @FXML
    private Button exitButton;

    @FXML
    private Button logoutButton;

    @FXML
    private Button mainButton;

    @FXML
    void onActionSceneMain(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/View/MainMenu.fxml"));
        loader.load();

        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        Parent scene = loader.getRoot();
        stage.setScene(new Scene(scene));
        stage.show();
    }

    /**
     * This method sends the customer object to query the database
     * Appointments are retrieved from the database and the tableview is populated
     * @param event
     * */

    @FXML
    void onActionDisplaySchedule(ActionEvent event) {
        try {

            Customer customerSchedule = customerComboBox.getSelectionModel().getSelectedItem();
            ReportsDB.sendCustomerSelection(customerSchedule);
            customerAptTableView.setItems(ReportsDB.getCustomerSchedule());
            for (Appointment appointment : ReportsDB.customerSchedule) {
                System.out.println(appointment.getStart());
            }
            appointmentID.setCellValueFactory(new PropertyValueFactory<>("appointmentID"));
            aptTitleTableColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
            aptTypeTableColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
            aptDescriptionTableColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
            aptStartTableColumn.setCellValueFactory(new PropertyValueFactory<>("start"));
            aptEndTableColumn.setCellValueFactory(new PropertyValueFactory<>("end"));
            aptCustomerIDTableColumn.setCellValueFactory(new PropertyValueFactory<>("customerID"));
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void onActionExitApp(ActionEvent event) {
        Button sourceButton = (Button) event.getSource();
        exitButton.setText(sourceButton.getText());
        DBConnection.closeConnection();
        System.exit(0);
    }

    @FXML
    void onActionSceneLogout(ActionEvent event) throws IOException {
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        Object scene = FXMLLoader.load(getClass().getResource("/View/Login.fxml"));
        stage.setScene(new Scene((Parent) scene));
        stage.show();
    }

    ObservableList<Customer> customerList = FXCollections.observableArrayList();

    @FXML
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            customerComboBox.setItems(CustomerDB.getAllCustomers());
            for (Customer customer : CustomerDB.allCustomers) {
                System.out.println(customer.getCustomerID());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
