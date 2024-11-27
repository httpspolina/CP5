package client;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws IOException {
        // Загружаем FXML файл
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/client/login.fxml"));
        AnchorPane root = loader.load();

        // Создаем сцену и показываем ее
        Scene scene = new Scene(root, 800, 560);
        primaryStage.setTitle("Авторизация/Регистрация");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}