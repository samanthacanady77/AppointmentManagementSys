package main;

import helper.JDBC;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


import java.io.*;
import java.sql.SQLException;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/view/login.fxml"));

        try {
            ResourceBundle rb = ResourceBundle.getBundle("ResourceBundle/ResourceBundle", Locale.getDefault());
            stage.setTitle(rb.getString("Appointment Management System"));
        }
        catch(MissingResourceException e){
            stage.setTitle("Appointment Management System");
        }

        stage.setScene(new Scene(root, 400, 500));
        stage.show();
        stage.centerOnScreen();
        stage.setResizable(false);


    }


    public static void main(String[] args) throws SQLException, IOException {
        JDBC.openConnection();
        //Locale.setDefault(new Locale("fr"));


        launch(args);

        JDBC.closeConnection();
    }

}