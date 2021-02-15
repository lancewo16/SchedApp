package Controller;

import Model.Customer;
import Utility.CustomerDB;
import Utility.DBConnection;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Logger;

/**
 * This class creates the main view where a user can add, edit, and delete customers.
 * A tableview displays all customers.
 */

public class CustomerMain implements Initializable {

    @FXML
    private TableView<Customer> customerTableView;

    @FXML
    private TableColumn<Customer, Integer> customerIdTableColumn;

    @FXML
    private TableColumn<Customer, String> nameTableColumn;

    @FXML
    private TableColumn<Customer, String> addressTableColumn;

    @FXML
    private TableColumn<Customer, String> postalTableColumn;

    @FXML
    private TableColumn<Customer, String> phoneTableColumn;

    @FXML
    private TableColumn<Customer, LocalDateTime> createDateTableColumn;

    @FXML
    private TableColumn<Customer, String> createdByTableColumn;

    @FXML
    private TableColumn<Customer, Timestamp> lastUpdateTableColumn;

    @FXML
    private TableColumn<Customer, String> lastUpdatedByTableColumn;

    @FXML
    private TableColumn<Customer, Integer> divisionIDTableColumn;

    @FXML
    private Button addCustomerButton;

    @FXML
    private Button deleteCustomerButton;

    @FXML
    private Button editCustomerButton;

    @FXML
    private Button mainButton;

    @FXML
    private Button exitButton;

    @FXML
    private Button logoutButton;

    @FXML
    void onActionExitApp(ActionEvent event) {
        Button sourceButton = (Button) event.getSource();
        exitButton.setText(sourceButton.getText());
        DBConnection.closeConnection();
        System.exit(0);
    }

    /**
     * This method adds a new customer to the database
     * @param event
     * @throws IOException
     * @see Utility.CustomerDB#addCustomer(Integer, String, String, String, String, LocalDateTime, String, LocalDateTime, String, Integer)
     */

    @FXML
    void onActionSceneAddCustomer(ActionEvent event) throws IOException {

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/View/AddCustomer.fxml"));
        loader.load();

        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        Parent scene = loader.getRoot();
        stage.setScene(new Scene(scene));
        stage.show();
    }

    /**
     * This method deletes a customer
     * @see Utility.CustomerDB#deleteCustomer(int)
     * @param event
     * @throws SQLException
     * @throws IOException
     */

    @FXML
    void onActionSceneDeleteCustomer(ActionEvent event) throws SQLException, IOException {
        try {
            Customer selectedItem = customerTableView.getSelectionModel().getSelectedItem();
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText("Warning");
            alert.setHeaderText("All associated appointments for " + selectedItem.getCustomerName() + " will be deleted");
            alert.setContentText("Are you sure you want to delete the customer?");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                CustomerDB.deleteCustomer(selectedItem.getCustomerID());
                alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("");
                alert.setContentText("Deletion Successful");
                alert.showAndWait();
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("/View/CustomerMain.fxml"));
                Parent parent = loader.load();

                Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
                Parent scene = loader.getRoot();
                stage.setScene(new Scene(scene));
                stage.show();

            }
        } catch (NullPointerException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR");
            alert.setContentText("Please select a customer to delete");
            alert.showAndWait();
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
     *  This method populates the edit customer scene after user selects an item from the table view
     * If user selects nothing, an alert prompts user to make a selection
     * @see Controller.EditCustomer
     * @param event
     * @throws IOException
     */

    @FXML
    void onActionSceneEditCustomer(ActionEvent event) throws IOException {
        try {
            Customer modifyCustomer = customerTableView.getSelectionModel().getSelectedItem();
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/View/EditCustomer.fxml"));
            Parent parent = loader.load();
            Scene modifyCustomerScene = new Scene(parent);
            EditCustomer controller = loader.getController();
            controller.sendCustomer(modifyCustomer);

            Stage window = (Stage) ((Button) event.getSource()).getScene().getWindow();
            window.setScene(modifyCustomerScene);
            window.setResizable(false);
            window.show();
        } catch (NullPointerException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR");
            alert.setContentText("Please select a customer to edit");
            alert.showAndWait();
        }
    }

    @FXML
    void onActionComboBoxSelect(ActionEvent event) {

    }
    public CustomerMain() {
        //Set New Customer ID Auto-Increment
        try {
            Statement statement = DBConnection.startConnection().createStatement();
            statement.executeUpdate("ALTER TABLE customers AUTO_INCREMENT");
        } catch (SQLException ce) {
            Logger.getLogger(ce.toString());
        }
    }

    /**
     * This method sets the values of the tableview from the database information
     * A for each lambda is run in the try statement to get all objects
     * @see CustomerDB#getAllCustomers()
     * @param url
     * @param resourceBundle
     */
    @FXML
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //Customer Table
        customerIdTableColumn.setCellValueFactory(new PropertyValueFactory<>("customerID"));
        nameTableColumn.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        addressTableColumn.setCellValueFactory(new PropertyValueFactory<>("address"));
        postalTableColumn.setCellValueFactory(new PropertyValueFactory<>("postal"));
        phoneTableColumn.setCellValueFactory(new PropertyValueFactory<>("phone"));
        createDateTableColumn.setCellValueFactory(new PropertyValueFactory<>("createDate"));
        createdByTableColumn.setCellValueFactory(new PropertyValueFactory<>("createdBy"));
        lastUpdateTableColumn.setCellValueFactory(new PropertyValueFactory<>("lastUpdate"));
        lastUpdatedByTableColumn.setCellValueFactory(new PropertyValueFactory<>("lastUpdatedBy"));
        divisionIDTableColumn.setCellValueFactory(new PropertyValueFactory<>("divisionID"));

        try {
           customerTableView.setItems(CustomerDB.getAllCustomers());
           for (Customer customer : CustomerDB.allCustomers)
           {
               System.out.println(customer.getCustomerName());
           }
            }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
