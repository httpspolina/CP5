package client.controllers;

import client.models.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.*;
import java.net.Socket;

public class LoginRegisterController {

    public Button loginButtonMenu;
    public Button registerButtonMenu;
    public Button enterButton;

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    private static final String SERVER_ADDRESS = "localhost";
    private static final int SERVER_PORT = 12345;

    private void switchPage(String fxmlFile) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
            Parent root = loader.load();
            Stage stage = (Stage) registerButtonMenu.getScene().getWindow();
            stage.getScene().setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
            showErrorAlert("Ошибка при загрузке страницы.");
        }
    }

    @FXML
    private void onRegisterClick(ActionEvent event) {
        switchPage("/client/register.fxml");
    }

    @FXML
    private void onAuthorizationClick(ActionEvent event) {
        switchPage("/client/login.fxml");
    }

    @FXML
    private void onLoginSubmitClick() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        if (username.isEmpty() || password.isEmpty()) {
            showErrorAlert("Пожалуйста, заполните все поля.");
            return;
        }

        User user = new User(username, password);

        try {
            String result = loginUser(user);

            if (result.startsWith("Авторизация успешна")) {
                // Извлекаем роль из результата
                String role = result.split("Роль: ")[1]; // Получаем роль

                if ("admin".equals(role)) {
                    openAdminWelcomeWindow();
                } else {
                    openClientWelcomeWindow();
                }
            } else {
                showErrorAlert(result);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
            showErrorAlert("Ошибка при подключении к серверу.");
        }
    }

    private void openClientWelcomeWindow() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/client/main.fxml"));
            Parent root = loader.load();

            WelcomeController.setCurrentUser(usernameField.getText());

            Stage currentStage = (Stage) usernameField.getScene().getWindow();
            currentStage.getScene().setRoot(root);
            currentStage.setTitle("Добро пожаловать");
        } catch (IOException e) {
            e.printStackTrace();
            showErrorAlert("Ошибка при загрузке страницы приветствия.");
        }
    }

    private void openAdminWelcomeWindow() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/admin/main.fxml"));
            Parent root = loader.load();

            WelcomeController.setCurrentUser(usernameField.getText());

            Stage currentStage = (Stage) usernameField.getScene().getWindow();
            currentStage.getScene().setRoot(root);
            currentStage.setTitle("Добро пожаловать, администратор");
        } catch (IOException e) {
            e.printStackTrace();
            showErrorAlert("Ошибка при загрузке страницы приветствия.");
        }
    }

    private String loginUser(User user) throws IOException {
        try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
             ObjectOutputStream objectOutput = new ObjectOutputStream(socket.getOutputStream());
             ObjectInputStream objectInput = new ObjectInputStream(socket.getInputStream())) {

            objectOutput.writeObject("LOGIN_USER");
            objectOutput.writeObject(user);
            return (String) objectInput.readObject();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return "Ошибка при обработке данных";
        }
    }

    private void showErrorAlert(String errorMessage) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Ошибка");
        alert.setHeaderText("Что-то пошло не так");
        alert.setContentText(errorMessage);
        alert.showAndWait();
    }
}
