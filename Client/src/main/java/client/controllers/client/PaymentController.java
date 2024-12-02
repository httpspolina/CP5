package client.controllers.client;

import client.controllers.AbstractController;
import common.command.Response;
import common.command.client.CreateOrderRequest;
import common.command.client.FindMyPaymentMethodsRequest;
import common.command.client.PaymentMethodsResponse;
import common.command.client.CreatePaymentMethodRequest;
import common.model.Film;
import common.model.Hall;
import common.model.PaymentMethod;
import common.model.Session;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.sql.Date;
import java.util.List;
import java.util.Set;

public class PaymentController extends AbstractController {

    @FXML
    private ComboBox<PaymentMethod> paymentMethodsComboBox;

    @FXML
    private VBox cardDetailsVBox;

    @FXML
    private TextField cardNumberField;

    @FXML
    private TextField cardholderField;

    @FXML
    private TextField expiresAtField;

    @FXML
    private TextField cvvField;

    private Film film;
    private Hall hall;
    private Session session;
    private Set<Integer> selectedSeats;
    private PaymentMethod paymentMethod;

    @FXML
    public void initialize() {
        super.initialize();
        System.out.println("paymentMethodsComboBox: " + paymentMethodsComboBox);
        loadPaymentMethods();
    }

    private void loadPaymentMethods() {
        try {
            FindMyPaymentMethodsRequest request = new FindMyPaymentMethodsRequest();
            Response response = call(request);

            if (response instanceof PaymentMethodsResponse paymentMethodsResponse) {
                List<PaymentMethod> paymentMethods = paymentMethodsResponse.getPaymentMethods();
                paymentMethodsComboBox.setItems(FXCollections.observableArrayList(paymentMethods));
            } else {
                showErrorAlert("Не удалось загрузить способы оплаты.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            showErrorAlert("Ошибка при загрузке способов оплаты.");
        }
    }

    public void setOrderDetails(Film film, Hall hall, Session session, Set<Integer> selectedSeats) {
        this.film = film;
        this.hall = hall;
        this.session = session;
        this.selectedSeats = selectedSeats;
    }

    public void buyTickets(ActionEvent actionEvent) {
        try {
            PaymentMethod selectedPaymentMethod = paymentMethodsComboBox.getValue();

            if (selectedPaymentMethod == null) {
                showErrorAlert("Пожалуйста, выберите способ оплаты.");
                return;
            }

            if (selectedSeats == null || selectedSeats.isEmpty()) {
                showErrorAlert("Нет выбранных мест для заказа.");
                return;
            }

            CreateOrderRequest request = new CreateOrderRequest();
            request.setSessionId(session.getId());
            request.setPaymentMethodId(selectedPaymentMethod.getId());
            request.setSelectedSeatIndexes(selectedSeats);

            Response response = call(request);

            if (response instanceof common.command.SuccessResponse) {
                showSuccessAlert("Заказ успешно оформлен!");
            } else {
                showErrorAlert("Не удалось оформить заказ.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            showErrorAlert("Ошибка при оформлении заказа.");
        }
    }

    public void addCard(ActionEvent actionEvent) {
        cardDetailsVBox.setVisible(true);
    }

    public void saveCard(ActionEvent actionEvent) {
        try {
            String cardNumber = cardNumberField.getText();
            String cardholder = cardholderField.getText();
            String expiresAt = expiresAtField.getText();
            String cvv = cvvField.getText();

            if (cardNumber.isEmpty() || cardholder.isEmpty() || expiresAt.isEmpty() || cvv.isEmpty()) {
                showErrorAlert("Пожалуйста, заполните все поля.");
                return;
            }

            PaymentMethod newPaymentMethod = new PaymentMethod();
            newPaymentMethod.setCardNumber(cardNumber);
            newPaymentMethod.setCardholder(cardholder);
            newPaymentMethod.setExpiresAt(Date.valueOf(expiresAt));
            newPaymentMethod.setCvv(cvv);

            CreatePaymentMethodRequest request = new CreatePaymentMethodRequest(newPaymentMethod);
            Response response = call(request);

            if (response instanceof common.command.SuccessResponse) {
                loadPaymentMethods();
                cardDetailsVBox.setVisible(false);
                showSuccessAlert("Карта успешно добавлена!");
            } else {
                showErrorAlert("Не удалось добавить карту.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            showErrorAlert("Ошибка при сохранении карты.");
        }
    }

    private void showSuccessAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Успех");
        alert.setHeaderText(message);

        ButtonType goToProfileButton = new ButtonType("Перейти к билетам в личном кабинете");
        alert.getButtonTypes().setAll(goToProfileButton);

        alert.showAndWait().ifPresent(response -> {
            if (response == goToProfileButton) {
                switchPage("/client/profile.fxml");
                alert.close();
            }
        });
    }
}
