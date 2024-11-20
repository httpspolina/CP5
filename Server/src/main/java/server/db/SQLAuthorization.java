package server.db;

import java.sql.*;

public class SQLAuthorization {

    public static String loginUser(String username, String password) throws SQLException {
        String result;
        // SQL-запрос на авторизацию пользователя
        String sql = "SELECT * FROM user WHERE username = ? AND password = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, username);
            statement.setString(2, password);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                result = "Авторизация успешна. Добро пожаловать, " + username;
            } else {
                result = "Неверное имя пользователя или пароль";
            }
        }
        return result;
    }
}
