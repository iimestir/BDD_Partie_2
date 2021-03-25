package view;

import common.Tools;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.*;

/**
 * Singleton class Navigator
 *
 * Used to navigate between scenes
 */
public class Navigator {
    private static final Navigator instance = new Navigator();

    private final Scene scene = new Scene(new Pane());
    private final List<Parent> panels = new ArrayList<>();

    /**
     * Used to prevent any instantiating
     */
    private Navigator() {}

    /**
     * Returns the singleton instance
     * @return singleton instance
     */
    public static Navigator getInstance() { return instance; }

    /**
     * Used to register the navigator local scene to the current main stage
     * @param stage the current main stage
     */
    public void register(Stage stage) {
        stage.setScene(this.scene);
    }

    /**
     * Used to push a new panel to the navigator
     * @param path panel fxml file
     */
    public void push(String path) {
        final FXMLLoader loader = new FXMLLoader(getClass().getResource(path),
                ResourceBundle.getBundle("translation", Tools.currentLocale));

        try {
            final Parent panel = loader.load();

            this.panels.add(panel);
            this.scene.setRoot(panel);
        } catch(IOException ex) {
            System.out.println(ex.getLocalizedMessage());
        }
    }

    /**
     * Used to remove the last panel from the navigator
     */
    public void pop() {
        this.panels.remove(panels.get(panels.size()-1));
        this.scene.setRoot(panels.get(panels.size()-1));
    }

}
