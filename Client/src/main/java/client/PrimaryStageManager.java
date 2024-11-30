package client;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class PrimaryStageManager {

    public static final PrimaryStageManager INSTANCE = new PrimaryStageManager();

    private Stage primaryStage;

    private PrimaryStageManager() {
    }

    public synchronized void initialize(Stage primaryStage) throws Exception {
        if (this.primaryStage != null) return;
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/client/login.fxml"));
        AnchorPane root = loader.load();
        Scene scene = new Scene(root, 800, 560);
        primaryStage.setScene(scene);
        primaryStage.show();
        this.primaryStage = primaryStage;
    }

    public synchronized <T> T switchPage(String fxmlFile) throws Exception {
        if (this.primaryStage == null) return null;
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
        Parent root = loader.load();
        primaryStage.getScene().setRoot(root);
        return loader.getController();
    }

}
