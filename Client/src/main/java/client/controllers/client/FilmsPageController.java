package client.controllers.client;

import client.controllers.AbstractController;
import common.command.ErrorResponse;
import common.command.Response;
import common.command.client.FilmResponse;
import common.command.client.FilmsResponse;
import common.command.client.FindAllFilmsRequest;
import common.command.client.FindFilmByTitleRequest;
import common.model.Film;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

public class FilmsPageController extends AbstractController {
    @FXML
    public ListView<Film> filmsListView;
    @FXML
    public TextField filmSearchField;

    @Override
    public void initialize() {
        loadAllFilms();
    }

    public void loadAllFilms() {
        try {
            Response res = call(new FindAllFilmsRequest());
            if (res instanceof FilmsResponse filmsResponse) {
                filmsListView.getItems().clear();
                filmsListView.getItems().addAll(filmsResponse.getFilms());
            } else if (res instanceof ErrorResponse errorResponse) {
                showErrorAlert(errorResponse.getMessage());
            } else {
                showErrorAlert("Не удалось загрузить список фильмов.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            showErrorAlert("Ошибка при загрузке списка фильмов.");
        }
    }

    public void goToFilmDetails(MouseEvent mouseEvent) {
        Film selectedFilm = filmsListView.getSelectionModel().getSelectedItem();
        if (selectedFilm != null) {
            FilmDetailsController controller = switchPage("/client/film.fxml");
            controller.setFilm(selectedFilm);
        }
    }

    public void onProfileButton(ActionEvent actionEvent) { switchPage("/client/profile.fxml"); }

    public void toSearchFilm(ActionEvent actionEvent) {
        String searchQuery = filmSearchField.getText().trim();
        if (searchQuery.isEmpty()) {
            showErrorAlert("Пожалуйста, введите название фильма.");
            return;
        }

        try {
            FindFilmByTitleRequest request = new FindFilmByTitleRequest();
            request.setTitle(searchQuery);

            Response response = call(request);

            if (response instanceof FilmResponse filmResponse) {
                filmsListView.getItems().clear();
                filmsListView.getItems().add(filmResponse.getFilm());
            } else if (response instanceof ErrorResponse errorResponse) {
                showErrorAlert(errorResponse.getMessage());
            } else {
                showErrorAlert("Не удалось найти фильм.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            showErrorAlert("Ошибка при поиске фильма.");
        }
    }

    public void showAllFilms(ActionEvent actionEvent) {
        loadAllFilms();
    }
}