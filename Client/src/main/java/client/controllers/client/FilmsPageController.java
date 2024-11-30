package client.controllers.client;

import client.controllers.AbstractController;
import common.command.ErrorResponse;
import common.command.Response;
import common.command.client.FilmsResponse;
import common.command.client.FindAllFilmsRequest;
import common.model.Film;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;

public class FilmsPageController extends AbstractController {
    @FXML
    public ListView<Film> filmsListView;

    @Override
    public void initialize() {
        try {
            super.initialize();
            Response res = call(new FindAllFilmsRequest());
            if (res instanceof FilmsResponse filmsResponse) {
                filmsListView.getItems().addAll(filmsResponse.getFilms());
                return;
            }
            if (res instanceof ErrorResponse r) {
                showErrorAlert(r.getMessage());
                return;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        showErrorAlert("Не удалось загрузить список фильмов.");
    }

    public void goToFilmDetails(MouseEvent mouseEvent) {
        Film selectedFilm = filmsListView.getSelectionModel().getSelectedItem();
        if (selectedFilm != null) {
            FilmDetailsController controller = switchPage("/client/film.fxml");
            controller.setFilm(selectedFilm);
        }
    }
}