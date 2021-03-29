package controller;

import controller.subpanels.WelcomeController;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.VBox;
import model.Disposable;

import java.net.URL;
import java.util.ResourceBundle;

public class MainController implements Initializable, Disposable {
    @FXML private VBox welcome;
    @FXML private WelcomeController welcomeController;

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
