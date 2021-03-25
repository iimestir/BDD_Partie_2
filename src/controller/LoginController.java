package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import view.Navigator;

/**
 * Controller of the login panel
 */
public class LoginController {
    @FXML
    private PasswordField passwordField;
    @FXML
    private TextField emailTextField;
    @FXML
    private Button loginButton;
    @FXML
    private Button registerButton;

    public void loginButtonAction(ActionEvent actionEvent) {
        // TODO
    }

    public void registerButtonAction(ActionEvent actionEvent) {
        Navigator.getInstance().push("register.fxml");
    }
}
