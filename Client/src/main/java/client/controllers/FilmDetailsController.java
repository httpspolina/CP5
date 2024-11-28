package client.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

import java.io.IOException;

public class FilmDetailsController {

    @FXML
    private TextArea detailsTextArea;

    // Метод для установки деталей фильма
    public void setFilmDetails(String details) {
        detailsTextArea.setText(details); // Устанавливаем текст в TextArea
    }

    // Возврат на предыдущую страницу (к странице списка фильмов)
    public void goBack(ActionEvent event) {
        try {
            // Загружаем FXML файл для страницы фильмов
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/client/films.fxml"));
            Parent root = loader.load();

            // Получаем текущую сцену и заменяем на новую
            Stage stage = (Stage) detailsTextArea.getScene().getWindow();
            stage.setScene(new Scene(root));

            // Устанавливаем новый заголовок для страницы фильмов (например, "Список фильмов")
            stage.setTitle("Список фильмов");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

