package Controller;

import Model.Appointment;
import Model.Contact;
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
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ResourceBundle;
import java.util.logging.Logger;

/**
 * This class populates a tableview with a report of the the selected contact's appointments
 */

public class ContactSchedulesReport implements Initializable {

    @FXML
    private ComboBox<Contact> contactComboBox;

    @FXML
    private TableView<Appointment> contactAptTableView;

    @FXML
    private TableColumn<Appointment, Integer> appointmentID;

    @FXML
    private TableColumn<Appointment, String> aptTitleTableView;

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

    @FXML
    void onActionDisplaySchedule(ActionEvent event) throws IOException {
        try {

            //selects the contact object to send to query the DB
                Contact contactSchedule = contactComboBox.getSelectionModel().getSelectedItem();
                ReportsDB.sendContactSelection(contactSchedule);

                //Acquires the appointments from the database and populates the tableview
                contactAptTableView.setItems(ReportsDB.getContactSchedule());
                for (Appointment appointment : ReportsDB.contactSchedule) {
                    System.out.println(appointment.getStart());
                }
                appointmentID.setCellValueFactory(new PropertyValueFactory<>("appointmentID"));
                aptTitleTableView.setCellValueFactory(new PropertyValueFactory<>("title"));
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
    void onActionSceneLogout(ActionEvent event) throws IOException {
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        Object scene = FXMLLoader.load(getClass().getResource("/View/Login.fxml"));
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
    ObservableList<Contact> contactList = FXCollections.observableArrayList();

    @FXML
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            Connection conn = DBConnection.startConnection();
            ResultSet rs = conn.createStatement().executeQuery("SELECT * FROM contacts");
            while (rs.next()) {

                //Creates Country Objects via Strings for Country selection
                contactList.add(new Contact(rs.getInt("Contact_ID"), rs.getString("Contact_Name"), rs.getString("Email")));
            }
            contactComboBox.setItems(contactList);

        }
        catch (SQLException ce) {
            Logger.getLogger(ce.toString());
        }
    }
}

