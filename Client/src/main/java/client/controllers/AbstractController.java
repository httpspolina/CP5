package client.controllers;

import client.CurrentUser;
import client.PrimaryStageManager;
import client.ServerClient;
import common.command.Request;
import common.command.Response;
import javafx.scene.control.Alert;

public abstract class AbstractController {

    protected void switchPage(String fxmlFile) {
        try {
            PrimaryStageManager.INSTANCE.switchPage(fxmlFile);
        } catch (Exception e) {
            e.printStackTrace();
            showErrorAlert("Ошибка при загрузке страницы.");
        }
    }

    protected void tryReconnect() throws Exception {
        if (!ServerClient.INSTANCE.isConnected()) {
            ServerClient.INSTANCE.connect();
        }
    }

    protected Response call(Request request) throws Exception {
        try {
            return ServerClient.INSTANCE.call(request);
        } catch (Exception e) {
            switchPage("/client/login.fxml");
            CurrentUser.CURRENT_USER = null;
            throw e;
        }
    }

    protected void showErrorAlert(String errorMessage) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Ошибка");
        alert.setHeaderText("Что-то пошло не так");
        alert.setContentText(errorMessage);
        alert.showAndWait();
    }
}
