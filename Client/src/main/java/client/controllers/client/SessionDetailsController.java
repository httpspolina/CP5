package client.controllers.client;

import client.controllers.AbstractController;
import common.command.Response;
import common.command.client.FindSessionByIdRequest;
import common.command.client.SessionResponse;
import common.model.Film;
import common.model.Hall;
import common.model.Session;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.Background;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

public class SessionDetailsController extends AbstractController {
    public GridPane grid;

    @FXML
    private Text sessionFilmNameField;

    @FXML
    private Text sessionHallNameField;

    @FXML
    private Text sessionDateField;

    @FXML
    private Text sessionSeatsField;

    @FXML
    private Text sessionsPriceField;

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

        sessionFilmNameField.setText(film.getTitle());
        sessionHallNameField.setText(hall.getName());
        sessionDateField.setText(session.getDate().toString());
        sessionSeatsField.setText(String.valueOf(session.getSeats()));

        try {
            FindSessionByIdRequest request = new FindSessionByIdRequest();
            request.setSessionId(session.getId());

            Response response = call(request);

            if (response instanceof SessionResponse sessionResponse) {
                this.session = sessionResponse.getSession();

                sessionSeatsField.setText(
                        selectedSeats.stream()
                                .map(String::valueOf)
                                .collect(Collectors.joining(", "))
                );

                int totalPrice = (int) (selectedSeats.size() * hall.getPrice());
                sessionsPriceField.setText(totalPrice + " руб.");

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
                                updateSelectedSeatsAndPrice();
                            } else {
                                selectedSeats.remove(seatIndex);
                                seat.setBackground(Background.fill(Color.WHITE));
                                updateSelectedSeatsAndPrice();
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

    private void updateSelectedSeatsAndPrice() {
        sessionSeatsField.setText(
                selectedSeats.stream()
                        .map(String::valueOf)
                        .collect(Collectors.joining(", "))
        );
        int totalPrice = (int) (selectedSeats.size() * hall.getPrice());
        sessionsPriceField.setText(totalPrice + " руб.");
    }

    public void goBack(ActionEvent actionEvent) {
        SessionsPageController controller = switchPage("/client/sessions.fxml");
        controller.setFilmAndHall(film, hall);
    }

    public void placeOrder(ActionEvent actionEvent) {
        try {
            if (selectedSeats.isEmpty()) {
                showErrorAlert("Выберите хотя бы одно место для оформления заказа.");
                return;
            }

            PaymentController controller = switchPage("/client/payment.fxml");
            if (controller == null) {
                showErrorAlert("Ошибка загрузки страницы оплаты.");
                return;
            }

            controller.setOrderDetails(film, hall, session, selectedSeats);

        } catch (Exception e) {
            e.printStackTrace();
            showErrorAlert("Ошибка при переходе к оплате.");
        }
    }
}
