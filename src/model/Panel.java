package model;

public enum Panel {
    // Panels
    MAIN("main.fxml"),
    LOGIN("login.fxml"),
    REGISTER("register.fxml");

    private final String fxmlPath;

    Panel(String fxmlPath) {
        this.fxmlPath = fxmlPath;
    }

    public String getPath() {
        return fxmlPath;
    }
}
