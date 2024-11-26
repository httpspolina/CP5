package client.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;

public class WelcomeController extends BaseController {

    @FXML
    private Label welcomeMessage;

    // Статическая переменная для хранения имени текущего пользователя
    private static String currentUser;

    // Метод для установки текущего имени пользователя (вызывается из другого контроллера)
    public static void setCurrentUser(String username) {
        currentUser = username;
    }

    @FXML
    private void initialize() {
        // Формируем текст приветствия
        if (currentUser != null && !currentUser.isEmpty()) {
            welcomeMessage.setText("Добро пожаловать, " + currentUser + "!");
        } else {
            welcomeMessage.setText("Добро пожаловать!");
        }
    }

    @FXML
    public void onPosterLabelClick(MouseEvent mouseEvent) {
        // Логика перехода на другие страницы, если потребуется
    }

    @FXML
    private void onExitClick() {
        // Очистка сессии перед закрытием
        currentUser = null;

        // Закрытие окна
        Stage stage = (Stage) welcomeMessage.getScene().getWindow();
        stage.close();
    }

    public void onHallsLabelClick(MouseEvent mouseEvent) {
        // Заглушка для обработки нажатия на "Кинозалы"
    }

    public void OnAccountButtonClick(ActionEvent actionEvent) {
        // Заглушка для обработки нажатия на "Профиль"
    }

    public void onEventsLabelClick(MouseEvent mouseEvent) {
        // Заглушка для обработки нажатия на "Мероприятия"
    }

    @FXML
    public void OnGladiator2_filmClick(ActionEvent actionEvent) {
        // Загружаем страницу фильма
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/client/film.fxml"));
            Parent root = loader.load();

            // Получаем доступ к контроллеру FilmController
            FilmController filmController = loader.getController();
            filmController.setFilmDetails("Гладиатор 2",
                    "После того как его дом завоевывают тиранические императоры, возглавляющие Рим, Луций, сын Луциллы и Максимуса, вынужден выйти на арену Колизея и обратиться к своему прошлому, чтобы найти в себе силы вернуть славу Рима его народу.");

            // Меняем сцену на страницу фильма
            Stage currentStage = (Stage) welcomeMessage.getScene().getWindow();
            currentStage.getScene().setRoot(root);
            currentStage.setTitle("Гладиатор 2 - Описание");
        } catch (IOException e) {
            e.printStackTrace();
            showErrorAlert("Ошибка при загрузке страницы фильма.");
        }
    }

    private void showErrorAlert(String errorMessage) {
        // Логика для отображения ошибки
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Ошибка");
        alert.setHeaderText("Что-то пошло не так");
        alert.setContentText(errorMessage);
        alert.showAndWait();
    }
}
