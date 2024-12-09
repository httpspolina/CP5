package client.controllers.client;

import client.controllers.AbstractController;
import common.command.Response;
import common.command.client.FindHallsRequest;
import common.command.client.HallsResponse;
import common.model.Film;
import common.model.Hall;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;

public class HallsPageController extends AbstractController {
    @FXML
    public ListView<Hall> hallsListView;
    private Film film;

    public void setFilm(Film film) {
        this.film = film;
        loadHallsDetails();
    }

    @FXML
    public void initialize() {
        super.initialize();

        hallsListView.setCellFactory(listView -> new ListCell<Hall>() {
            @Override
            protected void updateItem(Hall hall, boolean empty) {
                super.updateItem(hall, empty);
                if (empty || hall == null) {
                    setText(null);
                } else {
                    String hallText = "Название: " + hall.getName() + "\n" +
                            "Мест: " + hall.getSeats() + "\n" +
                            "Цена: " + String.format("%.2f", hall.getPrice()) + "BYN";

                    setText(hallText);
                }
            }
        });

        loadHallsDetails();
    }

    private void loadHallsDetails() {
        if (film == null) return;
        try {
            FindHallsRequest request = new FindHallsRequest();
            request.setFilmId(film.getId());

            Response response = call(request);

            if (response instanceof HallsResponse hallsResponse) {
                hallsListView.getItems().clear();
                if (hallsResponse.getHalls().isEmpty()) {
                    hallsListView.getItems().add(new Hall());
                } else {
                    hallsListView.getItems().addAll(hallsResponse.getHalls());
                }
            } else {
                showErrorAlert("Не удалось получить кинозалы.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            showErrorAlert("Ошибка при загрузке кинозалов.");
        }
    }

    public void goToSessions(MouseEvent mouseEvent) {
        Hall selectedHall = hallsListView.getSelectionModel().getSelectedItem();
        if (selectedHall != null) {
            SessionsPageController controller = switchPage("/client/sessions.fxml");
            controller.setFilmAndHall(film, selectedHall);
        }
    }

    public void goBack(ActionEvent actionEvent) {
        FilmDetailsController controller = switchPage("/client/film.fxml");
        controller.setFilm(film);
    }
}
