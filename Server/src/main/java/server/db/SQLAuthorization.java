package server.db;

import java.sql.*;

public class SQLAuthorization {

    public static String loginUser(String username, String password) throws SQLException {
        String result;
        String role = null;
        // SQL-запрос на авторизацию пользователя
        String sql = "SELECT * FROM user WHERE username = ? AND password = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, username);
            statement.setString(2, password);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                role = resultSet.getString("role");  // Получаем роль
                result = "Авторизация успешна. Роль: " + role;
            } else {
                result = "Неверное имя пользователя или пароль";
            }
        }
        return result;
    }
}
