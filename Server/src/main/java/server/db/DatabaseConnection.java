package server.db;

import java.sql.*;

public class DatabaseConnection {

    private static final String DB_URL = "jdbc:mysql://localhost:3306/sample1";  // Убедитесь, что название базы данных правильное
    private static final String DB_USER = "root";  // Убедитесь, что имя пользователя правильное
    private static final String DB_PASSWORD = "pc4w7wNR6tehEGY7";  // Убедитесь, что пароль правильный

    // Метод для получения соединения с базой данных
    public static Connection getConnection() throws SQLException {
        try {
            return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
        } catch (SQLException e) {
            e.printStackTrace();  // Логируем ошибку подключения
            throw new SQLException("Ошибка подключения к базе данных", e);  // Генерируем исключение
        }
    }
}
