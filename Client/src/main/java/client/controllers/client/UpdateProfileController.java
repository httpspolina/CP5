package client.controllers.client;

import client.CurrentUser;
import client.controllers.AbstractController;
import common.command.Response;
import common.command.client.ClientResponse;
import common.command.client.FindOrdersRequest;
import common.command.client.OrdersResponse;
import common.command.client.UpdateClientRequest;
import common.model.Client;
import common.model.Order;
import common.model.PaymentMethod;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;

import java.util.List;

public class UpdateProfileController extends AbstractController {
    Client currentUser = (Client) CurrentUser.CURRENT_USER;

    @FXML
    private TextArea nameField;
    @FXML
    private TextArea emailField;
    @FXML
    private TextArea phoneField;
    private PaymentMethod paymentMethod;

    @FXML
    private TextArea ordersTextArea;
    private Order order;

    @Override
    public void initialize() {
        super.initialize();
        nameField.setText(currentUser.getName());
        emailField.setText(currentUser.getEmail());
        phoneField.setText(currentUser.getPhone());
        ordersTextArea.setEditable(false);
        loadOrders();
    }

    public void loadOrders() {
        try {
            FindOrdersRequest request = new FindOrdersRequest();

            Response response = call(request);

            if (response instanceof OrdersResponse ordersResponse) {
                List<Order> orders = ordersResponse.getOrders();

                StringBuilder ordersText = new StringBuilder();
                for (Order order : orders) {
                    ordersText.append("ID заказа: ").append(order.getId()).append("\n")
                            .append("Статус: ").append(order.getStatus()).append("\n")
                            .append("Клиент ID: ").append(order.getClientId()).append("\n")
                            .append("Метод оплаты ID: ").append(order.getPaymentMethodId()).append("\n")
                            .append("Сессия ID: ").append(order.getSessionId()).append("\n")
                            .append("Место: ").append(order.getSeatIndex()).append("\n")
                            .append("Дата создания: ").append(order.getCreatedAt()).append("\n\n");
                }

                ordersTextArea.setText(ordersText.toString());
            } else {
                showAlert("Ошибка", "Не удалось загрузить заказы.", Alert.AlertType.ERROR);
            }
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Ошибка", "Произошла ошибка при загрузке заказов.", Alert.AlertType.ERROR);
        }
    }

    public void onProfileButton(ActionEvent actionEvent) {
        switchPage("/client/profile.fxml");
    }

    public void updateProfile(ActionEvent actionEvent) {
        String newName = nameField.getText() != null ? nameField.getText().trim() : "";
        String newEmail = emailField.getText() != null ? emailField.getText().trim() : "";
        String newPhone = phoneField.getText() != null ? phoneField.getText().trim() : "";

        if (newName.isEmpty() || newEmail.isEmpty() || newPhone.isEmpty()) {
            showAlert("Ошибка", "Все поля должны быть заполнены", Alert.AlertType.ERROR);
            return;
        }

        try {
            Client updatedClient = new Client();
            updatedClient.setId(currentUser.getId());
            updatedClient.setUsername(currentUser.getUsername());
            updatedClient.setPassword(currentUser.getPassword());
            updatedClient.setRole(currentUser.getRole());
            updatedClient.setName(newName);
            updatedClient.setEmail(newEmail);
            updatedClient.setPhone(newPhone);

            UpdateClientRequest request = new UpdateClientRequest();
            request.setClient(updatedClient);

            Response response = call(request);

            if (response instanceof ClientResponse clientResponse) {
                showAlert("Успех", "Профиль успешно обновлен", Alert.AlertType.INFORMATION);
                CurrentUser.CURRENT_USER = clientResponse.getClient();
            } else {
                showAlert("Ошибка", "Не удалось обновить профиль", Alert.AlertType.ERROR);
            }
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Ошибка", "Произошла ошибка при обновлении профиля", Alert.AlertType.ERROR);
        }
    }

    private void showAlert(String title, String message, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void goBack(ActionEvent actionEvent) {
        switchPage("/client/main.fxml");
    }

    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public void toCancelOrder(MouseEvent mouseEvent) {
        switchPage("/client/cancel.fxml");
    }
}
