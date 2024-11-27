package client.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.image.Image;

import java.io.IOException;

public class WelcomeController {

    private static final String DB_URL = "jdbc:mysql://localhost:3306/sample1";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "pc4w7wNR6tehEGY7";

    private static String currentUser;

    // Метод для установки текущего пользователя
    public static void setCurrentUser(String username) {
        currentUser = username;
    }

    private void showErrorAlert(String errorMessage) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Ошибка");
        alert.setHeaderText("Что-то пошло не так");
        alert.setContentText(errorMessage);
        alert.showAndWait();
    }

    public void toBackClick(ActionEvent actionEvent) {
    }

    public void toFilmsClick(ActionEvent actionEvent) {
    }
}