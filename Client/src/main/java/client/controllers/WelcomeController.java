package client.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class WelcomeController {

    @FXML
    private Label welcomeMessage;

    // Метод для установки приветственного сообщения
    public void setWelcomeMessage(String welcomeMessageText) {
        welcomeMessage.setText(welcomeMessageText);
    }

    // Метод для выхода из приложения (закрывает окно приветствия)
    @FXML
    private void onExitClick() {
        Stage stage = (Stage) welcomeMessage.getScene().getWindow();
        stage.close();
    }
}
