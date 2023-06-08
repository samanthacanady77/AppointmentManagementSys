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

/** This class creates and manages the Modify Customer Form. */
public class ModifyCustomerFormController implements Initializable {
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


    private static Customer passedCustomer;
    private static int index;

    /** This method receives the data passed from the Customer Form.
     * @param selectedCustomer The customer whose schedule is being viewed
     * */
    public static void passInfoToModifyCustomerForm(Customer selectedCustomer) {
        passedCustomer = selectedCustomer;
        index = Customer.getAllCustomers().indexOf(passedCustomer);
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

        Division comboSelection = (Division) divisionComboBox.getSelectionModel().getSelectedItem();

        if(customerName.isBlank() || address.isBlank() || address.isBlank() || postalCode.isBlank() || phone.isBlank()){
            Alerts.errorAlert("You must input a value for every field.");
        }
        else {
            if (divisionComboBox.getSelectionModel().getSelectedItem() == null) {
                Alerts.errorAlert("You must select a division.");
            }
            else {
                Customer customer = new Customer(customerId, customerName, address, postalCode, phone,comboSelection.getDivisionId());

                customer.updateCustomer(customer, index);
                CustomerQuery.update(customerId, customerName, address, postalCode, phone, comboSelection.getDivisionId());

                stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
                scene = FXMLLoader.load(getClass().getResource("/view/CustomerForm.fxml"));
                stage.setScene(new Scene(scene));
                stage.centerOnScreen();
                stage.show();
            }
        }
    }

    /** This method initializes the form along with populating the combo boxes and the text fields with the data to be modified. It also
     * uses a lambda. Instead of using a separate onAction method through JavaFX, the Lambda is in the initialize method making it
     * easier to see and modify. It also empties the division combo box when a new country is selected, as the data there is pre-populated
     * by the passedCustomer data.
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        countryComboBox.setItems(Country.getAllCountries());

        customerIdText.setText(String.valueOf(passedCustomer.getCustomerId()));
        nameText.setText(passedCustomer.getCustomerName());
        addressText.setText(passedCustomer.getAddress());
        postalCodeText.setText(passedCustomer.getPostalCode());
        phoneText.setText(passedCustomer.getPhone());
        countryComboBox.setValue(Country.findCountry(passedCustomer));
        divisionComboBox.setValue(Division.findDivision(passedCustomer));


        Country.findCountry(passedCustomer).findAssociatedDivisions(Country.findCountry(passedCustomer));
        divisionComboBox.setItems(Country.findCountry(passedCustomer).getAllAssociatedDivisions());

        countryComboBox.setOnAction(event1 -> {
            divisionComboBox.setValue(null);

            Country countryComboSelection = (Country) countryComboBox.getSelectionModel().getSelectedItem();
            countryComboSelection.findAssociatedDivisions(countryComboSelection);
            divisionComboBox.setItems(countryComboSelection.getAllAssociatedDivisions());
            divisionComboBox.setDisable(false);
        });

    }
}
