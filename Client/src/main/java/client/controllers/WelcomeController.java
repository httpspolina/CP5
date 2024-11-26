package client.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;

public class WelcomeController extends BaseController {

    @FXML
    private Label welcomeMessage;
    @FXML
    private ImageView filmPoster;  // Добавляем ссылку на ImageView постера
    @FXML
    private Text filmTitle;  // Добавляем ссылку на Label для названия фильма

    private static String currentUser;

    private static final String DB_URL = "jdbc:mysql://localhost:3306/sample1";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "pc4w7wNR6tehEGY7";

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

        // Загружаем данные о фильме "Гладиатор 2"
        loadFilmData("Гладиатор 2");
    }

    // Метод для загрузки данных о фильме из базы данных
    private void loadFilmData(String filmTitleText) {
        String query = "SELECT title, poster_url FROM films WHERE title = ?";

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, filmTitleText);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                // Устанавливаем название фильма
                filmTitle.setText(rs.getString("title"));

                // Загружаем и устанавливаем изображение постера
                String posterUrl = rs.getString("poster_url");
                if (posterUrl != null && !posterUrl.isEmpty()) {
                    // Используем путь из базы данных для изображения
                    Image posterImage = new Image("file:C:/Users/Polina/Desktop/untitled/Client/src/main/resources/" + posterUrl);
                    filmPoster.setImage(posterImage);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showErrorAlert("Ошибка при загрузке данных фильма.");
        }
    }

    @FXML
    public void onPosterLabelClick(MouseEvent mouseEvent) {
        // Логика перехода на другие страницы, если потребуется
    }

    @FXML
    private void onExitClick() {
        currentUser = null;
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
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/client/film.fxml"));
            Parent root = loader.load();

            FilmController filmController = loader.getController();
            filmController.loadFilmDetails("Гладиатор 2");  // Загружаем детали фильма

            Stage currentStage = (Stage) welcomeMessage.getScene().getWindow();
            currentStage.getScene().setRoot(root);
            currentStage.setTitle("Гладиатор 2 - Описание");
        } catch (IOException e) {
            e.printStackTrace();
            showErrorAlert("Ошибка при загрузке страницы фильма.");
        }
    }

    private void showErrorAlert(String errorMessage) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Ошибка");
        alert.setHeaderText("Что-то пошло не так");
        alert.setContentText(errorMessage);
        alert.showAndWait();
    }
}
