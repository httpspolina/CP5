package client.controllers;

import client.models.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.*;
import java.net.Socket;

public class LoginRegisterController {

    @FXML
    public Button registerButton;
    public Label registerAdminButton;
    public Button loginButtonMenu;
    public Button registerButtonMenu;
    public Button enterButton;

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField; // Используем PasswordField для пароля

    @FXML
    private TextField adminCodeField;

    private static final String SERVER_ADDRESS = "localhost";
    private static final int SERVER_PORT = 12345;
    private static final String ADMIN_CODE = "pass123";

    private void switchPage(String fxmlFile) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
            AnchorPane root = loader.load();
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
    public void onRegisterAdminClick(MouseEvent event) {
        switchPage("/client/register_admin.fxml");
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
                openWelcomeWindow("Добро пожаловать, " + username);
            } else {
                showErrorAlert(result);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
            showErrorAlert("Ошибка при подключении к серверу.");
        }
    }

    @FXML
    private void onRegisterSubmitClick() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        if (username.isEmpty() || password.isEmpty()) {
            showErrorAlert("Пожалуйста, заполните все поля.");
            return;
        }

        User user = new User(username, password);

        try {
            String result = registerUser(user);

            if (result.equals("Пользователь успешно зарегистрирован")) {
                openWelcomeWindow("Добро пожаловать, " + username);
            } else {
                showErrorAlert(result);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
            showErrorAlert("Ошибка при подключении к серверу.");
        }
    }

    private void openWelcomeWindow(String username) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/client/welcome.fxml"));
            Parent root = loader.load();

            // Устанавливаем имя пользователя в WelcomeController
            WelcomeController.setCurrentUser(username);  // Теперь этот метод существует!

            Stage currentStage = (Stage) usernameField.getScene().getWindow();
            currentStage.getScene().setRoot(root);
            currentStage.setTitle("Добро пожаловать");
        } catch (IOException e) {
            e.printStackTrace();
            showErrorAlert("Ошибка при загрузке страницы приветствия.");
        }
    }

    private String registerUser(User user) throws IOException {
        try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
             ObjectOutputStream objectOutput = new ObjectOutputStream(socket.getOutputStream());
             ObjectInputStream objectInput = new ObjectInputStream(socket.getInputStream())) {

            objectOutput.writeObject("REGISTER_USER");
            objectOutput.writeObject(user);
            return (String) objectInput.readObject();
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
            objectOutput.writeObject(user);
            return (String) objectInput.readObject();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return "Ошибка при обработке данных";
        }
    }

    private void showErrorAlert(String errorMessage) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Ошибка");
        alert.setHeaderText("Что-то пошло не так");
        alert.setContentText(errorMessage);
        alert.showAndWait();
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
}
