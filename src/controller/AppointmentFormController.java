package controller;

import helper.Alerts;
import helper.AppointmentQuery;
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
import javafx.scene.layout.Region;
import javafx.stage.Stage;
import model.Appointment;
import model.Contact;
import model.Customer;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.WeekFields;
import java.util.*;

/** This class creates and manages the Appointment Form. */
public class AppointmentFormController implements Initializable {
    Stage stage;
    Parent scene;

    @FXML
    private TableView<Appointment> appointmentTableView;

    @FXML
    private TableColumn<Appointment, Integer> appointmentIdCol;
    @FXML
    private TableColumn<Appointment, Integer> contactIdCol;
    @FXML
    private TableColumn<Appointment, Integer> customerIdCol;
    @FXML
    private TableColumn<Appointment, String> descriptionCol;
    @FXML
    private TableColumn<Appointment, String> locationCol;
    @FXML
    private TableColumn<Appointment, ZonedDateTime> startCol;
    @FXML
    private TableColumn<Appointment, ZonedDateTime> endCol;
    @FXML
    private TableColumn<Appointment, String> titleCol;
    @FXML
    private TableColumn<Appointment, String> typeCol;
    @FXML
    private TableColumn<Appointment, Integer> userIdCol;


    /** This method formats the time information in the associated columns for easier readability.
     * @param columnName The table column being formatted
     * */
    // Solution found on https://stackoverflow.com/questions/47484280/format-of-date-in-the-javafx-tableview
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
        locationCol.setCellValueFactory(new PropertyValueFactory<>("location"));
        contactIdCol.setCellValueFactory(new PropertyValueFactory<>("contactId"));
        typeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
        startCol.setCellValueFactory(new PropertyValueFactory<>("start"));
        endCol.setCellValueFactory(new PropertyValueFactory<>("end"));
        customerIdCol.setCellValueFactory(new PropertyValueFactory<>("customerId"));
        userIdCol.setCellValueFactory(new PropertyValueFactory<>("userId"));

        formatTimeColumn(startCol);
        formatTimeColumn(endCol);

        contactIdCol.setCellFactory((TableColumn<Appointment, Integer> column) -> new TableCell<>() {
            @Override
            protected void updateItem(Integer item, boolean empty) {
                super.updateItem(item, empty);

                if (item == null || empty) {
                    setText(null);
                } else {
                    Contact contact  = Contact.findContact(item);
                    setText(contact.getContactName() + " (ID: " + contact.getContactId() + ")");
                }
            }
        });
        customerIdCol.setCellFactory((TableColumn<Appointment, Integer> column) -> new TableCell<>() {
            @Override
            protected void updateItem(Integer item, boolean empty) {
                super.updateItem(item, empty);


                if (item == null || empty) {
                    setText(null);
                } else {
                    Customer customer = Customer.findCustomer(item);
                    setText(customer.getCustomerName() + " (ID: " + customer.getCustomerId() + ")");
                }
            }
        });
    }

    @FXML
    void onActionAdd(ActionEvent event) throws IOException {
        stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/view/AddAppointmentForm.fxml"));
        stage.setScene(new Scene(scene));
        stage.centerOnScreen();
        stage.show();
    }

    /** This method takes a user selected appointment and passes the data to the Modify Appointment Form.
     * @param event The action of clicking the modify button
     * */
    @FXML
    void onActionModify(ActionEvent event) throws IOException {
        Appointment selectedAppointment = appointmentTableView.getSelectionModel().getSelectedItem();

            if (selectedAppointment == null) {
                Alerts.errorAlert("You must select an appointment to be modified.");
            }
            else {
                ModifyAppointmentFormController.passInfoToModifyAppointmentForm(selectedAppointment);

                stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
                scene = FXMLLoader.load(getClass().getResource("/view/ModifyAppointmentForm.fxml"));
                stage.setScene(new Scene(scene));
                stage.centerOnScreen();
                stage.show();
            }
        }

    /** This method deletes a user selected appointment. It confirms cancellation and sends the data to a SQL query.
     * @param event The action of clicking the delete button
     * @throws SQLException
     */
    @FXML
    void onActionDelete(ActionEvent event) throws SQLException {
        Appointment selectedAppointment = appointmentTableView.getSelectionModel().getSelectedItem();

            if (selectedAppointment == null) {
                Alerts.errorAlert("You must select an appointment to be canceled.");
            }

            else {
                Alert alert1 = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to cancel this appointment?\n(" +
                        selectedAppointment.getType() + ", Appt. ID: " + selectedAppointment.getAppointmentId() + " with " +
                        Customer.findCustomer(selectedAppointment).getCustomerName() + ")");
                alert1.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
                Optional<ButtonType> result = alert1.showAndWait();

                if (result.isPresent() && result.get() == ButtonType.OK) {

                    selectedAppointment.deleteAppointment(selectedAppointment);
                    Customer.findCustomer(selectedAppointment).deleteAssociatedAppointment(selectedAppointment);
                    Contact.findContact(selectedAppointment).deleteAssociatedAppointment(selectedAppointment);

                    AppointmentQuery.delete(selectedAppointment.getAppointmentId());
                }
            }
    }

    @FXML
    void onActionBack(ActionEvent event) throws IOException {
        stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/view/MainMenuForm.fxml"));
        stage.setScene(new Scene(scene));
        stage.centerOnScreen();
        stage.show();
    }

    /** This method changes the table view to show all appointments by selecting a radio button.
     * @param event The action of selecting the all radio button
     */
    @FXML
    void onActionChangeToAll(ActionEvent event) {
        populateTable(Appointment.getAllAppointments());

    }

    /** This method changes the table view to show the current week's appointments by selecting a radio button.
     * @param event The action of selecting the all radio button
     */
    @FXML
    void onActionChangeToWeek(ActionEvent event) {
        ObservableList<Appointment> weekAppointments = FXCollections.observableArrayList();

        for (Appointment appointment : Appointment.getAllAppointments()) {
            if (appointment.getStart().withZoneSameInstant(ZoneId.systemDefault()).toLocalDateTime().getYear() == LocalDateTime.now().getYear()) {
                if (appointment.getStart().withZoneSameInstant(ZoneId.systemDefault()).toLocalDateTime().getMonthValue() == LocalDateTime.now().getMonthValue())
                    if (appointment.getStart().toLocalDateTime().get(WeekFields.of(Locale.getDefault()).weekOfYear()) == LocalDateTime.now().get(WeekFields.of(Locale.getDefault()).weekOfYear())) {
                        weekAppointments.add(appointment);
                    }
            }
        }
        populateTable(weekAppointments);
    }

    /** This method changes the table view to show this month's appointments by selecting a radio button.
     * @param event The action of selecting the all radio button
     */
    @FXML
    void onActionChangeToMonth(ActionEvent event) {
        ObservableList<Appointment> monthAppointments = FXCollections.observableArrayList();

        for (Appointment appointment : Appointment.getAllAppointments()) {
            if (appointment.getStart().withZoneSameInstant(ZoneId.systemDefault()).toLocalDateTime().getYear() == LocalDateTime.now().getYear()) {
                if (appointment.getStart().withZoneSameInstant(ZoneId.systemDefault()).toLocalDateTime().getMonthValue() == LocalDateTime.now().getMonthValue())
                    monthAppointments.add(appointment);
            }
            populateTable(monthAppointments);
        }
    }

    /** This method initializes the form and populates the table view with all appointments. */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        populateTable(Appointment.getAllAppointments());

    }
}