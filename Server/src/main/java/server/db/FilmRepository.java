package server.db;

import common.model.Film;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FilmRepository {

    public List<Film> findAll() {
        try (var connection = DatabaseConnection.get()) {
            try (var statement = connection.prepareStatement("""
                    SELECT id, title, country, year, director, roles, genre, description, poster_url
                    FROM film
                    ORDER BY year DESC, title
                    """)) {
                try (var rs = statement.executeQuery()) {
                    List<Film> films = new ArrayList<>();
                    while (rs.next()) {
                        Film film = new Film();
                        film.setId(rs.getInt("id"));
                        film.setTitle(rs.getString("title"));
                        film.setCountry(rs.getString("country"));
                        film.setYear(rs.getInt("year"));
                        film.setDirector(rs.getString("director"));
                        film.setRoles(rs.getString("roles"));
                        film.setGenre(rs.getString("genre"));
                        film.setDescription(rs.getString("description"));
                        film.setPosterUrl(rs.getString("poster_url"));
                        films.add(film);
                    }
                    return films;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Collections.emptyList();
    }

}
