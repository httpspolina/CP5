package server.db;

import common.model.User;
import common.model.UserRole;

import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

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

    public List<User> findAllUsers() {
        List<User> users = new ArrayList<>();
        try (var connection = DatabaseConnection.get()) {
            try (var statement = connection.prepareStatement("""
                    SELECT id, username, role
                    FROM user
                    """)) {
                try (var rs = statement.executeQuery()) {
                    while (rs.next()) {
                        User user = new User();
                        user.setId(rs.getInt("id"));
                        user.setUsername(rs.getString("username"));
                        user.setRole(UserRole.valueOf(rs.getString("role")));
                        users.add(user);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return users;
    }

    public User findById(Integer userId) {
        try (var connection = DatabaseConnection.get()) {
            try (var statement = connection.prepareStatement("""
                    SELECT id, username, password, role
                    FROM user
                    WHERE id = ?
                    """)) {
                statement.setInt(1, userId);
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

    public boolean deleteUserById(Integer userId) {
        try (var connection = DatabaseConnection.get()) {
            try {
                connection.setAutoCommit(false);

                try (var deletePaymentMethodsStatement = connection.prepareStatement("""
                        DELETE FROM payment_method WHERE client_id IN (
                            SELECT id FROM client WHERE id = ?
                        )
                        """)) {
                    deletePaymentMethodsStatement.setInt(1, userId);
                    deletePaymentMethodsStatement.executeUpdate();
                }

                try (var deleteClientsStatement = connection.prepareStatement("""
                        DELETE FROM client WHERE id = ?
                        """)) {
                    deleteClientsStatement.setInt(1, userId);
                    deleteClientsStatement.executeUpdate();
                }

                try (var deleteUserStatement = connection.prepareStatement("""
                        DELETE FROM user WHERE id = ?
                        """)) {
                    deleteUserStatement.setInt(1, userId);
                    int rowsAffected = deleteUserStatement.executeUpdate();

                    connection.commit();
                    return rowsAffected > 0;
                }
            } catch (Exception e) {
                connection.rollback();
                throw e;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
