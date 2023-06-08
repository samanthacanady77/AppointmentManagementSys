package controller;

import helper.Alerts;
import helper.AppointmentQuery;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.*;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.*;
import java.util.ResourceBundle;

/** This class creates and manages the Modify Appointment Form. */
public class ModifyAppointmentFormController implements Initializable{
    Stage stage;
    Parent scene;

    @FXML
    private TextField appointmentIdText;
    @FXML
    private TextField descriptionText;
    @FXML
    private TextField locationText;
    @FXML
    private TextField startText;
    @FXML
    private TextField endText;
    @FXML
    private TextField titleText;
    @FXML
    private TextField typeText;


    @FXML
    private ComboBox<Contact> contactComboBox;
    @FXML
    private ComboBox<Customer> customerIdComboBox;
    @FXML
    private ComboBox<User> userComboBox;
    @FXML
    private ComboBox<LocalTime> startComboBox;
    @FXML
    private ComboBox<LocalTime> endComboBox;


    @FXML
    private DatePicker datePicker;


    private static Appointment passedAppointment;
    private static int index;

    /** This method receives the data passed from the Appointment Form.
     * @param selectedAppointment The appointment being modified
     * */
    public static void passInfoToModifyAppointmentForm(Appointment selectedAppointment) {
        passedAppointment = selectedAppointment;
        index = Appointment.getAllAppointments().indexOf(passedAppointment);
    }


    @FXML
    void onActionCancel(ActionEvent event) throws IOException {
        stage = (Stage)((Button)event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/view/AppointmentForm.fxml"));
        stage.setScene(new Scene(scene));
        stage.centerOnScreen();
        stage.show();
    }

    /** This method saves the information entered into the form. It does input validation on all the information, along with calling
     * methods that verify appointments do not overlap nor are scheduled outside business hours. The data is sent to a SQL query.
     * @param event The action of clicking the save button
     * */
    @FXML
    void onActionSave(ActionEvent event) throws IOException, SQLException {
        int appointmentId = Integer.parseInt(appointmentIdText.getText());
        String title = String.valueOf(titleText.getText());
        String description = String.valueOf(descriptionText.getText());
        String location = String.valueOf(locationText.getText());
        String type = String.valueOf(typeText.getText());

        Contact contactComboSelection = contactComboBox.getSelectionModel().getSelectedItem();
        Customer customerComboSelection = customerIdComboBox.getSelectionModel().getSelectedItem();
        User userComboBoxSelection = userComboBox.getSelectionModel().getSelectedItem();


        if(title.isBlank() || title.isBlank() || description.isBlank() || location.isBlank() || type.isBlank()) {
            Alerts.errorAlert("You must input a value for every field.");
            return;
        }
        if(contactComboBox.getSelectionModel().getSelectedItem() == null){
            Alerts.errorAlert("You must select a contact.");
            return;
        }
        if(datePicker.getValue() == null) {
            Alerts.errorAlert("You must select a date.");
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
                type, start.withZoneSameInstant(ZoneId.of("UTC")), end.withZoneSameInstant(ZoneId.of("UTC")), customerComboSelection.getCustomerId(), userComboBoxSelection.getUserId());

        if(appointment.getStart().isAfter(appointment.getEnd()) || appointment.getStart().isEqual(appointment.getEnd())){
            Alerts.errorAlert("Appointment start time must be before the appointment end time.");
            return;
        }

        if (!Appointment.appointmentBusinessHoursCheck(appointment)) {
            if (!appointment.appointmentOverlapCheck(appointment)) {
                appointment.updateAppointment(appointment, index);

                Customer.findCustomer(appointment).addAssociatedAppointment(appointment);

                AppointmentQuery.update(appointmentId, title, description, location, contactComboSelection.getContactId(), type,
                        Timestamp.valueOf(start.toLocalDateTime()),
                        Timestamp.valueOf(end.toLocalDateTime()),
                        customerComboSelection.getCustomerId(), userComboBoxSelection.getUserId());


                stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
                scene = FXMLLoader.load(getClass().getResource("/view/AppointmentForm.fxml"));
                stage.setScene(new Scene(scene));
                stage.centerOnScreen();
                stage.show();
            }
        }
    }

    /** This method initializes the form along with populating the combo boxes and the text fields with the data to be modified
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        contactComboBox.setItems(Contact.getAllContacts());
        customerIdComboBox.setItems(Customer.getAllCustomers());
        userComboBox.setItems(User.getAllUsers());
        startComboBox.setItems(Appointment.getAllTimes());
        endComboBox.setItems(Appointment.getAllTimes());

        appointmentIdText.setText(String.valueOf(passedAppointment.getAppointmentId()));
        titleText.setText(passedAppointment.getTitle());
        descriptionText.setText(passedAppointment.getDescription());
        locationText.setText(passedAppointment.getLocation());
        typeText.setText(passedAppointment.getType());


        contactComboBox.setValue(Contact.findContact(passedAppointment));
        customerIdComboBox.setValue(Customer.findCustomer(passedAppointment));
        userComboBox.setValue(User.getCurrentUser().findUser(passedAppointment));
        startComboBox.setValue(passedAppointment.getStart().withZoneSameInstant(ZoneId.systemDefault()).toLocalTime());
        endComboBox.setValue(passedAppointment.getEnd().withZoneSameInstant(ZoneId.systemDefault()).toLocalTime());

        datePicker.setValue(passedAppointment.getStart().withZoneSameInstant(ZoneId.systemDefault()).toLocalDate());

    }

}