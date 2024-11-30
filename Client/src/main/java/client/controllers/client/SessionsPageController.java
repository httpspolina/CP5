package client.controllers.client;

import client.controllers.AbstractController;
import common.command.Response;
import common.command.client.FindSessionsRequest;
import common.command.client.SessionsResponse;
import common.model.Film;
import common.model.Hall;
import common.model.Session;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;

public class SessionsPageController extends AbstractController {
    @FXML
    public ListView<Session> sessionsListView;
    private Film film;
    private Hall hall;

    public void setFilmAndHall(Film film, Hall hall) {
        this.film = film;
        this.hall = hall;
        loadSessionsDetails();
    }

    private void loadSessionsDetails() {
        if (film == null || hall == null) return;
        try {
            FindSessionsRequest request = new FindSessionsRequest();
            request.setFilmId(film.getId());
            request.setHallId(hall.getId());

            Response response = call(request);

            if (response instanceof SessionsResponse sessionsResponse) {
                sessionsListView.getItems().addAll(sessionsResponse.getSessions());
            } else {
                showErrorAlert("Не удалось получить кинозалы.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            showErrorAlert("Ошибка при загрузке кинозалов.");
        }
    }


    public void goBack(ActionEvent actionEvent) {
        HallsPageController controller = switchPage("/client/halls.fxml");
        controller.setFilm(film);
    }

    public void goToSession(MouseEvent mouseEvent) {
        Session selectedSession = sessionsListView.getSelectionModel().getSelectedItem();
        if (selectedSession != null) {
            SessionDetailsController controller = switchPage("/client/session.fxml");
            controller.setFilmAndHallAndSession(film, hall, selectedSession);
        }
    }
}