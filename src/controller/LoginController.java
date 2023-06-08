package controller;

import helper.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.*;

import java.io.*;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.ZoneId;

import java.util.*;

import static java.time.format.DateTimeFormatter.ofPattern;

/** This class creates and manages the Login Form. */
public class LoginController implements Initializable {
    Stage stage;
    Parent scene;

    @FXML
    private Button exitButton;
    @FXML
    private Button loginButton;

    @FXML
    private Label invalidPasswordLabel;
    @FXML
    private Label invalidUsernameLabel;
    @FXML
    private Label passwordLabel;
    @FXML
    private Label usernameLabel;
    @FXML
    private Label loginLabel;


    @FXML
    private PasswordField passwordText;
    @FXML
    private TextField userNameText;
    @FXML
    private Label currentLocationLabel;


    @FXML
    void onActionExit(ActionEvent event) {
        System.exit(0);
    }

    /** This form handles login validation and generates a .txt log of all activity in the root folder. The form allows translation to French.
     * @param event The action of clicking the login button
     * */
    @FXML
    void onActionLogin(ActionEvent event) throws IOException{
        invalidUsernameLabel.setVisible(false);
        invalidPasswordLabel.setVisible(false);

        String filename = "login_activity.txt";
        String loginAttemptTimeUTC = LocalDateTime.now().atZone(ZoneId.systemDefault()).withZoneSameInstant(ZoneId.of("UTC")).format(ofPattern("MM/dd/yyyy H:mm z"));

        FileWriter fileWriter = new FileWriter(filename, true);
        PrintWriter outputFile = new PrintWriter(fileWriter);

        if(userNameText.getText().isEmpty()){
            outputFile.println("Invalid login attempt at " + loginAttemptTimeUTC + ".");
            outputFile.close();
            try{
                ResourceBundle rb = ResourceBundle.getBundle("ResourceBundle/ResourceBundle", Locale.getDefault());
                invalidUsernameLabel.setText(rb.getString("Username cannot be empty."));
                invalidUsernameLabel.setVisible(true);
            }
            catch(MissingResourceException e){
                invalidUsernameLabel.setText("Username cannot be empty.");
                invalidUsernameLabel.setVisible(true);
            }
        }
        else{
            if(passwordText.getText().isEmpty()) {
                outputFile.println("Invalid login attempt at " + loginAttemptTimeUTC + ".");
                outputFile.close();
                try {
                    ResourceBundle rb = ResourceBundle.getBundle("ResourceBundle/ResourceBundle", Locale.getDefault());
                    invalidPasswordLabel.setText(rb.getString("Password cannot be empty."));
                    invalidPasswordLabel.setVisible(true);
                }
                catch (MissingResourceException e) {
                    invalidPasswordLabel.setText("Password cannot be empty.");
                    invalidPasswordLabel.setVisible(true);
                }

            }
            else{
                for(User user : User.getAllUsers()){
                    if(Objects.equals(user.getUsername(), userNameText.getText()) & Objects.equals(user.getPassword(), passwordText.getText())){
                        outputFile.println("Login successful for user " + user.getUsername() + " at " + loginAttemptTimeUTC + ".");
                        outputFile.close();

                        User.setCurrentUser(user);

                        Appointment.appointmentSoonCheck();

                        stage = (Stage)((Button)event.getSource()).getScene().getWindow();
                        scene = FXMLLoader.load(getClass().getResource("/view/MainMenuForm.fxml"));
                        stage.setScene(new Scene(scene));
                        stage.centerOnScreen();
                        stage.show();
                    }
                    else if(Objects.equals(user.getUsername(), userNameText.getText()) & !Objects.equals(user.getPassword(), passwordText.getText())){
                        outputFile.println("Login unsuccessful for user " + user.getUsername() + " at " + loginAttemptTimeUTC + ".");
                        outputFile.close();
                    }

                    else{
                        outputFile.println("Login unsuccessful for user " + userNameText.getText() + " at " + loginAttemptTimeUTC + ".");
                        outputFile.close();
                        try {
                            ResourceBundle rb = ResourceBundle.getBundle("ResourceBundle/ResourceBundle", Locale.getDefault());
                            invalidUsernameLabel.setText(rb.getString("The username or password is invalid."));
                            invalidUsernameLabel.setVisible(true);
                        }
                        catch(MissingResourceException e){
                            invalidUsernameLabel.setText("The username or password is invalid.");
                            invalidUsernameLabel.setVisible(true);
                        }
                    }
                }
            }
        }
    }
    /** This variable changes to true after the data has been selected from the database. */
    static public boolean populatedData = false;

    /** This method initializes the form. It sets a label based on the user's location and gets data from the connected MySQL database.
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        currentLocationLabel.setText(String.valueOf(ZoneId.systemDefault()).replace("_"," "));

        try {
            ResourceBundle rb = ResourceBundle.getBundle("ResourceBundle/ResourceBundle", Locale.getDefault());
            loginLabel.setText(rb.getString("Login"));
            usernameLabel.setText(rb.getString("Username:"));
            passwordLabel.setText(rb.getString("Password:"));
            exitButton.setText(rb.getString("Exit"));
            loginButton.setText(rb.getString("Login"));
        }
        catch(MissingResourceException e){
            loginLabel.setText("Login");
            usernameLabel.setText("Username:");
            passwordLabel.setText("Password:");
            exitButton.setText("Exit");
            loginButton.setText("Login");
        }

        if(!populatedData){
            try {
                ContactQuery.select();
                CustomerQuery.select();
                UserQuery.select();
                DivisionQuery.select();
                CountryQuery.select();
                AppointmentQuery.select();
                Appointment.createTimes();
                populatedData = true;

            }
            catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}

