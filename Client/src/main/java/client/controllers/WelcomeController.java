package client.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.image.Image;

import java.io.IOException;

public class WelcomeController extends BaseController {
    @FXML
    public Label welcomeMessage;
    @FXML
    private TableView<Film> posterTable;
    @FXML
    private TableColumn<Film, ImageView> posterColumn;
    @FXML
    private TableColumn<Film, String> titleColumn;
    @FXML
    private TableColumn<Film, Button> infoColumn;


    private static final String DB_URL = "jdbc:mysql://localhost:3306/sample1";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "pc4w7wNR6tehEGY7";

    private static String currentUser;  // Статическая переменная для хранения текущего пользователя.

    @FXML
    private void initialize() {
        // Инициализация колонок таблицы
        posterColumn.setCellValueFactory(new PropertyValueFactory<>("poster"));
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        infoColumn.setCellValueFactory(new PropertyValueFactory<>("infoButton"));

        if (currentUser != null) {
            welcomeMessage.setText("Добро пожаловать, " + currentUser);  // Используем текущее имя пользователя
        } else {
            welcomeMessage.setText("Добро пожаловать, незнакомец");
        }
        loadFilmData();
    }

    // Метод для загрузки данных о фильмах
    private void loadFilmData() {
        // Для примера, добавляем фильм "Гладиатор 2"
        Film gladiator2 = new Film(
                new ImageView(new Image(getClass().getResource("/client/img/10049693-706x1024.jpg").toExternalForm())),
                "Гладиатор 2",
                new Button("Подробнее")
        );

        // Обработчик нажатия на кнопку "Подробнее"
        gladiator2.getInfoButton().setOnAction(event -> onFilmInfoClick(gladiator2.getTitle()));

        // Добавляем фильм в таблицу
        posterTable.getItems().add(gladiator2);
    }

    // Метод для установки текущего пользователя
    public static void setCurrentUser(String username) {
        currentUser = username;
    }

    // Метод для обработки клика по кнопке "Подробнее"
    private void onFilmInfoClick(String filmTitle) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/client/film.fxml"));
            Parent root = loader.load();

            FilmController filmController = loader.getController();
            filmController.loadFilmDetails(filmTitle);  // Загружаем детали фильма

            Stage currentStage = (Stage) posterTable.getScene().getWindow();
            currentStage.getScene().setRoot(root);
            currentStage.setTitle(filmTitle + " - Описание");
        } catch (IOException e) {
            e.printStackTrace();
            showErrorAlert("Ошибка при загрузке страницы фильма.");
        }
    }

    // Метод для отображения ошибки
    private void showErrorAlert(String errorMessage) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Ошибка");
        alert.setHeaderText("Что-то пошло не так");
        alert.setContentText(errorMessage);
        alert.showAndWait();
    }

    // Класс для представления данных фильма
    public static class Film {
        private ImageView poster;
        private String title;
        private Button infoButton;

        public Film(ImageView poster, String title, Button infoButton) {
            this.poster = poster;
            this.title = title;
            this.infoButton = infoButton;
        }

        public ImageView getPoster() {
            return poster;
        }

        public String getTitle() {
            return title;
        }

        public Button getInfoButton() {
            return infoButton;
        }
    }
}
