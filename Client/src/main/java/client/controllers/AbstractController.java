package client.controllers;

import client.CurrentUser;
import client.PrimaryStageManager;
import client.ServerClient;
import common.command.Request;
import common.command.Response;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;

public abstract class AbstractController {
    @FXML
    public Label welcomeLabel;
    @FXML
    public Label usernameLabel;

    @FXML
    public void initialize() {
        tryConnect();
        if (CurrentUser.CURRENT_USER != null && welcomeLabel != null && usernameLabel != null) {
            welcomeLabel.setText(welcomeLabel.getText().replace("{role}", switch (CurrentUser.CURRENT_USER.getRole()) {
                case ADMIN -> "администратор";
                case CLIENT -> "клиент";
                case SUPERVISOR -> "руководитель";
            }));
            usernameLabel.setText(usernameLabel.getText().replace("{username}", CurrentUser.CURRENT_USER.getUsername()));
        }
    }

    public void tryConnect() {
        try {
            if (!ServerClient.INSTANCE.isConnected()) {
                ServerClient.INSTANCE.connect();
                CurrentUser.CURRENT_USER = null;
                switchPage("/client/login.fxml");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public <T> T switchPage(String fxmlFile) {
        try {
            return PrimaryStageManager.INSTANCE.switchPage(fxmlFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public Response call(Request request) throws Exception {
        try {
            return ServerClient.INSTANCE.call(request);
        } catch (Exception e) {
            CurrentUser.CURRENT_USER = null;
            switchPage("/client/login.fxml");
            throw e;
        }
    }

    @FXML
    public void onExitButtonClick() {
        ServerClient.INSTANCE.disconnect();
        switchPage("/client/login.fxml");
        tryConnect();
    }

    public void showErrorAlert(String errorMessage) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Ошибка");
        alert.setHeaderText("Что-то пошло не так");
        alert.setContentText(errorMessage);
        alert.showAndWait();
    }
}
