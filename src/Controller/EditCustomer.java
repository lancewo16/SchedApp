package Controller;


import Model.Countries;
import Model.Customer;
import Model.FirstLevelDivision;
import Utility.CountriesDB;
import Utility.CustomerDB;
import Utility.DBConnection;
import Utility.FirstLevelDivisionDB;
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
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

/**
 * This class is where users can edit the customer information.
 * This class utilizes the CustomerDB class and information from the database.
 */

public class EditCustomer implements Initializable {

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
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
    private TextField lastUpdatedByTextField;

    @FXML
    private TextField lastUpdateTextField;

    @FXML
    private TextField createdByTextField;

    @FXML
    private TextField dateCreatedTextField;

    @FXML
    private ComboBox<Countries> countryComboBox;

    @FXML
    private ComboBox<FirstLevelDivision> divisionIDComboBox;

    @FXML
    private Button logoutButton;

    @FXML
    private Button backButton;

    public EditCustomer() throws SQLException {
    }

    /**
     * This method takes any updated fields and returns them to the database
     * @see CustomerDB#editCustomer(Integer, String, String, String, String, Timestamp, String, Timestamp, String, Integer)
     * @param event
     * @return
     * @throws SQLException
     * @throws IOException
     */
    @FXML
    boolean onActionEditCustomer(ActionEvent event) throws SQLException, IOException {
        try {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/View/CustomerMain.fxml"));
        Parent parent = loader.load();

        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        Parent scene = loader.getRoot();
        stage.setScene(new Scene(scene));
        stage.show();

        return CustomerDB.editCustomer(
                Integer.valueOf(customerIDTextField.getText()),
                customerNameTextField.getText(),
                customerAddressTextField.getText(),
                customerPostalTextField.getText(),
                customerPhoneTextField.getText(),
                Timestamp.valueOf(LocalDateTime.parse(dateCreatedTextField.getText(), formatter).minus(Duration.ofSeconds(offsetToUTC))),
                createdByTextField.getText(),
                Timestamp.valueOf(LocalDateTime.parse(lastUpdateTextField.getText(), formatter).minus(Duration.ofSeconds(offsetToUTC))),
                lastUpdatedByTextField.getText(),
                Integer.valueOf(String.valueOf(divisionIDComboBox.getSelectionModel().getSelectedItem().getDivisionID())));
        }
      catch (DateTimeParseException e) {
          Alert alert = new Alert(Alert.AlertType.ERROR);
          alert.setTitle("Missing selection");
          alert.setContentText("Please ensure all date and time fields are formatted YYYY-MM-DD HH:MM prior to adding an appointment");
          alert.showAndWait();
          return false;
      }

        }

    @FXML
    private void onActionAddCustomer(ActionEvent event)
    {

    }

    /**
     * This method sets the selected object from the Main Customer Tableview
     * @see CustomerMain#onActionSceneEditCustomer(ActionEvent)
     * @param modifyCustomer
     */

    @FXML
    public void sendCustomer(Customer modifyCustomer)
    {
        customerIDTextField.setText(String.valueOf(modifyCustomer.getCustomerID()));
        customerNameTextField.setText(modifyCustomer.getCustomerName());
        customerAddressTextField.setText(modifyCustomer.getAddress());
        customerPostalTextField.setText(modifyCustomer.getPostal());
        customerPhoneTextField.setText(String.valueOf(modifyCustomer.getPhone()));
        lastUpdatedByTextField.setText(modifyCustomer.getLastUpdatedBy());
        lastUpdateTextField.setText(modifyCustomer.getLastUpdate().format(formatter));
        createdByTextField.setText(modifyCustomer.getCreatedBy());
        dateCreatedTextField.setText(modifyCustomer.getCreateDate().format(formatter));
        int comboBoxPreset = modifyCustomer.getDivisionID();
        FirstLevelDivision fld = new FirstLevelDivision(comboBoxPreset);
        divisionIDComboBox.setValue(fld);

        //Here the division name in the combobox is set based on the country the user selects
        if (fld.getDivisionID() <= 54)
        {
            String countryName = "U.S";
            Countries c = new Countries(countryName);
            countryComboBox.setValue(c);
        }
        else if (fld.getDivisionID() >54 && fld.getDivisionID() <= 72)
        {
            String countryName = "UK";
            Countries c = new Countries(countryName);
            countryComboBox.setValue(c);
        }
        else if (fld.getDivisionID() > 72)
        {
            String countryName = "Canada";
            Countries c = new Countries(countryName);
            countryComboBox.setValue(c);
        }

        try {
            countryComboBox.setItems(CountriesDB.getAllCountries());
            for (Countries countries : CountriesDB.allCountries) {
                System.out.println(countries.getCountry());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            divisionIDComboBox.setItems(FirstLevelDivisionDB.getAllFirstLevelDivisions());
            for (FirstLevelDivision firstLevelDivision : FirstLevelDivisionDB.allFirstLevelDivisions) {
                System.out.println(firstLevelDivision.getDivision());
            }
            divisionIDComboBox.setValue(fld);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    ObservableList<FirstLevelDivision> firstLevelDivisionObservableList = FirstLevelDivisionDB.getAllFirstLevelDivisions();
    ObservableList<FirstLevelDivision> usFirstLevelDivisionObservableList = FXCollections.observableArrayList();
    ObservableList<FirstLevelDivision> canadaFirstLevelDivisionObservableList = FXCollections.observableArrayList();
    ObservableList<FirstLevelDivision> ukFirstLevelDivisionObservableList = FXCollections.observableArrayList();

    /**
     * A lambda is used here that is made up of filtered lists for each country, utilizing a predicate f, and sorting through the objects.
     * A stream filter is used to set the selectable divisions based on country selected from combobox.
     * @param event
    */

    @FXML
    private void onActionSetDivisionID(ActionEvent event) throws IOException, SQLException {
        if (countryComboBox.getSelectionModel().isEmpty()) {
            System.out.println(countryComboBox.getSelectionModel().toString());
        }

        //US Filter
        else if (countryComboBox.getSelectionModel().getSelectedItem().getCountry().equals("U.S")) {
            var usResult = firstLevelDivisionObservableList.stream().filter(f -> f.getDivisionID() < 54).collect(Collectors.toList());
            divisionIDComboBox.setItems(usFirstLevelDivisionObservableList = FXCollections.observableList(usResult));
        }
        //Canada Filter
        else if (countryComboBox.getSelectionModel().getSelectedItem().getCountry().equals("Canada")) {
            var canadaResult = firstLevelDivisionObservableList.stream().filter(f -> (f.getDivisionID() > 54) && (f.getDivisionID() < 101)).collect(Collectors.toList());
            divisionIDComboBox.setItems(canadaFirstLevelDivisionObservableList = FXCollections.observableList(canadaResult));
        }
        //UK Filter
        else if (countryComboBox.getSelectionModel().getSelectedItem().getCountry().equals("UK")) {
            var ukResult = firstLevelDivisionObservableList.stream().filter(f -> f.getDivisionID() >= 101).collect(Collectors.toList());
            divisionIDComboBox.setItems(ukFirstLevelDivisionObservableList = FXCollections.observableList(ukResult));
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

    /**
     * This method takes the user back to the Main Customer Screen
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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
