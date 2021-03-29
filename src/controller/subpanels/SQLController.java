package controller.subpanels;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class SQLController implements Initializable {
    @FXML private TableView tableView;
    @FXML private Button confirmButton;
    @FXML private TextField requestTextField;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        confirmButton
                .disableProperty()
                .bind(requestTextField.textProperty().isEmpty());
    }

    public void confirmButtonAction(ActionEvent actionEvent) {
        // TODO
    }
}
