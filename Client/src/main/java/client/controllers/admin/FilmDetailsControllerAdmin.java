package client.controllers.admin;

import client.controllers.AbstractController;
import common.command.Response;
import common.command.admin.DeleteFilmRequest;
import common.command.client.FilmResponse;
import common.command.client.FindFilmByIdRequest;
import common.model.Film;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextArea;
import javafx.scene.text.Text;

public class FilmDetailsControllerAdmin extends AbstractController {
    @FXML
    private TextArea filmDetailsTextArea;

    private Film film;

    @FXML
    private Text filmTitleText;

    @FXML
    public void initialize() {
        super.initialize();
        filmDetailsTextArea.setEditable(false);
    }

    public void setFilm(Film film) {
        this.film = film;
        loadFilmDetails();
    }

    public void loadFilmDetails() {
        if (film == null) return;
        try {
            FindFilmByIdRequest request = new FindFilmByIdRequest();
            request.setFilmId(film.getId());

            Response response = call(request);

            if (response instanceof FilmResponse filmResponse) {
                String filmDetails = getFilmDetails(filmResponse.getFilm());
                film = filmResponse.getFilm();
                filmDetailsTextArea.setText(filmDetails);

                filmTitleText.setText(film.getTitle());
            } else {
                showErrorAlert("Не удалось получить фильм.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            showErrorAlert("Ошибка при загрузке фильма.");
        }
    }

    private String getFilmDetails(Film film) {
        return "Название: " + film.getTitle() + "\n" +
                "Описание: " + film.getDescription() + "\n" +
                "Жанр: " + film.getGenre() + "\n" +
                "Год: " + film.getYear() + "\n" +
                "Рейтинг: " + Math.round(film.getRating() * 100.0) / 100.0 + "\n";
    }

    public void goBack(ActionEvent actionEvent) {
        switchPage("/admin/films.fxml");
    }

    public void toUpdateFilm(ActionEvent actionEvent) {
        UpdateFilmController controller = switchPage("/admin/update_film.fxml");

        if (controller == null) {
            showErrorAlert("Ошибка загрузки страницы обновления фильма.");
            return;
        }

        controller.setFilm(film);
    }

    public void toDeleteFilm(ActionEvent actionEvent) {
        if (film == null || film.getId() == null) {
            showErrorAlert("Фильм не выбран для удаления.");
            return;
        }

        try {
            DeleteFilmRequest request = new DeleteFilmRequest();
            request.setFilmId(film.getId());
            Response response = call(request);

            if (response instanceof common.command.SuccessResponse) {
                showSuccessAlert("Фильм успешно удален!");
            } else {
                showErrorAlert("Ошибка при удалении фильма.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            showErrorAlert("Произошла ошибка при удалении фильма.");
        }
    }

    private void showSuccessAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Успех");
        alert.setHeaderText(message);

        ButtonType goToFilms = new ButtonType("Перейти к фильмам");
        alert.getButtonTypes().setAll(goToFilms);

        alert.showAndWait().ifPresent(response -> {
            if (response == goToFilms) {
                switchPage("/admin/film.fxml");
                alert.close();
            }
        });
    }
}
