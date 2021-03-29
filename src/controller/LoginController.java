package controller;

import database.transfer.UserDTO;
import database.business.UserBusinessLogic;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import view.UITools;
import view.Navigator;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controller of the login panel
 */
public class LoginController implements Initializable {
    @FXML
    private PasswordField passwordField;
    @FXML
    private TextField usernameTextField;
    @FXML
    private Button loginButton;
    @FXML
    private Button registerButton;

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
            UserDTO myUser = UserBusinessLogic.getInstance().login(usernameTextField.getText(), passwordField.getText());

            usernameTextField.clear();
            passwordField.clear();

            UITools.showDialog("Logged in as : " + myUser.getFirstName());
        } catch (Exception ex) {
            UITools.showErrorDialog(ex.getLocalizedMessage());
        }
    }

    public void registerButtonAction(ActionEvent actionEvent) {
        Navigator.getInstance().push("register.fxml");
    }

}
