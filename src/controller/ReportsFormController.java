package controller;

import helper.AppointmentQuery;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.Appointment;
import model.Contact;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

/** This class creates and manages the Reports Form. */
public class ReportsFormController implements Initializable {
    Stage stage;
    Parent scene;

    @FXML
    private TextArea reportData;
    @FXML
    private ComboBox<Contact> contactComboBox;

    @FXML
    private Button contactEmailList;

    /** This method shows a report detailing the type and totals of appointments in the database.
     * @param actionEvent The action of clicking the show appointment totals button
     * @throws IOException
     * @throws SQLException
     */
    public void onActionShowTypeAppointments(ActionEvent actionEvent) throws IOException, SQLException {

        contactComboBox.getSelectionModel().clearSelection();

        Appointment.getAllTypeTotals().clear();
        AppointmentQuery.totalByType();

        StringBuilder s = new StringBuilder();

        for(String string : Appointment.getAllTypeTotals()){
            s.append(string).append("\n");
        }
        reportData.setText("Total Appointments:\n\n" + s);
    }

    /** This method shows all the schedules for the contacts in the database using a combo box to select the customer.
     * @param actionEvent The action of selecting a name from the combo box
     * @throws IOException
     */
    public void onActionShowContactSchedules(ActionEvent actionEvent) throws IOException {
        try {
            Contact contact = contactComboBox.getSelectionModel().getSelectedItem();
            contact.setAllAssociatedAppointments(contact);

            StringBuilder s = new StringBuilder();

            for (Appointment appointment : contact.getAllAssociatedAppointments()) {
                s.append("Appointment ID: ").append(appointment.getAppointmentId()).append(" | ").append(appointment.getTitle()).append("\n")
                        .append(appointment.getStart().withZoneSameInstant(ZoneId.systemDefault()).format(DateTimeFormatter.ofPattern("MM/dd/yyyy H:mm")))
                        .append(" - ")
                        .append(appointment.getEnd().withZoneSameInstant(ZoneId.systemDefault()).format(DateTimeFormatter.ofPattern("MM/dd/yyyy H:mm"))).append("\n")
                        .append(appointment.getType()).append(" | ").append(appointment.getDescription()).append("\n")
                        .append("Customer ID: ").append(appointment.getCustomerId()).append("\n\n");
            }

            reportData.setText(contact.getContactName() + "'s Schedule: \n\n" + s);
        }
        catch(NullPointerException e){

        }
    }

    public void onActionBack(ActionEvent actionEvent) throws IOException {
        stage = (Stage)((Button)actionEvent.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/view/MainMenuForm.fxml"));
        stage.setScene(new Scene(scene));
        stage.centerOnScreen();
        stage.show();
    }

    /** This method initializes the form and populates the contact combo box. It includes a lambda that retrieves the contact emailing list
     * report. The lambda offers a more concise way to add an otherwise short action to the button.
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        contactComboBox.setItems(Contact.getAllContacts());

        contactEmailList.setOnAction(e -> {
            StringBuilder s = new StringBuilder();

            for(Contact contact : Contact.getAllContacts()){
                s.append(contact.getContactName()).append(": ").append(contact.getEmail()).append("\n");
            }
            reportData.setText(String.valueOf(s));
        });

    }
}