package server;

import java.io.*;
import java.net.*;
import java.sql.*;
import server.db.SQLRegistration;
import server.db.SQLAuthorization;
import client.models.User;

public class Server {
    private static final int PORT = 12345;

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Сервер запущен. Ожидание подключения...");

            while (true) {
                try (Socket clientSocket = serverSocket.accept();
                     ObjectInputStream objectInput = new ObjectInputStream(clientSocket.getInputStream());
                     ObjectOutputStream objectOutput = new ObjectOutputStream(clientSocket.getOutputStream())) {

                    System.out.println("Клиент подключен: " + clientSocket.getInetAddress());

                    // Чтение типа операции
                    String operation = (String) objectInput.readObject();

                    if ("REGISTER_USER".equals(operation)) {
                        // Обработка запроса на регистрацию пользователя
                        User user = (User) objectInput.readObject(); // Чтение объекта User
                        String registrationResult = SQLRegistration.registerUser(user.getUsername(), user.getPassword(), user.getRole());
                        objectOutput.writeObject(registrationResult);

                    } else if ("LOGIN_USER".equals(operation)) {
                        // Обработка запроса на авторизацию
                        User user = (User) objectInput.readObject(); // Чтение объекта User
                        String loginResult = SQLAuthorization.loginUser(user.getUsername(), user.getPassword());
                        objectOutput.writeObject(loginResult);

                    } else {
                        objectOutput.writeObject("Неизвестная команда");
                    }
                } catch (IOException | ClassNotFoundException | SQLException e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
