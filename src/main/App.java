package main;

import common.Utils;
import javafx.application.Application;
import javafx.stage.Stage;
import view.Navigator;

public class App extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        // Window settings
        stage.setWidth(800);
        stage.setHeight(500);
        stage.setTitle(Utils.getTranslatedString("title"));

        Navigator.getInstance().register(stage, "login.fxml");
    }

    public static void run(String[] args) {
        launch(args);
    }
}
