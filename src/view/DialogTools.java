package view;

import common.Utils;
import javafx.scene.control.Alert;

/**
 * Used to show many kind of dialogs
 */
public class DialogTools {

    /**
     * Used to show an error dialog
     *
     * @param errMsg error message
     */
    public static void showErrorDialog(String errMsg) {
        final Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(Utils.getTranslatedString("error"));
        alert.setHeaderText(Utils.getTranslatedString("error_header"));
        alert.setContentText(errMsg);
        alert.showAndWait();
    }

    /**
     * Used to show a regular dialog
     *
     * @param msg dialog message
     */
    public static void showDialog(String msg) {
        final Alert dialog = new Alert(Alert.AlertType.INFORMATION);
        dialog.setTitle(Utils.getTranslatedString("message"));
        dialog.setHeaderText(Utils.getTranslatedString("information"));
        dialog.setContentText(msg);
        dialog.showAndWait();
    }
}
