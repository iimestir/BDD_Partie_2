package controller;

import controller.subpanels.*;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Tab;
import model.Disposable;

import java.net.URL;
import java.util.ResourceBundle;

public class MainController implements Initializable, Disposable {
    @FXML private Tab mapTab;
    @FXML private Tab dashboardTab;

    @FXML private WelcomeController welcomeController;
    @FXML private SQLController sqlController;
    @FXML private ProfileController profileController;
    @FXML private WorldMapController mapController;
    @FXML private DashboardController dashboardController;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        mapTab.selectedProperty().addListener((observableValue, aBoolean, t1) -> {
            if(t1)
                mapController.updateMap();
        });

        dashboardTab.selectedProperty().addListener((observableValue, aBoolean, t1) -> {
            if(t1) {
                dashboardController.loadDatas();
            }
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
