package client;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.stage.Stage;

public class PrimaryStageManager {

    public static final PrimaryStageManager INSTANCE = new PrimaryStageManager();

    private Stage primaryStage;

    private PrimaryStageManager() {
    }

    public synchronized void initialize(Stage primaryStage) {
        if (this.primaryStage != null) return;
        this.primaryStage = primaryStage;
    }

    public synchronized void switchPage(String fxmlFile) throws Exception {
        if (this.primaryStage == null) return;
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
        Parent root = loader.load();
        primaryStage.getScene().setRoot(root);
    }

}
