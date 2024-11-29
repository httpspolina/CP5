package server.db;

import common.model.Review;

import java.sql.Statement;

public class ReviewRepository {

    public Integer create(Review review) {
        try (var connection = DatabaseConnection.get()) {
            try (var statement = connection.prepareStatement("""
                    INSERT INTO review (film_id, client_id, rating, description)
                    VALUES (?, ?, ?, ?)
                    """, Statement.RETURN_GENERATED_KEYS)) {
                statement.setInt(1, review.getFilmId());
                statement.setInt(2, review.getClientId());
                statement.setInt(3, review.getRating());
                statement.setString(4, review.getDescription());
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
