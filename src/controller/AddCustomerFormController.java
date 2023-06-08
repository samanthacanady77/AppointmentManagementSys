package controller;

import helper.Alerts;
import helper.CustomerQuery;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.Country;
import model.Customer;
import model.Division;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;


/** This class creates and manages the Add Customer Form. */
public class AddCustomerFormController implements Initializable {
    Stage stage;
    Parent scene;

    @FXML
    private TextField addressText;
    @FXML
    private TextField customerIdText;
    @FXML
    private TextField nameText;
    @FXML
    private TextField phoneText;
    @FXML
    private TextField postalCodeText;


    @FXML
    private ComboBox<Country> countryComboBox;
    @FXML
    private ComboBox<Division> divisionComboBox;

    /** This method generates unique customer IDs. */
    private int customerIdGenerator(){
        int customerId = 1;

        for(Customer customer : Customer.getAllCustomers()){
            if(customer.getCustomerId() == customerId){
                customerId++;
            }
        }
        return customerId;
    }

    @FXML
    void onActionCancel(ActionEvent event) throws IOException {
        stage = (Stage)((Button)event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/view/CustomerForm.fxml"));
        stage.setScene(new Scene(scene));
        stage.centerOnScreen();
        stage.show();
    }

    /** This method saves the information entered into the form. It does input validation on the data and sends the data to a SQL query.
     * @param event The action of clicking the save button
     * */
    @FXML
    void onActionSave(ActionEvent event) throws IOException, SQLException {
        int customerId = Integer.parseInt(customerIdText.getText());
        String customerName = String.valueOf(nameText.getText());
        String address = String.valueOf(addressText.getText());
        String postalCode = String.valueOf(postalCodeText.getText());
        String phone = String.valueOf(phoneText.getText());

        Division divisionComboSelection = (Division) divisionComboBox.getSelectionModel().getSelectedItem();

        if(customerName.isBlank() || address.isBlank() || address.isBlank() || postalCode.isBlank() || phone.isBlank()){
            Alerts.errorAlert("You must input a value for every field.");
        }
        else{
            if(divisionComboBox.getSelectionModel().getSelectedItem() == null){
                Alerts.errorAlert("You must select a division.");
            }
            else{
                Customer customer = new Customer(customerId, customerName, address, postalCode, phone, divisionComboSelection.getDivisionId());

                customer.addCustomer(customer);

                CustomerQuery.insert(customerId, customerName, address, postalCode, phone, divisionComboSelection.getDivisionId());

                stage = (Stage)((Button)event.getSource()).getScene().getWindow();
                scene = FXMLLoader.load(getClass().getResource("/view/CustomerForm.fxml"));
                stage.setScene(new Scene(scene));
                stage.centerOnScreen();
                stage.show();
            }
        }
    }

    /** This method initializes the form and populates the country combo box and customer ID field. */

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        customerIdText.setText(String.valueOf(customerIdGenerator()));
        countryComboBox.setItems(Country.getAllCountries());

        countryComboBox.setOnAction(event1 -> {
            Country countryComboSelection = (Country) countryComboBox.getSelectionModel().getSelectedItem();
            countryComboSelection.findAssociatedDivisions(countryComboSelection);
            divisionComboBox.setItems(countryComboSelection.getAllAssociatedDivisions());
            divisionComboBox.setDisable(false);
        });
    }
}
