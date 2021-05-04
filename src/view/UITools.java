package view;

import common.Utils;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.util.converter.IntegerStringConverter;

import java.util.function.UnaryOperator;

/**
 * Used to show many kind of dialogs
 */
public final class UITools {
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

    /**
     * Used to limit a text field input to numeric
     * @param textField the text field
     */
    public static void setNumericField(TextField textField) {
        UnaryOperator<TextFormatter.Change> integerFilter = change -> {
            String newText = change.getControlNewText();
            if (newText.matches("-?([1-9][0-9]*)?")) {
                return change;
            }
            return null;
        };

        textField.setTextFormatter(new TextFormatter<>(new IntegerStringConverter(), null, integerFilter));
    }
}
