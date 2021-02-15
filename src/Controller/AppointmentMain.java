package Controller;

import Model.Appointment;
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
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static Utility.AppointmentDB.deleteAppointment;

/**
 * This class creates the main appointment screen where a user can add, edit, and delete appointments
 */
public class AppointmentMain implements Initializable {

    @FXML
    private RadioButton allAptsRadio;

    @FXML
    private ToggleGroup aptTableToggle;

    @FXML
    private RadioButton weeklyRB;

    @FXML
    private RadioButton monthlyRB;

    @FXML
    private TableView<Appointment> aptTableView;

    @FXML
    private TableColumn<Appointment, Integer> aptIDTableColumn;

    @FXML
    private TableColumn<Appointment, String> aptTitleTableColumn;

    @FXML
    private TableColumn<Appointment, String> aptDescriptionTableColumn;

    @FXML
    private TableColumn<Appointment, String> aptLocationTableColumn;

    @FXML
    private TableColumn<Appointment, String> aptTypeTableColumn;

    @FXML
    private TableColumn<Appointment, LocalDateTime> aptStartTableColumn;

    @FXML
    private TableColumn<Appointment, LocalDateTime> aptEndTableColumn;

    @FXML
    private TableColumn<Appointment, LocalDateTime> aptCreateDateTableColumn;

    @FXML
    private TableColumn<Appointment, String> aptCreatedByTableColumn;

    @FXML
    private TableColumn<Appointment, LocalDateTime> aptLastUpdateTableColumn;

    @FXML
    private TableColumn<Appointment, String> aptLastUpdatedByTableColumn;

    @FXML
    private TableColumn<Appointment, Integer> aptCIDTableColumn;

    @FXML
    private TableColumn<Appointment, Integer> aptUIDTableColumn;

    @FXML
    private TableColumn<Appointment, Integer> aptContactIDTableColumn;

    @FXML
    private Button addAptButton;

    @FXML
    private Button editAptButton;

    @FXML
    private Button deleteAptButton;

    @FXML
    private Button logoutButton;

    @FXML
    private Button menuButton;

    @FXML
    private Button exitButton;

    ObservableList<Appointment> aptList = AppointmentDB.getAllAppointments();
    ObservableList<Appointment> weeklyAppointmentList = FXCollections.observableArrayList();
    ObservableList<Appointment> monthlyAppointmentList = FXCollections.observableArrayList();

    public AppointmentMain() throws SQLException {
    }

    /**
     * Here, a for each lambda loop is utilized to get all appointments, reduce the amount of code, and improve visual cleanliness.
     * @param event
     * @throws SQLException
     */
    @FXML
    void onActionAllAptsRadio(ActionEvent event) throws SQLException {

        try {
            aptTableView.setItems(AppointmentDB.getAllAppointments());
            for (Appointment appointment : AppointmentDB.allAppointments) {
                System.out.println(appointment.getStart());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * This method uses the date variables to compare the start date from the Appointment list and the user's system date
     * The predicate looks at today's date OR after AND also before one week from today's system date
     * The list is cast to the Appointment Observable List
     * @param event
     */

    @FXML
    void onActionWeeklyAptsRadio(ActionEvent event) throws SQLException {
        LocalDate today = LocalDate.from(ZonedDateTime.now());
        LocalDate oneWeekFromToday = LocalDate.from(ZonedDateTime.now()).plusWeeks(1);


        if ((this.aptTableToggle.getSelectedToggle().equals(this.weeklyRB))) {
            Predicate<Appointment> weeklyView = appointment -> (appointment.getStart().toLocalDate().equals(today))
                    || appointment.getStart().toLocalDate().isAfter(today)
                    && appointment.getStart().toLocalDate().isBefore((oneWeekFromToday));
            System.out.println(today);

            var weeklyResult = aptList.stream().filter(weeklyView).collect(Collectors.toList());
            aptTableView.setItems(weeklyAppointmentList = FXCollections.observableList(weeklyResult));
        }
    }

    /**
     * This method uses the radio button to display filtered appointments on a monthly basis
     * @param event
     */

    @FXML
    void onActionMonthlyAptsRadio(ActionEvent event) {

        //Date variables for comparative operations of the start date from the Appointment list and the system date pulled from User's system.
        LocalDate today = LocalDate.from(ZonedDateTime.now());
        LocalDate oneMonthFromToday = LocalDate.from(ZonedDateTime.now()).plusMonths(1);
        if ((this.aptTableToggle.getSelectedToggle().equals(this.monthlyRB))) {
            Predicate<Appointment> monthlyView = appointment -> (appointment.getStart().toLocalDate().equals(today))
                    || appointment.getStart().toLocalDate().isAfter((today))
                    && appointment.getStart().toLocalDate().isBefore((oneMonthFromToday));
            System.out.println(today);

            var monthList = aptList.stream().filter(monthlyView).collect(Collectors.toList());
            //Cast the list to the Appointment Observable List
            aptTableView.setItems(monthlyAppointmentList = FXCollections.observableList(monthList));
        }
    }

    @FXML
    void onActionExitApp(ActionEvent event) {
        Button sourceButton = (Button) event.getSource();
        exitButton.setText(sourceButton.getText());
        DBConnection.closeConnection();
        System.exit(0);
    }

    /**
     * This method takes the user to the addAppointment scene when the button is clicked
     * @param event
     * @throws IOException
     * @see AppointmentDB#addAppointment(Integer, String, String, String, String, LocalDateTime, LocalDateTime, LocalDateTime, String, LocalDateTime, String, Integer, Integer, Integer)
     */

    @FXML
    void onActionSceneAddApt(ActionEvent event) throws IOException {

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/View/AddAppointment.fxml"));
        loader.load();

        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        Parent scene = loader.getRoot();
        stage.setScene(new Scene(scene));
        stage.show();
    }


    /**
     * This method takes the user to the deleteAppointment scene when the button is clicked
     * @param event
     * @see AppointmentDB#deleteAppointment(int)
     */

    @FXML
    void onActionSceneDeleteApt(ActionEvent event) {
        try {

            Appointment selectedItem = aptTableView.getSelectionModel().getSelectedItem();
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText("Warning");
            alert.setHeaderText("Delete appointment type: " + selectedItem.getType() + " ID Number: " + selectedItem.getAppointmentID() + " ?");
            alert.setContentText("Are you sure you want to delete the appointment?");
            Optional<ButtonType> result = alert.showAndWait();

            if (result.isPresent() && result.get() == ButtonType.OK) {
                deleteAppointment(selectedItem.getAppointmentID());
                System.out.println("Appointment: " + selectedItem.getAppointmentID() + " Successful!");
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("/View/AppointmentMain.fxml"));
                Parent parent = loader.load();

                Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
                Parent scene = loader.getRoot();
                stage.setScene(new Scene(scene));
                stage.show();

            }
        } catch (IOException e) {
            String s = "a customer";
        }
    }

    /**
     * This method takes the user to the editAppointment scene when the button is clicked
     * @param event
     * @throws IOException
     * @see AppointmentDB#editAppointment(Integer, String, String, String, String, LocalDateTime, LocalDateTime, LocalDateTime, String, LocalDateTime, String, Integer, Integer, Integer)
     */

    @FXML
    void onActionSceneEditApt(ActionEvent event) throws IOException {
        try {

            Appointment modifyAppointment = aptTableView.getSelectionModel().getSelectedItem();
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/View/EditAppointment.fxml"));
            Parent parent = loader.load();
            Scene modifyCustomerScene = new Scene(parent);

            //Sends the selected object from the table to a method on the Edit Appointment Scene
            EditAppointment controller = loader.getController();
            controller.sendAppointment(modifyAppointment);

            Stage window = (Stage) ((Button) event.getSource()).getScene().getWindow();
            window.setScene(modifyCustomerScene);
            window.setResizable(false);
            window.show();
        }
        catch (NullPointerException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Missing selection");
            alert.setContentText("Please select an appointment you would like to edit.");
            alert.showAndWait();
        }
    }

    /**
     * This method takes the user to the login scene when the button is clicked.
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

    /**
     * This method takes the user to the login scene when the button is clicked.
     * @param event
     * @throws IOException
     */

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
     * Here a for each lambda is used to get all appointments using less code
     * @param resourceBundle
     * @param url
     */

    @FXML
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //Appointments Table
        aptIDTableColumn.setCellValueFactory(new PropertyValueFactory<>("appointmentID"));
        aptTitleTableColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        aptLocationTableColumn.setCellValueFactory(new PropertyValueFactory<>("location"));
        aptDescriptionTableColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        aptTypeTableColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        aptStartTableColumn.setCellValueFactory(new PropertyValueFactory<>("start"));
        aptEndTableColumn.setCellValueFactory(new PropertyValueFactory<>("end"));
        aptCreateDateTableColumn.setCellValueFactory(new PropertyValueFactory<>("createDate"));
        aptCreatedByTableColumn.setCellValueFactory(new PropertyValueFactory<>("createdBy"));
        aptLastUpdateTableColumn.setCellValueFactory(new PropertyValueFactory<>("lastUpdate"));
        aptLastUpdatedByTableColumn.setCellValueFactory(new PropertyValueFactory<>("lastUpdatedBy"));
        aptCIDTableColumn.setCellValueFactory(new PropertyValueFactory<>("customerID"));
        aptUIDTableColumn.setCellValueFactory(new PropertyValueFactory<>("userID"));
        aptContactIDTableColumn.setCellValueFactory(new PropertyValueFactory<>("contactID"));
        try {
            aptTableView.setItems(AppointmentDB.getAllAppointments());

            for (Appointment appointment : AppointmentDB.allAppointments) {
                System.out.println(appointment.getStart());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
