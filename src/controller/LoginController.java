package controller;

import common.LoginToken;
import database.business.UserBusinessLogic;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import model.Disposable;
import model.Panel;
import view.Navigator;
import view.UITools;

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

    @FXML
    private void loginButtonAction() {
        try {
            LoginToken.CURRENT_LOGIN.set(
                    UserBusinessLogic.getInstance()
                    .login(usernameTextField.getText(), passwordField.getText())
            );

            Navigator.getInstance().push(Panel.MAIN);
        } catch (Exception ex) {
            UITools.showErrorDialog(ex.getLocalizedMessage());
        }
    }

    @FXML
    private void registerButtonAction() {
        Navigator.getInstance().push(Panel.REGISTER);
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
