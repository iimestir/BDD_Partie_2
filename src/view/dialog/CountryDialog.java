package view.dialog;

import common.Utils;
import controller.dialog.CountryDialogController;
import controller.dialog.SQLDialogController;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class CountryDialog extends Dialog {
    private static final String FXML_PATH = "/fxml/dialogs/country_dialog.fxml";

    private CountryDialog(String ISO) throws IOException, SQLException {
        setTitle(ISO);
        setResizable(true);

        final FXMLLoader loader = new FXMLLoader(getClass().getResource(FXML_PATH),
                ResourceBundle.getBundle("translation", Utils.CURRENT_LOCALE));

        getDialogPane().setContent(loader.load());

        final CountryDialogController controller = loader.getController();
        controller.setData(ISO);

        getDialogPane().getButtonTypes().addAll(ButtonType.CLOSE);
    }

    public static CountryDialog promptDialog(String ISO) throws IOException, SQLException {
        return new CountryDialog(ISO);
    }
}
