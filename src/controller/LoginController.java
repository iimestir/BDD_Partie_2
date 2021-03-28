package controller;

import database.DAO.UserDAO;
import database.DTO.UserDTO;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import view.Navigator;

import java.net.URL;
import java.sql.SQLException;
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
        // TODO
        try {
            UserDTO user = UserDAO.select(emailTextField.getText(), passwordField.getText());
            System.out.println("User found : " + user.getFirstName());
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void registerButtonAction(ActionEvent actionEvent) {
        Navigator.getInstance().push("register.fxml");
    }

}
