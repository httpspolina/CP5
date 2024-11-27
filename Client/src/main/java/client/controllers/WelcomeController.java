package client.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

public class WelcomeController {

    private static final String DB_URL = "jdbc:mysql://localhost:3306/sample1";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "pc4w7wNR6tehEGY7";

    private static String currentUser;

    // Метод для установки текущего пользователя
    public static void setCurrentUser(String username) {
        currentUser = username;
    }

    private void showErrorAlert(String errorMessage) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Ошибка");
        alert.setHeaderText("Что-то пошло не так");
        alert.setContentText(errorMessage);
        alert.showAndWait();
    }

    // Обработчик перехода на страницу "films.fxml"
    public void toFilmsClick(ActionEvent actionEvent) {
        try {
            // Загружаем новый экран "films.fxml"
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/client/films.fxml"));
            Parent root = loader.load();

            // Получаем текущее окно и меняем сцену
            Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));

            // Устанавливаем заголовок для нового окна
            stage.setTitle("Афиша");

            // Отображаем сцену
            stage.show();
        } catch (IOException e) {
            showErrorAlert("Не удалось загрузить страницу фильмов.");
        }
    }

    // Обработчик перехода назад на страницу "main.fxml"
    public void toBackClick(ActionEvent actionEvent) {
        try {
            // Загружаем начальный экран "main.fxml"
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/client/main.fxml"));
            Parent root = loader.load();

            // Получаем текущее окно и меняем сцену
            Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));

            // Устанавливаем заголовок для начального окна
            stage.setTitle("Главная страница");

            // Отображаем сцену
            stage.show();
        } catch (IOException e) {
            showErrorAlert("Не удалось вернуться на главную страницу.");
        }
    }
}
