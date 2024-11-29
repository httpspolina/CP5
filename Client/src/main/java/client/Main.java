package client;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        PrimaryStageManager.INSTANCE.initialize(primaryStage);

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/client/login.fxml"));
        AnchorPane root = loader.load();

        Scene scene = new Scene(root, 800, 560);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}