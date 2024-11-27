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
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class WelcomeController {

    private static final String DB_URL = "jdbc:mysql://localhost:3306/sample1";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "pc4w7wNR6tehEGY7";

    private static final String SERVER_ADDRESS = "localhost";
    private static final int SERVER_PORT = 12345;

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
    public void toFilmsClick(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/client/films.fxml"));
        Parent root = loader.load();

        // Получаем текущее окно и меняем сцену
        Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));

        // Устанавливаем заголовок для нового окна
        stage.setTitle("Афиша");

        // Отображаем сцену
        stage.show();
        try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);

             ObjectOutputStream objectOutput = new ObjectOutputStream(socket.getOutputStream());
             ObjectInputStream objectInput = new ObjectInputStream(socket.getInputStream())) {

            // Отправляем запрос на получение фильмов
            objectOutput.writeObject("GET_FILMS");

            // Получаем список фильмов
            String filmsData = (String) objectInput.readObject();

            // Выводим данные в консоль
            System.out.println(filmsData);

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            showErrorAlert("Ошибка при получении данных с сервера.");
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
