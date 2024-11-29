package client.controllers;

import client.CurrentUser;
import common.command.ErrorResponse;
import common.command.Response;
import common.command.client.ClientLoginRequest;
import common.command.client.ClientResponse;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class LoginRegisterController extends AbstractController {

    @FXML
    public Button loginButtonMenu;
    @FXML
    public Button registerButtonMenu;
    @FXML
    public Button enterButton;
    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private TextField adminCodeField;
    @FXML
    private TextField supervisorCodeField;

    @FXML
    public void initialize() throws Exception {
        tryReconnect();
    }

    @FXML
    private void onRegisterClick(ActionEvent event) throws Exception {
        switchPage("/client/register.fxml");
    }

    @FXML
    private void onLoginClick(ActionEvent event) throws Exception {
        switchPage("/client/login.fxml");
    }

    @FXML
    private void onClientLoginSubmit() {
        String username = usernameField.getText();
        String password = passwordField.getText();
        if (username.isEmpty() || password.isEmpty()) {
            showErrorAlert("Пожалуйста, заполните все поля.");
            return;
        }

        ClientLoginRequest req = new ClientLoginRequest();
        req.setUsername(username);
        req.setPassword(password);

        try {
            Response res = call(req);
            if (res instanceof ClientResponse r) {
                CurrentUser.CURRENT_USER = r.getClient();
                switchPage("/client/main.fxml");
                return;
            }
            if (res instanceof ErrorResponse r) {
                showErrorAlert(r.getMessage());
                return;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        showErrorAlert("Не удалось авторизоваться.");
    }
}
