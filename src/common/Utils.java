package common;

import javafx.scene.control.Alert;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Used for every miscellaneous things
 */
public final class Utils {
    public static final Locale CURRENT_LOCALE = Locale.getDefault();

    /**
     * Used to get the translated string corresponding to a key from the resource bundle
     * @param key String key
     * @return the translated string
     */
    public static String getTranslatedString(String key) {
        return ResourceBundle.getBundle("translation", CURRENT_LOCALE).getString(key);
    }

    /**
     * Used to show an error dialog
     *
     * @param errMsg error message
     */
    public static void showErrorDialog(String errMsg) {
        final Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(getTranslatedString("error"));
        alert.setHeaderText(getTranslatedString("error_header"));
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
        dialog.setTitle(getTranslatedString("message"));
        dialog.setHeaderText(getTranslatedString("information"));
        dialog.setContentText(msg);
        dialog.showAndWait();
    }
}
