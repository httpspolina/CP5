package client.controllers;

import client.CurrentUser;
import common.command.ErrorResponse;
import common.command.Response;
import common.command.admin.AdminLoginRequest;
import common.command.admin.AdminRegisterRequest;
import common.command.admin.AdminResponse;
import common.command.client.ClientLoginRequest;
import common.command.client.ClientRegisterRequest;
import common.command.client.ClientResponse;
import common.command.supervisor.SupervisorLoginRequest;
import common.command.supervisor.SupervisorRegisterRequest;
import common.command.supervisor.SupervisorResponse;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class LoginRegisterController extends AbstractController {

    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private PasswordField codeField;

    @FXML
    public void goToAdminLoginPage() {
        switchPage("/admin/login.fxml");
    }

    @FXML
    public void goToAdminRegisterPage() {
        switchPage("/admin/register.fxml");
    }

    @FXML
    public void goToClientLoginPage() {
        switchPage("/client/login.fxml");
    }

    @FXML
    public void goToClientRegisterPage() {
        switchPage("/client/register.fxml");
    }

    @FXML
    public void goToSupervisorLoginPage() {
        switchPage("/supervisor/login.fxml");
    }

    @FXML
    public void goToSupervisorRegisterPage() {
        switchPage("/supervisor/register.fxml");
    }

    @FXML
    public void onAdminLoginSubmit() {
        String username = usernameField.getText();
        String password = passwordField.getText();
        if (username.isEmpty() || password.isEmpty()) {
            showErrorAlert("Пожалуйста, заполните все поля.");
            return;
        }

        AdminLoginRequest req = new AdminLoginRequest();
        req.setUsername(username);
        req.setPassword(password);

        try {
            Response res = call(req);
            if (res instanceof AdminResponse r) {
                CurrentUser.CURRENT_USER = r.getUser();
                switchPage("/admin/main.fxml");
                return;
            }
            if (res instanceof ErrorResponse r) {
                showErrorAlert(r.getMessage());
                return;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        showErrorAlert("Не удалось войти как администратор.");
    }

    @FXML
    public void onAdminRegisterSubmit() {
        String username = usernameField.getText();
        String password = passwordField.getText();
        String code = codeField.getText();
        if (username.isEmpty() || password.isEmpty() || code.isEmpty()) {
            showErrorAlert("Пожалуйста, заполните все поля.");
            return;
        }

        AdminRegisterRequest req = new AdminRegisterRequest();
        req.setUsername(username);
        req.setPassword(password);
        req.setCode(code);

        try {
            Response res = call(req);
            if (res instanceof AdminResponse r) {
                CurrentUser.CURRENT_USER = r.getUser();
                switchPage("/admin/main.fxml");
                return;
            }
            if (res instanceof ErrorResponse r) {
                showErrorAlert(r.getMessage());
                return;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        showErrorAlert("Не удалось зарегистрироваться как администратор.");
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
        showErrorAlert("Не удалось войти как клиент.");
    }

    @FXML
    public void onClientRegisterSubmit() {
        String username = usernameField.getText();
        String password = passwordField.getText();
        if (username.isEmpty() || password.isEmpty()) {
            showErrorAlert("Пожалуйста, заполните все поля.");
            return;
        }

        ClientRegisterRequest req = new ClientRegisterRequest();
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
        showErrorAlert("Не удалось зарегистрироваться как клиент.");
    }

    @FXML
    public void onSupervisorLoginSubmit() {
        String username = usernameField.getText();
        String password = passwordField.getText();
        if (username.isEmpty() || password.isEmpty()) {
            showErrorAlert("Пожалуйста, заполните все поля.");
            return;
        }

        SupervisorLoginRequest req = new SupervisorLoginRequest();
        req.setUsername(username);
        req.setPassword(password);

        try {
            Response res = call(req);
            if (res instanceof SupervisorResponse r) {
                CurrentUser.CURRENT_USER = r.getUser();
                switchPage("/supervisor/main.fxml");
                return;
            }
            if (res instanceof ErrorResponse r) {
                showErrorAlert(r.getMessage());
                return;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        showErrorAlert("Не удалось войти как руководитель.");
    }

    @FXML
    public void onSupervisorRegisterSubmit() {
        String username = usernameField.getText();
        String password = passwordField.getText();
        String code = codeField.getText();
        if (username.isEmpty() || password.isEmpty() || code.isEmpty()) {
            showErrorAlert("Пожалуйста, заполните все поля.");
            return;
        }

        SupervisorRegisterRequest req = new SupervisorRegisterRequest();
        req.setUsername(username);
        req.setPassword(password);
        req.setCode(code);

        try {
            Response res = call(req);
            if (res instanceof SupervisorResponse r) {
                CurrentUser.CURRENT_USER = r.getUser();
                switchPage("/supervisor/main.fxml");
                return;
            }
            if (res instanceof ErrorResponse r) {
                showErrorAlert(r.getMessage());
                return;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        showErrorAlert("Не удалось зарегистрироваться как руководитель.");
    }

}
