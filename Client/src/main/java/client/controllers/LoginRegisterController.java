package client.controllers;

import client.models.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
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
    public TextField usernameField_registration;
    public PasswordField passwordField_registration;
    public Button registerButton_registration;
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

    // Новый метод для переключения между страницами
    private void switchPage(String fxmlFile) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
            AnchorPane root = loader.load();
            // Используем текущую сцену для загрузки нового содержимого
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
        switchPage("/client/login_register.fxml");
    }

    @FXML
    private void onRegisterAdminClick(MouseEvent event) {
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
                showErrorAlert(result); // Показываем сообщение об ошибке
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

    private void openWelcomeWindow(String welcomeMessage) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/client/welcome.fxml"));
            AnchorPane root = loader.load();
            WelcomeController welcomeController = loader.getController();
            welcomeController.setWelcomeMessage(welcomeMessage);

            Scene scene = new Scene(root, 400, 300);
            Stage welcomeStage = new Stage();
            welcomeStage.setTitle("Добро пожаловать");
            welcomeStage.setScene(scene);
            welcomeStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String registerUser(User user) throws IOException {
        try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
             DataOutputStream output = new DataOutputStream(socket.getOutputStream());
             DataInputStream input = new DataInputStream(socket.getInputStream())) {

            // Учитываем роль пользователя при отправке запроса
            output.writeUTF("REGISTER_USER:" + user.getUsername() + ":" + user.getPassword() + ":" + user.getRole());
            return input.readUTF(); // Возвращаем результат регистрации
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
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Ошибка");
        alert.setHeaderText("Что-то пошло не так");
        alert.setContentText(errorMessage);
        alert.showAndWait();
    }
}
