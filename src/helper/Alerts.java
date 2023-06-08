package helper;

import javafx.scene.control.Alert;
import javafx.scene.layout.Region;

/** This class allows for easier generating of alerts. */
public class Alerts {

    /** This method allows creation of an error alert.
     * @param message The text to be added to the alert
     */
    public static void errorAlert(String message){
        Alert alert2 = new Alert(Alert.AlertType.ERROR);
        alert2.setTitle("Error");
        alert2.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
        alert2.setContentText(message);
        alert2.show();
    }

    /** This method allows creation of an information alert.
     * @param message The text to be added to the alert
     */
    public static void informationAlert(String message){
        Alert alert2 = new Alert(Alert.AlertType.INFORMATION);
        alert2.setTitle("Information");
        alert2.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
        alert2.setContentText(message);
        alert2.show();
    }

}
