package controller.subpanels;

import common.LoginToken;
import common.Utils;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import model.Disposable;

import java.net.URL;
import java.util.ResourceBundle;

public class WelcomeController implements Initializable, Disposable {
    @FXML private Label uuidLabel;
    @FXML private Label titleLabel;
    @FXML private Label subTitleLabel;

    private Thread service;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        titleLabel.setText(Utils.getTranslatedString("hello") + " " + LoginToken.CURRENT_LOGIN.get().getFirstName());
        if(LoginToken.isEpidemiologist())
            uuidLabel.setText(Utils.getTranslatedString("your_id_is") + " " + LoginToken.CURRENT_LOGIN.get().getId());

        uuidLabel.setVisible(LoginToken.isEpidemiologist());

        subTitleLabel.setText(Utils.currentFormattedTime());

        initTimeRefreshService();

        LoginToken.CURRENT_LOGIN.addListener((observableValue, userDTO, t1) -> {
            if(t1 != null) {
                titleLabel.setText(
                        Utils.getTranslatedString("hello") + " " + LoginToken.CURRENT_LOGIN.get().getFirstName()
                );

                if(LoginToken.isEpidemiologist())
                    uuidLabel.setText(Utils.getTranslatedString("your_id_is") + " " + LoginToken.CURRENT_LOGIN.get().getId());
                uuidLabel.setVisible(LoginToken.isEpidemiologist());
            }
        });
    }

    /**
     * Initializes the time refresh service
     */
    private void initTimeRefreshService() {
        service = new Thread(() -> {
            while(!service.isInterrupted()) {
                try {
                    //noinspection BusyWait
                    Thread.sleep(1000);

                    Platform.runLater(() -> subTitleLabel.setText(Utils.currentFormattedTime()));
                } catch (InterruptedException e) {
                    System.out.println("Interrupted Service");
                    service.interrupt();
                }
            }
        });

        service.start();
    }

    @Override
    public void dispose() {
        service.interrupt();
    }

    @Override
    public void pause() {
        service.interrupt();
    }

    @Override
    public void resume() {
        service.start();
    }

    @FXML
    private void aboutAction() {
        Dialog dialog = new Dialog();
        dialog.setTitle(Utils.getTranslatedString("about"));
        dialog.setHeaderText(Utils.getTranslatedString("group"));
        dialog.setContentText("""
                IMESTIR Ibrahim (000524358)
                OUDAHYA Ismaïl (000479390)
                BELGADA Naoufal (000479191)"""
        );
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);
        dialog.showAndWait();
    }
}
