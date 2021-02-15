package Controller;

import Model.Countries;
import Model.Customer;
import Model.FirstLevelDivision;
import Model.User;
import Utility.CountriesDB;
import Utility.CustomerDB;
import Utility.DBConnection;
import Utility.FirstLevelDivisionDB;
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
import javafx.scene.input.MouseEvent;
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
import java.util.ResourceBundle;

/**
 * This class creates a controller to add a customer
 * */
public class AddCustomer implements Initializable {

    //Format for the LocalDateTime fields
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    //Gets the offset of the users system date time from UTC used in converting the time pulled from the database
    Long offsetToUTC = (long) (ZonedDateTime.now().getOffset()).getTotalSeconds();

    @FXML
    private Button exitButton;

    @FXML
    private Button addCustomerButton;

    @FXML
    private TextField customerIDTextField;

    @FXML
    private TextField customerNameTextField;

    @FXML
    private TextField customerAddressTextField;

    @FXML
    private TextField customerPostalTextField;

    @FXML
    private TextField customerPhoneTextField;

    @FXML
    private TextField createdByTextField;

    @FXML
    private TextField dateCreatedTxt;

    @FXML
    private TextField lastUpdateTextField;

    @FXML
    private TextField lastUpdatedByTextField;

    @FXML
    private Button logoutButton;

    @FXML
    private Button backButton;

    @FXML
    private ComboBox<FirstLevelDivision> divisionComboBox;

    @FXML
    private ComboBox<Countries> countryComboBox;

    public AddCustomer() throws SQLException {
    }

    /**
     * This method grabs the customer fields and sends them to the database
     * @see CustomerDB#addCustomer(Integer, String, String, String, String, LocalDateTime, String, LocalDateTime, String, Integer)
     * @param event Adds Customer
     * @return
     * @throws IOException
     */
    @FXML
    void onActionAddCustomer(ActionEvent event) throws IOException {
        try {
            Integer customerID = Integer.valueOf(customerIDTextField.getText());
            String customerName = customerNameTextField.getText();
            String address = customerAddressTextField.getText();
            String postal = customerPostalTextField.getText();
            String phone = customerPhoneTextField.getText();
            LocalDateTime createDate = LocalDateTime.parse(dateCreatedTxt.getText(), formatter).minus(Duration.ofSeconds(offsetToUTC));
            String createdBy = createdByTextField.getText();
            LocalDateTime lastUpdate = LocalDateTime.parse(lastUpdateTextField.getText(), formatter).minus(Duration.ofSeconds(offsetToUTC));
            String lastUpdatedBy = lastUpdatedByTextField.getText();
            int divisionID = Integer.parseInt(String.valueOf(divisionComboBox.getSelectionModel().getSelectedItem().getDivisionID()));

            if (customerIDTextField.getText().isEmpty() ||
                    customerNameTextField.getText().isEmpty() ||
                    customerAddressTextField.getText().isEmpty() ||
                    customerPostalTextField.getText().isEmpty() ||
                    customerPhoneTextField.getText().isEmpty()){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("ERROR");
                alert.setContentText("Please fill all fields for the customer you are adding");
                alert.showAndWait();
                return;
            } else {
                Customer customer = new Customer(customerID, customerName, address, postal, phone,createDate, createdBy, lastUpdate, lastUpdatedBy, divisionID);

                boolean customerCreated = CustomerDB.addCustomer(customerID, customerName, address, postal, phone, createDate, createdBy, lastUpdate, lastUpdatedBy, divisionID);
                if (customerCreated) {
                    CustomerDB.getAllCustomers().add(customer);

                    Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
                    Object scene = FXMLLoader.load(getClass().getResource("/View/CustomerMain.fxml"));
                    stage.setScene(new Scene((Parent) scene));
                    stage.show();
                }
            }
        } catch (DateTimeParseException | SQLException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR");
            alert.setContentText("Date and time fields must be in format: YYYY-MM-DD HH:MM");
            alert.showAndWait();
            return;
        }
    }

    @FXML
    void exitApp(ActionEvent event) {
        Button sourceButton = (Button) event.getSource();
        exitButton.setText(sourceButton.getText());
        DBConnection.closeConnection();
        System.exit(0);
    }


    @FXML
    void onActionBackCustomerMain(ActionEvent event) throws IOException {
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        Object scene = FXMLLoader.load(getClass().getResource("/View/CustomerMain.fxml"));
        stage.setScene(new Scene((Parent) scene));
        stage.show();
    }

    //The Division ID List for the Combo Box
    ObservableList<FirstLevelDivision> fldList = FirstLevelDivisionDB.getAllFirstLevelDivisions();


    /**
     * This method gives each country its own list
     * @see FirstLevelDivisionDB#usFilteredFirstLevelDivisions
     * @see FirstLevelDivisionDB#ukFilteredFirstLevelDivisions
     * @see FirstLevelDivisionDB#canadaFilteredFirstLevelDivisions
     * A for each lambda loop is run within each combobox, thus reducing the amount of code needed to iterate through objects
     * @param event After one of the countries is selected, the filtered lists will be set in the division combo box
     */
    @FXML
    void SetDivisionID(MouseEvent event) {

        if (countryComboBox.getSelectionModel().isEmpty()) {
            System.out.println(countryComboBox.getSelectionModel().toString());
        }

        //US Filter
        else if (countryComboBox.getSelectionModel().getSelectedItem().getCountry().equals("U.S")) {
            try {
                divisionComboBox.setItems(FirstLevelDivisionDB.getusFilteredFirstLevelDivisions());

                for (FirstLevelDivision usFLD : FirstLevelDivisionDB.usFilteredFirstLevelDivisions) {
                    System.out.println(usFLD.getDivision());
                }
            }
            catch (SQLException e) {
                e.printStackTrace();
            }
                }

        //Canada Filter
        else if (countryComboBox.getSelectionModel().getSelectedItem().getCountry().equals("Canada")) {
            try {
                divisionComboBox.setItems(FirstLevelDivisionDB.getcanadaFilteredFirstLevelDivisions());
                for (FirstLevelDivision canadaFLD : FirstLevelDivisionDB.canadaFilteredFirstLevelDivisions) {
                    System.out.println(canadaFLD.getDivision());
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        //UK Filter
        else if (countryComboBox.getSelectionModel().getSelectedItem().getCountry().equals("UK")) {
            try {
                divisionComboBox.setItems(FirstLevelDivisionDB.getukFilteredFirstLevelDivisions());
                for (FirstLevelDivision ukFLD : FirstLevelDivisionDB.ukFilteredFirstLevelDivisions) {
                    System.out.println(ukFLD.getDivision());
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

    }

    @FXML
    void onActionSceneLogout(ActionEvent event) throws IOException {
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        Object scene = FXMLLoader.load(getClass().getResource("/View/Login.fxml"));
        stage.setScene(new Scene((Parent) scene));
        stage.show();
    }

    /**
     * This method sets the combo boxes and customer ID
     * @param url
     * @param resourceBundle
     */
    @FXML
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            countryComboBox.setItems(CountriesDB.getAllCountries());
            for (Countries countries : CountriesDB.allCountries) {
                System.out.println(countries.getCountry());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            divisionComboBox.setItems(FirstLevelDivisionDB.getAllFirstLevelDivisions());
            for (FirstLevelDivision firstLevelDivision : FirstLevelDivisionDB.allFirstLevelDivisions) {
                System.out.println(firstLevelDivision.getDivision());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        createdByTextField.setText(String.valueOf(User.getUsername()));
            lastUpdatedByTextField.setText(String.valueOf(User.getUsername()));
            try {

                // Establishes the connection to the database
                Connection conn = DBConnection.startConnection();

                //Selects the last row from Customer ID
                ResultSet rs = conn.createStatement().executeQuery("SELECT Customer_ID FROM customers ORDER BY Customer_ID DESC LIMIT 1 ");
                // Here is the SQL query

                while (rs.next()) {
                    //Creates a temporary var for customer ID
                    int tempID = rs.getInt("Customer_ID");
                    //Sets the customer ID to auto increment by 1
                    customerIDTextField.setText(String.valueOf(tempID + 1));
                    System.out.println(rs.getInt(tempID));

                }


            } catch (Exception exc) {
                exc.printStackTrace();
            }
        }
    }




