package view.dialog;

import common.Utils;
import controller.dialog.SQLDialogController;
import database.transfer.DTO;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import model.DTOType;
import model.SQLRequest;

import java.io.IOException;
import java.util.ResourceBundle;

public class SQLDialog extends Dialog<DTO> {
    private static final String FXML_PATH = "/fxml/dialogs/sql_dialog.fxml";

    private SQLDialog(SQLRequest sqlType, DTOType dto) throws IOException, IllegalArgumentException, NumberFormatException {
        setTitle(sqlType.toString());

        final FXMLLoader loader = new FXMLLoader(getClass().getResource(FXML_PATH),
                ResourceBundle.getBundle("translation", Utils.CURRENT_LOCALE));

        getDialogPane().setContent(loader.load());

        final SQLDialogController controller = loader.getController();
        controller.setData(sqlType, dto);

        final ButtonType ok = new ButtonType(Utils.getTranslatedString("confirm"));

        setResultConverter(p -> p != ok ? null : controller.getDTO());

        getDialogPane().getButtonTypes().addAll(ok, ButtonType.CANCEL);
    }

    public static SQLDialog promptDialog(SQLRequest sqlType, DTOType dto) throws IOException, IllegalArgumentException, NumberFormatException {
        return new SQLDialog(sqlType, dto);
    }
}
