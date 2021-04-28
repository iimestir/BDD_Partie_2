package controller;

import controller.subpanel.SQLController;
import controller.subpanel.WelcomeController;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import model.Disposable;

import java.net.URL;
import java.util.ResourceBundle;

public class MainController implements Initializable, Disposable {
    @FXML private WelcomeController welcomeController;
    @FXML private SQLController sqlController;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    @Override
    public void dispose() {
        welcomeController.dispose();
    }

    @Override
    public void pause() {
        welcomeController.pause();
    }

    @Override
    public void resume() {
        welcomeController.resume();
    }
}
