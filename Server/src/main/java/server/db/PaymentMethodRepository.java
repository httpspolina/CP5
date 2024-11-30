package server.db;

import common.model.PaymentMethod;

import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PaymentMethodRepository {

    public Integer create(PaymentMethod paymentMethod) {
        try (var connection = DatabaseConnection.get()) {
            try (var statement = connection.prepareStatement("""
                    INSERT INTO payment_method (client_id, card_number, expires_at, cardholder, cvv)
                    VALUES (?, ?, ?, ?, ?)
                    """, Statement.RETURN_GENERATED_KEYS)) {
                statement.setInt(1, paymentMethod.getClientId());
                statement.setString(2, paymentMethod.getCardNumber());
                statement.setDate(3, paymentMethod.getExpiresAt());
                statement.setString(4, paymentMethod.getCardholder());
                statement.setString(5, paymentMethod.getCvv());
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

    public List<PaymentMethod> findByClientId(Integer clientId) {
        try (var connection = DatabaseConnection.get()) {
            try (var statement = connection.prepareStatement("""
                    SELECT id, client_id, card_number, expires_at, cardholder, cvv
                    FROM payment_method
                    WHERE client_id = ?
                    """)) {
                statement.setInt(1, clientId);
                try (var rs = statement.executeQuery()) {
                    List<PaymentMethod> paymentMethods = new ArrayList<>();
                    while (rs.next()) {
                        PaymentMethod paymentMethod = new PaymentMethod();
                        paymentMethod.setId(rs.getInt("id"));
                        paymentMethod.setClientId(rs.getInt("client_id"));
                        paymentMethod.setCardNumber(rs.getString("card_number"));
                        paymentMethod.setExpiresAt(rs.getDate("expires_at"));
                        paymentMethod.setCardholder(rs.getString("cardholder"));
                        paymentMethod.setCvv(rs.getString("cvv"));
                        paymentMethods.add(paymentMethod);
                    }
                    return paymentMethods;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Collections.emptyList();
    }
}
