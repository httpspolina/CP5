package client.controllers.client;

import client.controllers.AbstractController;
import common.command.Response;
import common.command.client.FindSessionByIdRequest;
import common.command.client.SessionResponse;
import common.model.Film;
import common.model.Hall;
import common.model.Session;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.layout.Background;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;

import java.util.Set;
import java.util.TreeSet;

public class SessionDetailsController extends AbstractController {
    public GridPane grid;

    private Film film;
    private Hall hall;
    private Session session;

    private Set<Integer> selectedSeats = new TreeSet<>();

    public void setFilmAndHallAndSession(Film film, Hall hall, Session session) {
        this.film = film;
        this.hall = hall;
        this.session = session;
        loadSessionDetails();
    }

    private void loadSessionDetails() {
        if (film == null || hall == null || session == null) return;
        try {
            FindSessionByIdRequest request = new FindSessionByIdRequest();
            request.setSessionId(session.getId());

            Response response = call(request);

            if (response instanceof SessionResponse sessionResponse) {
                this.session = sessionResponse.getSession();
                for (int rowIndex = 0; rowIndex < session.getSeats() / 10; rowIndex++) {
                    for (int columnIndex = 0; columnIndex < 10; columnIndex++) {
                        var seatIndex = 10 * rowIndex + columnIndex + 1;
                        Button seat = new Button(String.valueOf(seatIndex));
                        seat.setPrefWidth(50.0);
                        grid.add(seat, columnIndex, rowIndex);

                        if (session.getOccupiedSeats().contains(seatIndex)) {
                            seat.setBackground(Background.fill(Color.GRAY));
                            continue;
                        }
                        seat.setBackground(Background.fill(Color.WHITE));
                        seat.setOnMouseClicked(event -> {
                            if (!selectedSeats.contains(seatIndex)) {
                                selectedSeats.add(seatIndex);
                                seat.setBackground(Background.fill(Color.GREEN));
                            } else {
                                selectedSeats.remove(seatIndex);
                                seat.setBackground(Background.fill(Color.WHITE));
                            }
                        });
                    }
                }
            } else {
                showErrorAlert("Не удалось получить кинозалы.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            showErrorAlert("Ошибка при загрузке кинозалов.");
        }
    }

    public void goBack(ActionEvent actionEvent) {
        SessionsPageController controller = switchPage("/client/sessions.fxml");
        controller.setFilmAndHall(film, hall);
    }

    public void placeOrder(ActionEvent actionEvent) {
        System.out.println(selectedSeats);
        // ...
    }
}
