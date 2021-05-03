package controller.subpanel;

import common.LoginToken;
import common.Utils;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import model.Disposable;

import java.net.URL;
import java.util.ResourceBundle;

public class WelcomeController implements Initializable, Disposable {
    @FXML private Label titleLabel;
    @FXML private Label subTitleLabel;

    private Thread service;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        titleLabel.setText(Utils.getTranslatedString("hello") + " " + LoginToken.CURRENT_LOGIN.get().getFirstName());
        subTitleLabel.setText(Utils.currentFormattedTime());

        initTimeRefreshService();
    }

    /**
     * Initializes the time refresh service
     */
    private void initTimeRefreshService() {
        service = new Thread(() -> {
            while(!service.isInterrupted()) {
                try {
                    Thread.sleep(1000);

                    Platform.runLater(() -> {
                        subTitleLabel.setText(Utils.currentFormattedTime());
                    });
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
}
