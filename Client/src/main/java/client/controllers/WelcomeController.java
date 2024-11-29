package client.controllers;

import client.ClientConfig;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class WelcomeController {

    private static final String SERVER_ADDRESS = ClientConfig.INSTANCE.getServerHost();
    private static final int SERVER_PORT = ClientConfig.INSTANCE.getServerPort();

    private static String currentUser;
    public Button addFilmButton;
    public Label welcomeMessage;

    @FXML
    private ListView<String> filmListView; // Изменено с TextArea на ListView

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
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/client/films.fxml"));
            Parent root = loader.load();

            // Получаем текущее окно и меняем сцену
            Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Афиша");
            stage.show();

            // После загрузки сцены, загружаем данные фильмов
            WelcomeController controller = loader.getController();
            controller.loadFilms();

        } catch (IOException e) {
            e.printStackTrace();
            showErrorAlert("Не удалось загрузить страницу фильмов.");
        }
    }

    private void loadFilms() {
        try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
             ObjectOutputStream objectOutput = new ObjectOutputStream(socket.getOutputStream());
             ObjectInputStream objectInput = new ObjectInputStream(socket.getInputStream())) {

            // Отправляем запрос на получение фильмов
            objectOutput.writeObject("GET_FILMS");

            // Получаем список фильмов (предположим, данные приходят как строки, разделенные новой строкой)
            String filmsData = (String) objectInput.readObject();

            // Преобразуем данные в список и добавляем в ListView
            List<String> films = Stream.of(filmsData.split("\n"))
                    .collect(Collectors.toList());
            filmListView.getItems().setAll(films);

            // Устанавливаем обработчик клика по элементам списка
            filmListView.setOnMouseClicked(event -> {
                String selectedFilm = filmListView.getSelectionModel().getSelectedItem();
                if (selectedFilm != null) {
                    System.out.println("Selected film: " + selectedFilm);
                    // Логика перехода на страницу с подробностями о фильме
                    openFilmDetails(selectedFilm);
                }
            });

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            showErrorAlert("Ошибка при получении данных с сервера.");
        }
    }

    private void openFilmDetails(String filmTitle) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/client/filmDetails.fxml"));
            Parent root = loader.load();

            // Получаем контроллер новой страницы
            FilmDetailsController controller = loader.getController();

            // Получаем подробную информацию о фильме
            String filmDetails = getFilmDetails(filmTitle);

            // Устанавливаем информацию о фильме в TextArea
            controller.setFilmDetails(filmDetails);

            // Название фильма извлекаем из начала строки или другим способом
            String filmTitleOnly = extractFilmTitle(filmDetails);

            // Создаем новое окно
            Stage stage = (Stage) filmListView.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle(filmTitleOnly); // Только название фильма в заголовке окна
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
            showErrorAlert("Не удалось загрузить страницу с подробностями фильма.");
        }
    }

    // Вспомогательный метод для извлечения названия фильма
    private String extractFilmTitle(String filmDetails) {
        // Предполагаем, что название фильма находится после "Title: " и до первой запятой
        if (filmDetails.startsWith("Title: ")) {
            int startIndex = "Title: ".length();
            int endIndex = filmDetails.indexOf(",", startIndex);
            if (endIndex != -1) {
                return filmDetails.substring(startIndex, endIndex).trim();
            }
        }
        // Если структура данных изменилась, возвращаем всю строку на случай ошибки
        return filmDetails;
    }

    private String getFilmDetails(String filmTitle) {
        return filmTitle;
    }


    // Обработчик перехода назад на страницу "main.fxml"
    public void toBackClick(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/client/main.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Главная страница");
            stage.show();
        } catch (IOException e) {
            showErrorAlert("Не удалось вернуться на главную страницу.");
        }
    }

    public void toAddFilmClick(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/admin/addFilm.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage(); // Создаем новое окно
            stage.setScene(new Scene(root));
            stage.setTitle("Добавить фильм");
            stage.show();

        } catch (IOException e) {
            showErrorAlert("Не удалось загрузить окно добавления фильма.");
            e.printStackTrace();
        }
    }
}
