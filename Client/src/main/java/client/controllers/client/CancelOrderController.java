package client.controllers.client;

import client.controllers.AbstractController;
import common.command.CommonErrorResponse;
import common.command.Response;
import common.command.SuccessResponse;
import common.command.client.*;
import common.model.*;
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


public class CancelOrderController extends AbstractController {
    @FXML
    private ComboBox<Order> ordersComboBox;

    @FXML
    public void initialize() {
        super.initialize();
        loadOrders();
    }

    private void loadOrders() {
        try {
            FindOrdersRequest request = new FindOrdersRequest();
            Response response = call(request);

            if (response instanceof OrdersResponse findOrdersResponse) {
                List<Order> orders = findOrdersResponse.getOrders();

                orders = orders.stream()
                        .filter(order -> order.getStatus() == OrderStatus.PAYED)
                        .toList();

                ordersComboBox.setItems(FXCollections.observableArrayList(orders));
            } else {
                showErrorAlert("Не удалось загрузить заказы.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            showErrorAlert("Ошибка при загрузке заказов.");
        }
    }

    public void cancelOrder(ActionEvent actionEvent) {
        Order selectedOrder = ordersComboBox.getValue();
        if (selectedOrder == null) {
            showErrorAlert("Пожалуйста, выберите заказ.");
            return;
        }

        try {
            // Создаём запрос для обновления статуса заказа
            UpdateOrderStatusRequest request = new UpdateOrderStatusRequest();
            request.setOrderId(selectedOrder.getId());
            request.setStatus(OrderStatus.CANCELED);

            // Отправляем запрос на сервер
            Response response = call(request);
            if (response instanceof SuccessResponse) {
                showSuccessAlert("Заказ успешно отменен.");
                loadOrders();  // Перезагружаем заказы для обновления состояния
            } else {
                showErrorAlert("Не удалось отменить заказ.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            showErrorAlert("Ошибка при отмене заказа.");
        }
    }

    private void showSuccessAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Успех");
        alert.setHeaderText(message);

        ButtonType goToProfileButton = new ButtonType("Перейти в личный кабинет");
        alert.getButtonTypes().setAll(goToProfileButton);

        alert.showAndWait().ifPresent(response -> {
            if (response == goToProfileButton) {
                switchPage("/client/profile.fxml");
                alert.close();
            }
        });
    }

    public void goBack(ActionEvent actionEvent) { switchPage("/client/main.fxml"); }
}