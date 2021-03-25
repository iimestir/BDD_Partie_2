package controller;

import at.favre.lib.crypto.bcrypt.BCrypt;
import common.Utils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import model.AccountType;
import view.Navigator;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Register panel controller
 */
public class RegisterController implements Initializable {
    @FXML
    private TextField centerTextField;
    @FXML
    private TextField servicePhoneTextField;
    @FXML
    private TextField firstNameTextField;
    @FXML
    private TextField lastNameTextField;
    @FXML
    private TextField streetTextField;
    @FXML
    private TextField doorTextField;
    @FXML
    private TextField cityTextField;
    @FXML
    private TextField zipTextField;
    @FXML
    private Button createAccountButton;
    @FXML
    private ComboBox<AccountType> accountTypeComboBox;
    @FXML
    private PasswordField passwordField;
    @FXML
    private TextField usernameTextField;

    /**
     * Used to setup the panel before showing it
     * @param url not used here
     * @param resourceBundle not used here
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        accountTypeComboBox.getItems().addAll(AccountType.class.getEnumConstants());
        setEpidemiologistForms(false);
        initAccountComboBoxListener();
        initRegisterButtonBinding();
    }

    /**
     * Used to bind conditions to the register button
     */
    private void initRegisterButtonBinding() {
        createAccountButton
                .disableProperty()
                .bind(usernameTextField.textProperty().isEmpty()
                        .or(passwordField.textProperty().isEmpty())
                        .or(firstNameTextField.textProperty().isEmpty())
                        .or(lastNameTextField.textProperty().isEmpty())
                        .or(streetTextField.textProperty().isEmpty())
                        .or(doorTextField.textProperty().isEmpty())
                        .or(cityTextField.textProperty().isEmpty())
                        .or(zipTextField.textProperty().isEmpty())
                        .or(accountTypeComboBox.valueProperty().isNull())
                        .or((centerTextField.textProperty().isEmpty()
                                .or(servicePhoneTextField.textProperty().isEmpty()))
                                .and(accountTypeComboBox.valueProperty().isEqualTo(AccountType.EPIDEMIOLOGIST)))
                );
    }

    /**
     * Used to add a listener to the combobox
     */
    private void initAccountComboBoxListener() {
        accountTypeComboBox.valueProperty().addListener((observable, oldValue, newValue) -> {
            boolean epidemiologist = accountTypeComboBox.getSelectionModel()
                    .getSelectedItem().equals(AccountType.EPIDEMIOLOGIST);

            setEpidemiologistForms(epidemiologist);
        });
    }

    /**
     * Used to disable or enable the epidemiologist forms
     * @param enable boolean
     */
    private void setEpidemiologistForms(boolean enable) {
        centerTextField.setDisable(!enable);
        servicePhoneTextField.setDisable(!enable);

        if(!enable) {
            centerTextField.setText("");
            servicePhoneTextField.setText("");
        }
    }

    /**
     * Used to create an account when the button is clicked
     * @param actionEvent not used here
     */
    public void createAccountButtonAction(ActionEvent actionEvent) {
        // TODO
        String username = Utils.getHashedString(usernameTextField.getText());
        String password = Utils.getHashedString(passwordField.getText());

        BCrypt.Result result = BCrypt.verifyer().verify(usernameTextField.getText().toCharArray(),
                password);
        System.out.println(result.verified);
    }

    /**
     * Used to return from the register menu when the cancel button is clicked
     * @param actionEvent not used here
     */
    public void cancelButtonAction(ActionEvent actionEvent) {
        Navigator.getInstance().pop();
    }
}