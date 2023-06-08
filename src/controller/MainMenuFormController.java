package controller;

import helper.Stages;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/** This class creates and manages the Main Menu Form and allows navigation to all other forms. */
public class MainMenuFormController implements Initializable {

    @FXML
    private Button reportsButton;
    @FXML
    private Button appointmentsButton;
    @FXML
    private Button customersButton;
    @FXML
    private Button logoutButton;

    /** This method initializes the form and uses lambdas to switch between scenes using their assigned buttons. The lambdas make what
     * otherwise would be four separate methods more concise and easier to read.
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        customersButton.setOnAction((event) -> {
            try {
                Stages.changeStages("/view/CustomerForm.fxml", event);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        appointmentsButton.setOnAction((event) -> {
            try {
                Stages.changeStages("/view/AppointmentForm.fxml", event);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        reportsButton.setOnAction((event) -> {
            try {
                Stages.changeStages("/view/ReportsForm.fxml", event);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        logoutButton.setOnAction((event) -> {
            try {
                Stages.changeStages("/view/Login.fxml", event);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });


    }
}
