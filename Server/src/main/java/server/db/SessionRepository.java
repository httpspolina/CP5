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

    public Session findById(Integer id) {
        try (var connection = DatabaseConnection.get()) {
            try (var statement = connection.prepareStatement("""
                    SELECT s.id, s.film_id, s.hall_id, s.date, h.seats, o.seat_index
                    FROM session AS s
                    JOIN hall AS h ON s.hall_id = h.id
                    LEFT JOIN `order` AS o ON s.id = o.session_id AND o.status = 'PAYED'
                    WHERE s.id = ?
                    ORDER BY o.seat_index
                    """)) {
                statement.setInt(1, id);
                try (var rs = statement.executeQuery()) {
                    if (rs.next()) {
                        Session session = new Session();
                        session.setId(rs.getInt("id"));
                        session.setFilmId(rs.getInt("film_id"));
                        session.setHallId(rs.getInt("hall_id"));
                        session.setDate(rs.getTimestamp("date"));
                        session.setSeats(rs.getInt("seats"));
                        for (int i = 1; i <= session.getSeats(); i++) {
                            session.getAvailableSeats().add(i);
                        }
                        do {
                            int seatIndex = rs.getInt("seat_index");
                            session.getAvailableSeats().remove(seatIndex);
                            session.getOccupiedSeats().add(seatIndex);
                        } while (rs.next());
                        return session;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
