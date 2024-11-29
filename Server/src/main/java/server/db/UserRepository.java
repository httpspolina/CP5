package server.db;

import common.model.User;
import common.model.UserRole;

import java.sql.Statement;

public class UserRepository {

    public User findByUsername(String username) {
        try (var connection = DatabaseConnection.get()) {
            try (var statement = connection.prepareStatement("""
                    SELECT id, username, password, role
                    FROM user
                    WHERE username = ?
                    """)) {
                statement.setString(1, username);
                try (var rs = statement.executeQuery()) {
                    if (rs.next()) {
                        User user = new User();
                        user.setId(rs.getInt("id"));
                        user.setUsername(rs.getString("username"));
                        user.setPassword(rs.getString("password"));
                        user.setRole(UserRole.valueOf(rs.getString("role")));
                        return user;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public Integer create(User user) {
        try (var connection = DatabaseConnection.get()) {
            try (var statement = connection.prepareStatement("""
                    INSERT INTO user (username, password, role)
                    VALUES (?, ?, ?)
                    """, Statement.RETURN_GENERATED_KEYS)) {
                statement.setString(1, user.getUsername());
                statement.setString(2, user.getPassword());
                statement.setString(3, user.getRole().name());
                if (statement.executeUpdate() == 0) throw new RuntimeException();
                var generatedKeys = statement.getGeneratedKeys();
                if (!generatedKeys.next()) throw new RuntimeException();
                return generatedKeys.getInt(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
