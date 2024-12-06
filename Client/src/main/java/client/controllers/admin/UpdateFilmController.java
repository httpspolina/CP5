package client.controllers.admin;

import client.controllers.AbstractController;
import common.command.Response;
import common.command.SuccessResponse;
import common.command.admin.UpdateFilmRequest;
import common.model.Film;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;

public class UpdateFilmController extends AbstractController {

    @FXML
    public TextField titleField;
    @FXML
    public TextField countryField;
    @FXML
    public TextField yearField;
    @FXML
    public TextField directorField;
    @FXML
    public TextField rolesField;
    @FXML
    public TextField genreField;
    @FXML
    public TextField descriptionField;
    @FXML
    public TextField posterUrlField;

    private Film film;

    public void setFilm(Film film) {
        this.film = film;
        loadFilmDetails();
    }

    private void loadFilmDetails() {
        if (film == null) return;

        titleField.setText(film.getTitle());
        countryField.setText(film.getCountry());
        yearField.setText(String.valueOf(film.getYear()));
        directorField.setText(film.getDirector());
        rolesField.setText(film.getRoles());
        genreField.setText(film.getGenre());
        descriptionField.setText(film.getDescription());
        posterUrlField.setText(film.getPosterUrl());
    }

    public void updateFilm(ActionEvent actionEvent) {
        String newTitle = titleField.getText().trim();
        String newCountry = countryField.getText().trim();
        String newYear = yearField.getText().trim();
        String newDirector = directorField.getText().trim();
        String newRoles = rolesField.getText().trim();
        String newGenre = genreField.getText().trim();
        String newDescription = descriptionField.getText().trim();
        String newPosterUrl = posterUrlField.getText().trim();

        if (newTitle.isEmpty() || newCountry.isEmpty() || newYear.isEmpty() || newDirector.isEmpty() ||
                newRoles.isEmpty() || newGenre.isEmpty() || newDescription.isEmpty() || newPosterUrl.isEmpty()) {
            showErrorAlert("Все поля должны быть заполнены");
            return;
        }

        film.setTitle(newTitle);
        film.setCountry(newCountry);
        film.setYear(Integer.parseInt(newYear));
        film.setDirector(newDirector);
        film.setRoles(newRoles);
        film.setGenre(newGenre);
        film.setDescription(newDescription);
        film.setPosterUrl(newPosterUrl);

        UpdateFilmRequest request = new UpdateFilmRequest();
        request.setFilm(film);

        try {
            Response response = call(request);

            if (response instanceof SuccessResponse) {
                showSuccessAlert("Фильм успешно обновлен!");
            } else {
                showErrorAlert("Не удалось обновить фильм");
            }
        } catch (Exception e) {
            e.printStackTrace();
            showErrorAlert("Произошла ошибка при обновлении фильма.");
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
                switchPage("/admin/films.fxml");
                alert.close();
            }
        });
    }

    public void cancel(ActionEvent actionEvent) {
        switchPage("/admin/film.fxml");
    }
}
