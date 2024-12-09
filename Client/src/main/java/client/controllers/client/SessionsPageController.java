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
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import java.text.SimpleDateFormat;

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

    @Override
    public void initialize() {
        super.initialize();

        sessionsListView.setCellFactory(listView -> new ListCell<>() {
            @Override
            protected void updateItem(Session session, boolean empty) {
                super.updateItem(session, empty);
                if (empty || session == null) {
                    setText(null);
                } else {
                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm");
                    String sessionDate = dateFormat.format(session.getDate());
                    String sessionText = "Дата: " + sessionDate + "\n";
                    setText(sessionText);
                }
            }
        });
    }

    private void loadSessionsDetails() {
        if (film == null || hall == null) return;
        try {
            FindSessionsRequest request = new FindSessionsRequest();
            request.setFilmId(film.getId());
            request.setHallId(hall.getId());

            Response response = call(request);

            if (response instanceof SessionsResponse sessionsResponse) {
                sessionsListView.getItems().clear();
                sessionsListView.getItems().addAll(sessionsResponse.getSessions());
            } else {
                showErrorAlert("Не удалось получить сеансы.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            showErrorAlert("Ошибка при загрузке сеансов.");
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
