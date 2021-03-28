package controller;

import database.DTO.UserDTO;
import database.business.UserLogic;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import view.DialogTools;
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
    private TextField emailTextField;
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
                .bind(emailTextField.textProperty().isEmpty()
                        .or(passwordField.textProperty().isEmpty())
                );
    }

    public void loginButtonAction(ActionEvent actionEvent) {
        try {
            UserDTO myUser = UserLogic.getInstance().login(emailTextField.getText(), passwordField.getText());

            emailTextField.clear();
            passwordField.clear();

            DialogTools.showDialog("Logged in as : " + myUser.getFirstName());
        } catch (Exception ex) {
            DialogTools.showErrorDialog(ex.getLocalizedMessage());
        }
    }

    public void registerButtonAction(ActionEvent actionEvent) {
        Navigator.getInstance().push("register.fxml");
    }

}
