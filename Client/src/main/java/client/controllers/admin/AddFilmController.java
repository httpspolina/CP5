package client.controllers.admin;

import client.controllers.AbstractController;
import common.command.Response;
import common.command.admin.AddFilmRequest;
import common.model.Film;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;

public class AddFilmController extends AbstractController {
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

    public void addFilm(ActionEvent actionEvent) {
        try {
            String title = titleField.getText();
            String country = countryField.getText();
            String year = yearField.getText();
            String director = directorField.getText();
            String roles = rolesField.getText();
            String genre = genreField.getText();
            String description = descriptionField.getText();
            String posterUrl = posterUrlField.getText();

            if (title.isEmpty() || country.isEmpty() || year.isEmpty() || director.isEmpty()
                    || roles.isEmpty() || genre.isEmpty() || description.isEmpty() || posterUrl.isEmpty()) {
                showErrorAlert("Пожалуйста, заполните все поля.");
                return;
            }

            Film newFilm = new Film();
            newFilm.setTitle(title);
            newFilm.setCountry(country);
            newFilm.setYear(Integer.parseInt(year));
            newFilm.setDirector(director);
            newFilm.setRoles(roles);
            newFilm.setGenre(genre);
            newFilm.setDescription(description);
            newFilm.setPosterUrl(posterUrl);

            AddFilmRequest request = new AddFilmRequest();
            request.setFilm(newFilm);
            Response response = call(request);

            if (response instanceof common.command.SuccessResponse) {
                showSuccessAlert("Фильм успешно добавлен!");
            } else {
                showErrorAlert("Не удалось добавить фильм.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            showErrorAlert("Ошибка при создании фильма.");
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
        switchPage("/admin/films.fxml");
    }
}
