package server.db;

import common.model.Hall;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class HallRepository {

    public List<Hall> findByFilmId(Integer filmId) {
        try (var connection = DatabaseConnection.get()) {
            try (var statement = connection.prepareStatement("""
                    SELECT DISTINCT hall.id, hall.name, hall.seats, hall.price
                    FROM hall
                    JOIN session ON hall.id = session.hall_id
                    WHERE session.film_id = ? AND session.date >= now()
                    ORDER BY hall.name
                    """)) {
                statement.setInt(1, filmId);
                try (var rs = statement.executeQuery()) {
                    List<Hall> halls = new ArrayList<>();
                    while (rs.next()) {
                        Hall hall = new Hall();
                        hall.setId(rs.getInt("id"));
                        hall.setName(rs.getString("name"));
                        hall.setSeats(rs.getInt("seats"));
                        hall.setPrice(rs.getFloat("price"));
                        halls.add(hall);
                    }
                    return halls;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Collections.emptyList();
    }
}
