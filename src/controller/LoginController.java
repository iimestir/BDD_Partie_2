package controller;

import common.LoginToken;
import database.transfer.UserDTO;
import database.business.UserBusinessLogic;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import model.Disposable;
import view.UITools;
import view.Navigator;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controller of the login panel
 */
public class LoginController implements Initializable, Disposable {
    @FXML private PasswordField passwordField;
    @FXML private TextField usernameTextField;
    @FXML private Button loginButton;
    @FXML private Button registerButton;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initLoginButtonBinding();
    }

    private void initLoginButtonBinding() {
        loginButton
                .disableProperty()
                .bind(usernameTextField.textProperty().isEmpty()
                        .or(passwordField.textProperty().isEmpty())
                );
    }

    public void loginButtonAction(ActionEvent actionEvent) {
        try {
            LoginToken.CURRENT_USER = UserBusinessLogic.getInstance()
                    .login(usernameTextField.getText(), passwordField.getText());

            Navigator.getInstance().push("main.fxml");
        } catch (Exception ex) {
            UITools.showErrorDialog(ex.getLocalizedMessage());
        }
    }

    public void registerButtonAction(ActionEvent actionEvent) {
        Navigator.getInstance().push("register.fxml");
    }

    @Override
    public void dispose() {
        usernameTextField.clear();
        passwordField.clear();
    }

    @Override
    public void pause() {
        usernameTextField.clear();
        passwordField.clear();
    }

    @Override
    public void resume() { }
}
