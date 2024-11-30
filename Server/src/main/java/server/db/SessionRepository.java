package server.db;

import common.model.Session;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SessionRepository {

    public List<Session> findByFilmIdAndHallId(Integer filmId, Integer hallId) {
        try (var connection = DatabaseConnection.get()) {
            try (var statement = connection.prepareStatement("""
                    SELECT id, date
                    FROM session
                    WHERE film_id = ? AND hall_id = ? AND date >= now()
                    ORDER BY date
                    """)) {
                statement.setInt(1, filmId);
                statement.setInt(2, hallId);
                try (var rs = statement.executeQuery()) {
                    List<Session> sessions = new ArrayList<>();
                    while (rs.next()) {
                        Session session = new Session();
                        session.setId(rs.getInt("id"));
                        session.setFilmId(filmId);
                        session.setHallId(hallId);
                        session.setDate(rs.getTimestamp("date"));
                        sessions.add(session);
                    }
                    return sessions;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Collections.emptyList();
    }
}
