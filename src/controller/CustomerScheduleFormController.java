package controller;

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
import model.*;

import java.io.IOException;
import java.net.URL;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

/** This class creates and manages the Customer Schedule Form. */
public class CustomerScheduleFormController implements Initializable {
    Stage stage;
    Parent scene;

    @FXML
    private Label scheduleForLabel;
    @FXML
    private Label phoneNumberLabel;
    @FXML
    private Label locationLabel;

    @FXML
    private TableView<Appointment> appointmentTableView;
    @FXML
    private TableColumn<Appointment, Integer> customerIdCol;
    @FXML
    private TableColumn<Appointment, String> descriptionCol;
    @FXML
    private TableColumn<Appointment, ZonedDateTime> endCol;
    @FXML
    private TableColumn<Appointment, ZonedDateTime> startCol;
    @FXML
    private TableColumn<Appointment, String> titleCol;
    @FXML
    private TableColumn<Appointment, String> typeCol;
    @FXML
    private TableColumn<Appointment, Integer> appointmentIdCol;


    private static Customer passedCustomer;
    private static int index;

    /** This method receives the data passed from the Customer Form.
     * @param selectedCustomer The customer whose schedule is being viewed
     * */
    public static void passInfoToCustomerScheduleForm(Customer selectedCustomer) {
        passedCustomer = selectedCustomer;
        index = Customer.getAllCustomers().indexOf(passedCustomer);
    }

    /** This method formats the time information in the associated columns for easier readability.
     * @param columnName The table column being formatted
     * */
    //Solution found on https://stackoverflow.com/questions/47484280/format-of-date-in-the-javafx-tableview
    public void formatTimeColumn(TableColumn<Appointment, ZonedDateTime> columnName){
        columnName.setCellFactory((TableColumn<Appointment, ZonedDateTime> column) -> new TableCell<>() {
            @Override
            protected void updateItem(ZonedDateTime item, boolean empty) {
                super.updateItem(item, empty);
                if (item == null || empty) {
                    setText(null);
                } else {
                    setText(item.withZoneSameInstant(ZoneId.systemDefault()).format(DateTimeFormatter.ofPattern("MM/dd/yyyy H:mm")));
                }
            }
        });
    }

    /** This method populates the table view.
     * @param appointmentList The observable list being used to populate the table
     */
    public void populateTable(ObservableList<Appointment> appointmentList) {
        appointmentTableView.setItems(appointmentList);
        appointmentIdCol.setCellValueFactory(new PropertyValueFactory<>("appointmentId"));
        titleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
        descriptionCol.setCellValueFactory(new PropertyValueFactory<>("description"));
        typeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
        startCol.setCellValueFactory(new PropertyValueFactory<>("start"));
        endCol.setCellValueFactory(new PropertyValueFactory<>("end"));
        customerIdCol.setCellValueFactory(new PropertyValueFactory<>("customerId"));

        formatTimeColumn(startCol);
        formatTimeColumn(endCol);
    }

    @FXML
    void onActionBack(ActionEvent event) throws IOException {
        stage = (Stage)((Button)event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/view/CustomerForm.fxml"));
        stage.setScene(new Scene(scene));
        stage.centerOnScreen();
        stage.show();
    }

    /** This method initializes the form and sets the labels with the appropriate customer and calls methods necessary
     * to populate the table. */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        scheduleForLabel.setText(passedCustomer.getCustomerName());
        phoneNumberLabel.setText(passedCustomer.getPhone());
        locationLabel.setText(Division.findDivision(passedCustomer).getDivision() + ", " + Country.findCountry(passedCustomer).getCountry());

        populateTable(passedCustomer.getAllAssociatedAppointments());
        appointmentTableView.getSortOrder().add(startCol);
    }
}
