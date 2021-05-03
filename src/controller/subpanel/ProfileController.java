package controller.subpanel;

import common.LoginToken;
import common.Utils;
import database.business.EpidemiologistBusinessLogic;
import database.business.UserBusinessLogic;
import database.transfer.EpidemiologistDTO;
import database.transfer.UserDTO;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import model.AccountType;
import view.Navigator;
import view.UITools;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class ProfileController implements Initializable {
    @FXML private PasswordField passwordField;
    @FXML private ComboBox<AccountType> accountTypeComboBox;
    @FXML private TextField firstNameTextField;
    @FXML private TextField lastNameTextField;
    @FXML private TextField streetTextField;
    @FXML private TextField doorTextField;
    @FXML private TextField cityTextField;
    @FXML private TextField zipTextField;
    @FXML private TextField centerTextField;
    @FXML private TextField servicePhoneTextField;
    @FXML private Button confirmButton;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        accountTypeComboBox.getItems().addAll(AccountType.class.getEnumConstants());
        setEpidemiologistForms(false);
        UITools.setNumericField(doorTextField);

        initAccountComboBoxListener();
        initConfirmButtonBinding();
        initForm();

        LoginToken.CURRENT_LOGIN.addListener((observableValue, userDTO, t1) -> {
            if(t1 != null)
                initForm();
        });
    }

    /**
     * Used to bind conditions to the confirm button
     */
    private void initConfirmButtonBinding() {
        confirmButton
                .disableProperty()
                .bind(firstNameTextField.textProperty().isEmpty()
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
     * Pre-fill the form
     */
    private void initForm() {
        boolean isEpidemiologist = LoginToken.isEpidemiologist();

        accountTypeComboBox.setValue(isEpidemiologist ? AccountType.EPIDEMIOLOGIST : AccountType.USER);
        accountTypeComboBox.setDisable(true);

        passwordField.setText("");
        firstNameTextField.setText(LoginToken.CURRENT_LOGIN.get().getFirstName());
        lastNameTextField.setText(LoginToken.CURRENT_LOGIN.get().getLastName());
        streetTextField.setText(LoginToken.CURRENT_LOGIN.get().getStreet());
        doorTextField.setText(LoginToken.CURRENT_LOGIN.get().getDoorNumber().toString());
        cityTextField.setText(LoginToken.CURRENT_LOGIN.get().getCity());
        zipTextField.setText(LoginToken.CURRENT_LOGIN.get().getZipCode());
        if(isEpidemiologist) {
            centerTextField.setText(((EpidemiologistDTO) LoginToken.CURRENT_LOGIN.get()).getCenter());
            servicePhoneTextField.setText(((EpidemiologistDTO) LoginToken.CURRENT_LOGIN.get()).getServiceNumber());
        }

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
     *
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

    @FXML
    public void confirmButtonAction(ActionEvent actionEvent) {
        String password = passwordField.getText();

        if(password != null && !password.equals("")
                && Utils.scorePassword(passwordField.getText()) < 9) {
            UITools.showErrorDialog(Utils.getTranslatedString("password_error_message"));
            return;
        }

        String firstname = firstNameTextField.getText();
        String lastname = lastNameTextField.getText();
        String street = streetTextField.getText();
        int door_number = Integer.parseInt(doorTextField.getText());
        String city = cityTextField.getText();
        String zip = zipTextField.getText();
        String center = centerTextField.getText();
        String phone = servicePhoneTextField.getText();

        try {
            if(LoginToken.isEpidemiologist()) {
                EpidemiologistDTO oldUser = ((EpidemiologistDTO) LoginToken.CURRENT_LOGIN.get());
                EpidemiologistDTO updatedUser = new EpidemiologistDTO(
                        oldUser.getId(),
                        firstname,
                        lastname,
                        street,
                        door_number,
                        city,
                        zip,
                        center,
                        phone
                );

                EpidemiologistBusinessLogic.getInstance().update(oldUser, updatedUser);
                LoginToken.CURRENT_LOGIN.set(updatedUser);
            } else {
                UserDTO oldUser = LoginToken.CURRENT_LOGIN.get();
                UserDTO updatedUser = new UserDTO(
                        oldUser.getId(),
                        firstname,
                        lastname,
                        street,
                        door_number,
                        city,
                        zip
                );

                EpidemiologistBusinessLogic.getInstance().update(oldUser, updatedUser);
                LoginToken.CURRENT_LOGIN.set(updatedUser);
            }

            if(password != null && !password.equals(""))
                UserBusinessLogic.getInstance().updatePassword(password);
        } catch (IllegalArgumentException | SQLException ex) {
            UITools.showErrorDialog(ex.getLocalizedMessage());
        }
    }

    @FXML
    public void disconnectButtonAction(ActionEvent actionEvent) {
        Navigator.getInstance().pop();
        LoginToken.logOff();
    }
}
