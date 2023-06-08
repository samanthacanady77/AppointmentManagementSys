package controller;

import helper.Alerts;
import helper.AppointmentQuery;


import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.*;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.*;
import java.util.ResourceBundle;

/** This class creates and manages the Add Appointment Form. */
public class AddAppointmentFormController implements Initializable {
    Stage stage;
    Parent scene;

    @FXML
    private TextField appointmentIdText;
    @FXML
    private TextField descriptionText;
    @FXML
    private TextField locationText;

    @FXML
    private TextField titleText;
    @FXML
    private TextField typeText;


    @FXML
    private ComboBox<Contact> contactComboBox;
    @FXML
    private ComboBox<Customer> customerIdComboBox;
    @FXML
    private ComboBox<LocalTime> startComboBox;
    @FXML
    private ComboBox<LocalTime> endComboBox;
    @FXML
    private ComboBox<User> userComboBox;


    @FXML
    private DatePicker datePicker;

    /** This method generates unique appointment IDs. */
    private int appointmentIdGenerator(){
        int appointmentId = 1;

        for(Appointment appointment: Appointment.getAllAppointments()){
            if(appointment.getAppointmentId() == appointmentId){
                appointmentId++;
            }
        }
        return appointmentId;
    }

    /** This method cancels adding an appointment.
     * @param actionEvent The action of clicking the cancel button
     * */
    @FXML
    void onActionCancel(javafx.event.ActionEvent actionEvent) throws IOException {
        stage = (Stage)((Button)actionEvent.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/view/AppointmentForm.fxml"));
        stage.setScene(new Scene(scene));
        stage.centerOnScreen();
        stage.show();
    }

    /** This method saves the information entered into the form. It does input validation on all the information, along with calling
     * methods that verify appointments do not overlap nor are scheduled outside business hours. The data is sent to a SQL query.
     * @param actionEvent The action of clicking the save button
     * */
    @FXML
    void onActionSave(javafx.event.ActionEvent actionEvent) throws IOException, SQLException {
        int appointmentId = Integer.parseInt(appointmentIdText.getText());
        String title = String.valueOf(titleText.getText());
        String description = String.valueOf(descriptionText.getText());
        String location = String.valueOf(locationText.getText());
        String type = String.valueOf(typeText.getText());


        Contact contactComboSelection = contactComboBox.getSelectionModel().getSelectedItem();
        Customer customerComboSelection = customerIdComboBox.getSelectionModel().getSelectedItem();
        User userComboSelection = userComboBox.getSelectionModel().getSelectedItem();

        if(title.isBlank() || title.isBlank() || description.isBlank() || location.isBlank() || type.isBlank()) {
            Alerts.errorAlert("You must input a value for every field.");
            return;
        }
        if(contactComboBox.getSelectionModel().getSelectedItem() == null){
            Alerts.errorAlert("You must select a contact.");
            return;
        }
        if(datePicker.getValue() == null) {
            Alerts.errorAlert("You must select a date");
            return;
        }
        if(startComboBox.getSelectionModel().getSelectedItem() == null || endComboBox.getSelectionModel().getSelectedItem() == null){
            Alerts.errorAlert("You must select a start and end time.");
            return;
        }
        if(customerIdComboBox.getSelectionModel().getSelectedItem() == null) {
            Alerts.errorAlert("You must select a customer.");
            return;
        }

        if(userComboBox.getSelectionModel().getSelectedItem() == null) {
            Alerts.errorAlert("You must select a user.");
            return;
        }


        ZonedDateTime start = (startComboBox.getSelectionModel().getSelectedItem().atDate(datePicker.getValue()).atZone(ZoneId.systemDefault()));
        ZonedDateTime end = (endComboBox.getSelectionModel().getSelectedItem().atDate(datePicker.getValue()).atZone(ZoneId.systemDefault()));


        Appointment appointment = new Appointment(appointmentId, title, description, location, contactComboSelection.getContactId(),
                type, start.withZoneSameInstant(ZoneId.of("UTC")), end.withZoneSameInstant(ZoneId.of("UTC")), customerComboSelection.getCustomerId(),
                userComboSelection.getUserId());


        if(appointment.getStart().isAfter(appointment.getEnd()) || appointment.getStart().isEqual(appointment.getEnd())){
            Alerts.errorAlert("Appointment start time must be before the appointment end time.");
            return;
        }

        if(!Appointment.appointmentBusinessHoursCheck(appointment)) {
            if (!appointment.appointmentOverlapCheck(appointment)) {

                appointment.addAppointment(appointment);

                Customer.findCustomer(appointment).addAssociatedAppointment(appointment);
                Contact.findContact(appointment).addAssociatedAppointment(appointment);

                AppointmentQuery.insert(appointmentId, title, description, location, contactComboSelection.getContactId(), type,
                        Timestamp.valueOf(start.toLocalDateTime()), Timestamp.valueOf(end.toLocalDateTime()),
                        customerComboSelection.getCustomerId(), userComboSelection.getUserId());

                stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
                scene = FXMLLoader.load(getClass().getResource("/view/AppointmentForm.fxml"));
                stage.setScene(new Scene(scene));
                stage.centerOnScreen();
                stage.show();
            }
        }
    }

    /** This method initializes the form and populates the combo boxes along with the appointment ID. */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        appointmentIdText.setText(String.valueOf(appointmentIdGenerator()));

        contactComboBox.setItems(Contact.getAllContacts());

        customerIdComboBox.setItems(Customer.getAllCustomers());
        userComboBox.setItems(User.getAllUsers());


        startComboBox.setItems(Appointment.getAllTimes());
        endComboBox.setItems(Appointment.getAllTimes());


    }


}