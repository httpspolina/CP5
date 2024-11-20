package client.controllers;

import client.models.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.*;
import java.net.Socket;

public class LoginRegisterController {

    public Button registerAdminButton;
    @FXML
    private Button registerButton;

    @FXML
    private Button loginButton;

    @FXML
    private TextField usernameField;

    @FXML
    private TextField passwordField;


    @FXML
    private TextField adminCodeField;

    private static final String SERVER_ADDRESS = "localhost";
    private static final int SERVER_PORT = 12345;
    private static final String ADMIN_CODE = "pass123";

    @FXML
    private void onRegisterClick(ActionEvent event) {
        openWindow("/client/register.fxml", "Регистрация");
    }

    @FXML
    private void onRegisterAdminClick(ActionEvent event) {
        openWindow("/client/register_admin.fxml", "Регистрация администратора");
    }

    @FXML
    private void onLoginClick(ActionEvent event) {
        openWindow("/client/login.fxml", "Авторизация");
    }

    private void openWindow(String fxmlPath, String title) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            AnchorPane root = loader.load();
            Stage stage = new Stage();
            stage.setTitle(title);
            stage.setScene(new Scene(root, 300, 200));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showErrorAlert("Ошибка при открытии окна.");
        }
    }

    @FXML
    private void onRegisterSubmitClick() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        // Проверка на пустые поля
        if (username.isEmpty() || password.isEmpty()) {
            showErrorAlert("Пожалуйста, заполните все поля.");
            return;
        }

        // Создаем объект User
        User user = new User(username, password);

        try {
            String result = registerUser(user);

            if (result.equals("Пользователь успешно зарегистрирован")) {
                openWelcomeWindow("Добро пожаловать, " + username); // Открываем окно приветствия
            } else {
                showErrorAlert(result); // Показываем сообщение об ошибке
            }
        } catch (IOException ex) {
            ex.printStackTrace();
            showErrorAlert("Ошибка при подключении к серверу.");
        }
    }

    @FXML
    private void onRegisterAdminSubmitClick(ActionEvent actionEvent) {
        String username = usernameField.getText();
        String password = passwordField.getText();
        String adminCode = adminCodeField.getText();

        // Проверка на пустые поля
        if (username.isEmpty() || password.isEmpty() || adminCode.isEmpty()) {
            showErrorAlert("Пожалуйста, заполните все поля.");
            return;
        }

        // Проверка правильности кода администратора
        if (adminCode.equals(ADMIN_CODE)) {
            // Создаем объект User с ролью admin
            User user = new User(username, password, "admin");  // Присваиваем роль admin

            try {
                // Отправляем запрос на сервер для регистрации администратора
                String result = registerUser(user);

                // Обработка результата регистрации
                if (result.equals("Пользователь успешно зарегистрирован")) {
                    openWelcomeWindow("Добро пожаловать, " + username); // Открываем окно приветствия
                } else {
                    showErrorAlert(result); // Показываем сообщение об ошибке
                }
            } catch (IOException ex) {
                ex.printStackTrace();
                showErrorAlert("Ошибка при подключении к серверу.");
            }
        } else {
            // Ошибка, если код неправильный
            showErrorAlert("Неверный код администратора.");
        }
    }

    @FXML
    private void onLoginSubmitClick() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        // Проверка на пустые поля
        if (username.isEmpty() || password.isEmpty()) {
            showErrorAlert("Пожалуйста, заполните все поля.");
            return;
        }

        // Создаем объект User
        User user = new User(username, password);

        try {
            String result = loginUser(user);

            if (result.startsWith("Авторизация успешна")) {
                openWelcomeWindow(result); // Открываем окно приветствия с сообщением
            } else {
                showErrorAlert(result); // Показываем сообщение об ошибке
            }
        } catch (IOException ex) {
            ex.printStackTrace();
            showErrorAlert("Ошибка при подключении к серверу.");
        }
    }

    private void openWelcomeWindow(String welcomeMessage) {
        try {
            // Загружаем FXML для окна приветствия
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/client/welcome.fxml"));
            AnchorPane root = loader.load(); // Используем AnchorPane

            // Получаем контроллер для окна приветствия
            WelcomeController welcomeController = loader.getController();
            welcomeController.setWelcomeMessage(welcomeMessage); // Передаем полное сообщение в окно приветствия

            Scene scene = new Scene(root, 400, 300);
            Stage welcomeStage = new Stage();
            welcomeStage.setTitle("Добро пожаловать");
            welcomeStage.setScene(scene);
            welcomeStage.show();
        } catch (IOException e) {
            e.printStackTrace();
            // Обработка ошибки загрузки окна приветствия
        }
    }

    private String registerUser(User user) throws IOException {
        try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
             ObjectOutputStream objectOutput = new ObjectOutputStream(socket.getOutputStream());
             ObjectInputStream objectInput = new ObjectInputStream(socket.getInputStream())) {

            objectOutput.writeObject("REGISTER_USER");
            objectOutput.writeObject(user); // Отправляем объект User
            return (String) objectInput.readObject(); // Ожидаем строку с результатом
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return "Ошибка при обработке данных";
        }
    }

    private String loginUser(User user) throws IOException {
        try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
             ObjectOutputStream objectOutput = new ObjectOutputStream(socket.getOutputStream());
             ObjectInputStream objectInput = new ObjectInputStream(socket.getInputStream())) {

            objectOutput.writeObject("LOGIN_USER");
            objectOutput.writeObject(user); // Отправляем объект User
            return (String) objectInput.readObject(); // Ожидаем строку с результатом
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return "Ошибка при обработке данных";
        }
    }

    private void showErrorAlert(String errorMessage) {
        // Создаем Alert для отображения ошибки
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Ошибка");
        alert.setHeaderText("Что-то пошло не так");
        alert.setContentText(errorMessage);
        alert.showAndWait();
    }
}
