package client.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import java.sql.*;

public class FilmController extends BaseController {

    @FXML
    private Text filmTitle;
    @FXML
    private Text CountryField;
    @FXML
    private Text YearField;
    @FXML
    private Text DirectorField;
    @FXML
    private Text RolesField;
    @FXML
    private Text GenreField;
    @FXML
    private TextArea filmDescription;
    @FXML
    private ImageView filmPoster;  // добавляем ImageView для постера

    private static final String DB_URL = "jdbc:mysql://localhost:3306/sample1"; // Замените на ваш URL
    private static final String DB_USER = "root"; // Замените на вашего пользователя
    private static final String DB_PASSWORD = "pc4w7wNR6tehEGY7"; // Замените на ваш пароль

    public void loadFilmDetails(String title) {
        String query = "SELECT title, country, year, director, roles, genre, description, poster_url FROM films WHERE title = ?";

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, title);  // Устанавливаем параметр названия фильма
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                // Заполнение данных фильма
                filmTitle.setText(rs.getString("title"));
                CountryField.setText(rs.getString("country"));
                YearField.setText(String.valueOf(rs.getInt("year")));
                DirectorField.setText(rs.getString("director"));
                RolesField.setText(rs.getString("roles"));
                GenreField.setText(rs.getString("genre"));
                filmDescription.setText(rs.getString("description"));

                // Получаем URL постера из базы данных
                String posterUrl = rs.getString("poster_url");
                if (posterUrl != null && !posterUrl.isEmpty()) {
                    // Добавляем "/" в начале пути, чтобы корректно загрузить ресурс
                    String resourcePath = "/" + posterUrl;
                    // Загружаем изображение в ImageView
                    Image image = new Image(getClass().getResource(resourcePath).toExternalForm());
                    filmPoster.setImage(image);  // Устанавливаем изображение в ImageView
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            // Обрабатываем случай, если ресурс не найден
            System.err.println("Ошибка: изображение не найдено по пути: " + e.getMessage());
        }
    }
}
