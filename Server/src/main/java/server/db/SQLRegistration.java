package server.db;

import java.sql.*;

public class SQLRegistration {

    public static String registerUser(String username, String password, String role) {
        // Убедитесь, что таблица содержит поле role
        String query = "INSERT INTO user (username, password, role) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, username);
            stmt.setString(2, password);
            stmt.setString(3, role);  // Передаем роль пользователя (client/admin)

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                return "Пользователь успешно зарегистрирован";
            } else {
                return "Ошибка при регистрации";
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return "Ошибка при подключении к базе данных";
        }
    }
}
