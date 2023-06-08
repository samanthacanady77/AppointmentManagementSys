package controller;
import helper.Alerts;
import helper.CustomerQuery;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Region;
import javafx.stage.Stage;
import model.Customer;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;

/** This class creates and manages the Customer Form. */

public class CustomerFormController implements Initializable {
        Stage stage;
        Parent scene;

        @FXML
        private TableView<Customer> customerTableView;
        @FXML
        private TableColumn<Customer, String> addressCol;
        @FXML
        private TableColumn<Customer, Integer> customerIdCol;
        @FXML
        private TableColumn<Customer, Integer> divisionIdCol;

        @FXML
        private TableColumn<Customer, String> nameCol;
        @FXML
        private TableColumn<Customer, Integer> phoneCol;
        @FXML
        private TableColumn<Customer, String> postalCodeCol;

        @FXML
        void onActionAdd(ActionEvent event) throws IOException {
                stage = (Stage)((Button)event.getSource()).getScene().getWindow();
                scene = FXMLLoader.load(getClass().getResource("/view/AddCustomerForm.fxml"));
                stage.setScene(new Scene(scene));
                stage.centerOnScreen();
                stage.show();
        }

        /** This method deletes a user selected customer. It confirms cancellation and sends the data to a SQL query.
         * @param event The action of clicking the delete button
         * @throws SQLException
         */
        @FXML
        void onActionDelete(ActionEvent event) throws IOException, SQLException {
                Customer selectedCustomer = customerTableView.getSelectionModel().getSelectedItem();
                try {
                        if (selectedCustomer == null) {
                                Alerts.errorAlert("You must select a customer to be deleted.");
                        }
                        else {

                                Alert alert = new Alert(Alert.AlertType.CONFIRMATION,"Are you sure you want to delete this customer and " +
                                        "cancel all associated appointments?");
                                alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
                                Optional<ButtonType> result = alert.showAndWait();

                                if(result.isPresent() && result.get() == ButtonType.OK) {

                                        CustomerQuery.delete(selectedCustomer.getCustomerId());
                                        selectedCustomer.deleteCustomer(selectedCustomer);

                                        Alerts.informationAlert("Customer information for " + selectedCustomer.getCustomerName() +
                                                " has been deleted and all associated appointments have been canceled." );
                                }
                        }
                }
                catch(NullPointerException e){
                        Alerts.errorAlert("You must select a customer to be deleted.");
                }
        }

        /** This method takes a user selected customer and passes the data to the Modify Customer Form.
         * @param event The action of clicking the modify button
         * */
        @FXML
        void onActionModify(ActionEvent event) throws IOException {
                Customer selectedCustomer = customerTableView.getSelectionModel().getSelectedItem();

                if(selectedCustomer == null){
                        Alerts.errorAlert("You must select a customer to be modified.");
                }

                else{
                        ModifyCustomerFormController.passInfoToModifyCustomerForm(selectedCustomer);

                        stage = (Stage)((Button)event.getSource()).getScene().getWindow();
                        scene = FXMLLoader.load(getClass().getResource("/view/ModifyCustomerForm.fxml"));
                        stage.setScene(new Scene(scene));
                        stage.centerOnScreen();
                        stage.show();
                }
        }

        @FXML
        void onActionBack(ActionEvent event) throws IOException {
                stage = (Stage)((Button)event.getSource()).getScene().getWindow();
                scene = FXMLLoader.load(getClass().getResource("/view/MainMenuForm.fxml"));
                stage.setScene(new Scene(scene));
                stage.centerOnScreen();
                stage.show();
        }

        /** This method allows shows a user selected customer's schedule.
         * @param event The action of clicking the view customer schedule button
         * */
        @FXML
        void onActionChangeToCustomerScheduleForm(ActionEvent event) throws IOException {
                Customer selectedCustomer = customerTableView.getSelectionModel().getSelectedItem();

                if(selectedCustomer == null){
                        Alerts.errorAlert("You must select a customer.");
                }
                else{
                        CustomerScheduleFormController.passInfoToCustomerScheduleForm(selectedCustomer);

                        stage = (Stage)((Button)event.getSource()).getScene().getWindow();
                        scene = FXMLLoader.load(getClass().getResource("/view/CustomerScheduleForm.fxml"));
                        stage.setScene(new Scene(scene));
                        stage.centerOnScreen();
                        stage.show();
                }
        }

        /** This method initializes the form and populates the table view. */
        @Override
        public void initialize(URL url, ResourceBundle resourceBundle) {
                customerTableView.setItems(Customer.getAllCustomers());
                customerIdCol.setCellValueFactory(new PropertyValueFactory<>("customerId"));
                nameCol.setCellValueFactory(new PropertyValueFactory<>("customerName"));
                addressCol.setCellValueFactory(new PropertyValueFactory<>("address"));
                postalCodeCol.setCellValueFactory(new PropertyValueFactory<>("postalCode"));
                phoneCol.setCellValueFactory(new PropertyValueFactory<>("phone"));
                divisionIdCol.setCellValueFactory(new PropertyValueFactory<>("divisionId"));
        }
}
