package server.db;

import common.command.client.AddReviewRequest;

public class ReviewRepository {

    public boolean create(Integer currentClientId, AddReviewRequest req) {
        try (var connection = DatabaseConnection.get()) {
            try (var statement = connection.prepareStatement("""
                    INSERT INTO review (film_id, client_id, rating, description)
                    VALUES (?, ?, ?, ?)
                    """)) {
                statement.setInt(1, req.getFilmId());
                statement.setInt(2, currentClientId);
                statement.setInt(3, req.getRating());
                statement.setString(4, req.getDescription());
                return statement.executeUpdate() > 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

}
