package client.controllers.client;

import client.controllers.AbstractController;
import common.command.CommonErrorResponse;
import common.command.Response;
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
    }

    public void goBack(ActionEvent actionEvent) { switchPage("/client/main.fxml"); }
}