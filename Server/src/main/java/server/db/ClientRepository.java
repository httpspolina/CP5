package server.db;

import common.model.Client;
import common.model.UserRole;

import java.sql.Statement;

public class ClientRepository {

    public Client findByUsername(String username) {
        try (var connection = DatabaseConnection.get()) {
            try (var statement = connection.prepareStatement("""
                    SELECT user.id, user.username, user.password, user.role,
                           client.name, client.email, client.phone
                    FROM user
                    LEFT JOIN client ON user.id = client.id
                    WHERE user.username = ?
                    """)) {
                statement.setString(1, username);
                try (var rs = statement.executeQuery()) {
                    if (rs.next()) {
                        Client client = new Client();
                        client.setId(rs.getInt("id"));
                        client.setUsername(rs.getString("username"));
                        client.setPassword(rs.getString("password"));
                        client.setRole(UserRole.valueOf(rs.getString("role")));
                        client.setName(rs.getString("name"));
                        client.setEmail(rs.getString("email"));
                        client.setPhone(rs.getString("phone"));
                        return client;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public Integer create(Client client) {
        try (var connection = DatabaseConnection.get()) {
            int newUserId;
            try (var statement = connection.prepareStatement("""
                    INSERT INTO user (username, password, role)
                    VALUES (?, ?, ?)
                    """, Statement.RETURN_GENERATED_KEYS)) {
                statement.setString(1, client.getUsername());
                statement.setString(2, client.getPassword());
                statement.setString(3, client.getRole().name());
                if (statement.executeUpdate() == 0) throw new RuntimeException();
                var generatedKeys = statement.getGeneratedKeys();
                if (!generatedKeys.next()) throw new RuntimeException();
                newUserId = generatedKeys.getInt(1);
            }
            try (var statement = connection.prepareStatement("""
                    INSERT INTO client (id, name, email, phone)
                    VALUES (?, ?, ?, ?)
                    """)) {
                statement.setInt(1, newUserId);
                statement.setString(2, client.getName());
                statement.setString(3, client.getEmail());
                statement.setString(4, client.getPhone());
                if (statement.executeUpdate() == 0) throw new RuntimeException();
                return newUserId;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
