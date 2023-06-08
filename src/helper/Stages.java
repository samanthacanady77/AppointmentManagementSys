package helper;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

/** This class allows easier changing of scenes. */
public class Stages {
    /** This scene allows scenes to be changed by simple entering a link as a string.
     * @param sceneString The address of the scene
     * @param event
     * @throws IOException
     */
    public static void changeStages(String sceneString, ActionEvent event) throws IOException {
        Stage stage;
        Parent scene;

        stage = (Stage)((Button)event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(Stages.class.getResource(sceneString));
        stage.setScene(new Scene(scene));
        stage.centerOnScreen();
        stage.show();
    }
}
