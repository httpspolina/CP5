package client.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

public abstract class BaseController {

    @FXML
    private Label PosterLabel;
    @FXML
    private Label HallsLabel;
    @FXML
    private Label EventsLabel;
    @FXML
    private Button AccountButton;

    @FXML
    protected void onPosterLabelClick(MouseEvent mouseEvent) {
        try {
            // Убедитесь, что путь к файлу правильный
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/client/welcome.fxml"));
            AnchorPane welcomePage = loader.load(); // Загружаем страницу

            // Получаем ссылку на текущую сцену и обновляем ее
            Stage stage = (Stage) PosterLabel.getScene().getWindow();  // Получаем текущее окно
            Scene scene = new Scene(welcomePage);  // Создаем новую сцену с загруженным FXML
            stage.setScene(scene);  // Устанавливаем новую сцену
            stage.show();  // Показываем новое окно

        } catch (IOException e) {
            e.printStackTrace();  // Логируем ошибку, если не удается загрузить страницу
        }
    }

    // Метод для обработки нажатия на "Кинозалы"
    @FXML
    protected void onHallsLabelClick(MouseEvent mouseEvent) {
        System.out.println("Переход к кинозалам");
        // Реализовать логику перехода к кинозалам
    }

    // Метод для обработки нажатия на "Мероприятия"
    @FXML
    protected void onEventsLabelClick(MouseEvent mouseEvent) {
        System.out.println("Переход к мероприятиям");
        // Реализовать логику перехода к мероприятиям
    }

    @FXML
    protected void OnAccountButtonClick(ActionEvent actionEvent) {
    }
}