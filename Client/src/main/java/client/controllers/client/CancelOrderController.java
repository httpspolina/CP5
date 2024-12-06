package client.controllers.client;

import client.controllers.AbstractController;
import common.command.Response;
import common.command.SuccessResponse;
import common.command.client.FindOrdersRequest;
import common.command.client.OrdersResponse;
import common.command.client.UpdateOrderStatusRequest;
import common.model.Order;
import common.model.OrderStatus;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;

import java.util.List;


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
            UpdateOrderStatusRequest request = new UpdateOrderStatusRequest();
            request.setOrderId(selectedOrder.getId());
            request.setStatus(OrderStatus.CANCELED);

            Response response = call(request);
            if (response instanceof SuccessResponse) {
                showSuccessAlert("Заказ успешно отменен.");
                loadOrders();
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

    public void goBack(ActionEvent actionEvent) {
        switchPage("/client/main.fxml");
    }
}