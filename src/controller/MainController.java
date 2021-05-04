package controller;

import controller.subpanel.ProfileController;
import controller.subpanel.SQLController;
import controller.subpanel.WelcomeController;
import controller.subpanel.WorldMapController;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import model.Disposable;

import java.net.URL;
import java.util.ResourceBundle;

public class MainController implements Initializable, Disposable {
    @FXML private Tab mapTab;

    @FXML private WelcomeController welcomeController;
    @FXML private SQLController sqlController;
    @FXML private ProfileController profileController;
    @FXML private WorldMapController mapController;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        mapTab.selectedProperty().addListener((observableValue, aBoolean, t1) -> {
            if(t1)
                mapController.updateMap();
        });
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
