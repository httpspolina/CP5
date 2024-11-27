package server;

import java.io.*;
import java.net.*;
import java.sql.*;

import server.db.DatabaseConnection;
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

                    }
                    else if ("GET_FILMS".equals(operation)) {
                        // Обработка запроса на получение списка фильмов
                        try (Connection connection = DatabaseConnection.getConnection()) {
                            String query = "SELECT title, country, year, director, roles, genre, description, poster_url FROM films";
                            Statement statement = connection.createStatement();
                            ResultSet resultSet = statement.executeQuery(query);

                            // Собираем фильмы в список
                            StringBuilder films = new StringBuilder();
                            while (resultSet.next()) {
                                films.append("Title: ").append(resultSet.getString("title"))
                                        .append(", Country: ").append(resultSet.getString("country"))
                                        .append(", Year: ").append(resultSet.getInt("year"))
                                        .append(", Director: ").append(resultSet.getString("director"))
                                        .append(", Roles: ").append(resultSet.getString("roles"))
                                        .append(", Genre: ").append(resultSet.getString("genre"))
                                        .append(", Description: ").append(resultSet.getString("description"))
                                        .append(", Poster URL: ").append(resultSet.getString("poster_url"))
                                        .append("\n");
                            }

                            objectOutput.writeObject(films.toString());
                        } catch (SQLException e) {
                            e.printStackTrace();
                            objectOutput.writeObject("Ошибка при получении данных из базы.");
                        }
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
