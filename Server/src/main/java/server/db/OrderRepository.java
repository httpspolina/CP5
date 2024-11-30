package server.db;

import common.model.Order;
import common.model.OrderStatus;

import java.sql.Statement;

public class OrderRepository {

    public Integer create(Order order) {
        try (var connection = DatabaseConnection.get()) {
            try (var statement = connection.prepareStatement("""
                    INSERT INTO `order` (client_id, payment_method_id, session_id, seat_index, status)
                    VALUES (?, ?, ?, ?, ?)
                    """, Statement.RETURN_GENERATED_KEYS)) {
                statement.setInt(1, order.getClientId());
                statement.setInt(2, order.getPaymentMethodId());
                statement.setInt(3, order.getSessionId());
                statement.setInt(4, order.getSeatIndex());
                statement.setString(5, order.getStatus().name());
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

    public Order findById(Integer id) {
        try (var connection = DatabaseConnection.get()) {
            try (var statement = connection.prepareStatement("""
                    SELECT id, client_id, payment_method_id, session_id, seat_index, status, created_at
                    FROM `order`
                    WHERE id = ?
                    """)) {
                statement.setInt(1, id);
                try (var rs = statement.executeQuery()) {
                    if (rs.next()) {
                        Order order = new Order();
                        order.setId(rs.getInt("id"));
                        order.setClientId(rs.getInt("client_id"));
                        order.setPaymentMethodId(rs.getInt("payment_method_id"));
                        order.setSessionId(rs.getInt("session_id"));
                        order.setSeatIndex(rs.getInt("seat_index"));
                        order.setStatus(OrderStatus.valueOf(rs.getString("status")));
                        order.setCreatedAt(rs.getTimestamp("created_at"));
                        return order;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
