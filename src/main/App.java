package main;

import common.Utils;
import javafx.application.Application;
import javafx.stage.Stage;
import view.Navigator;

public class App extends Application {
    @Override
    public void start(Stage stage) {
        // Window settings
        stage.setWidth(800);
        stage.setHeight(500);
        stage.setMinWidth(800);
        stage.setMinHeight(500);
        stage.setTitle(Utils.getTranslatedString("title"));
        stage.setOnCloseRequest(event -> {
            Navigator.getInstance().exit();
        });

        Navigator.getInstance().register(stage, "login.fxml");
    }

    public static void run(String[] args) {
        launch(args);
    }
}
