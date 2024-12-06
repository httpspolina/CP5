package server.db;

import common.model.Client;
import common.model.Film;
import common.model.Review;
import common.model.UserRole;

import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FilmRepository {

    public Integer create(Film film) {
        try (var connection = DatabaseConnection.get()) {
            try (var statement = connection.prepareStatement("""
                    INSERT INTO film (title, country, year, director, roles, genre, description, poster_url)
                    VALUES (?, ?, ?, ?, ?, ?, ?, ?)
                    """, Statement.RETURN_GENERATED_KEYS)) {
                statement.setString(1, film.getTitle());
                statement.setString(2, film.getCountry());
                statement.setInt(3, film.getYear());
                statement.setString(4, film.getDirector());
                statement.setString(5, film.getRoles());
                statement.setString(6, film.getGenre());
                statement.setString(7, film.getDescription());
                statement.setString(8, film.getPosterUrl());
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

    public List<Film> findAll() {
        try (var connection = DatabaseConnection.get()) {
            try (var statement = connection.prepareStatement("""
                    SELECT film.id, film.title, film.country, film.year, film.director, film.roles, film.genre, film.description, film.poster_url,
                           avg(review.rating) AS rating
                    FROM film
                    LEFT JOIN review ON film.id = review.film_id
                    GROUP BY film.id, film.title, film.country, film.year, film.director, film.roles, film.genre, film.description, film.poster_url
                    ORDER BY film.year DESC, film.title
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
                        film.setRating(rs.getDouble("rating"));
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

    public Film findById(Integer filmId) {
        try (var connection = DatabaseConnection.get()) {
            try (var statement = connection.prepareStatement("""
                    SELECT film.id AS film_id, film.title, film.country, film.year, film.director, film.roles, film.genre, film.description AS film_description, film.poster_url,
                           review.id AS review_id, review.client_id, review.rating, review.description AS review_description, review.created_at,
                           client.name AS client_name
                    FROM film
                    LEFT JOIN review ON film.id = review.film_id
                    LEFT JOIN client ON review.client_id = client.id
                    WHERE film.id = ?
                    ORDER BY review.created_at DESC
                    """)) {
                statement.setInt(1, filmId);
                try (var rs = statement.executeQuery()) {
                    if (rs.next()) {
                        Film film = new Film();
                        List<Review> reviews = new ArrayList<>();
                        film.setId(rs.getInt("film_id"));
                        film.setTitle(rs.getString("title"));
                        film.setCountry(rs.getString("country"));
                        film.setYear(rs.getInt("year"));
                        film.setDirector(rs.getString("director"));
                        film.setRoles(rs.getString("roles"));
                        film.setGenre(rs.getString("genre"));
                        film.setDescription(rs.getString("film_description"));
                        film.setPosterUrl(rs.getString("poster_url"));
                        do {
                            if (rs.getInt("review_id") != 0) {
                                Review review = new Review();
                                review.setId(rs.getInt("review_id"));
                                review.setFilmId(rs.getInt("film_id"));
                                review.setClientId(rs.getInt("client_id"));
                                review.setRating(rs.getInt("rating"));
                                review.setDescription(rs.getString("review_description"));
                                review.setCreatedAt(rs.getTimestamp("created_at"));
                                if (rs.getString("client_name") != null) {
                                    Client client = new Client();
                                    client.setId(rs.getInt("client_id"));
                                    client.setRole(UserRole.CLIENT);
                                    client.setName(rs.getString("client_name"));
                                    review.setClient(client);
                                }
                                reviews.add(review);
                            }
                        } while (rs.next());
                        film.setRating(reviews.stream().mapToInt(r -> r.getRating()).average().orElse(0.0));
                        film.setReviews(reviews);
                        return film;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public Film findByTitle(String title) {
        try (var connection = DatabaseConnection.get()) {
            try (var statement = connection.prepareStatement("""
                    SELECT film.id, film.title, film.country, film.year, film.director, film.roles, film.genre, film.description, film.poster_url,
                           avg(review.rating) AS rating
                    FROM film
                    LEFT JOIN review ON film.id = review.film_id
                    WHERE film.title = ?
                    GROUP BY film.id, film.title, film.country, film.year, film.director, film.roles, film.genre, film.description, film.poster_url
                    ORDER BY film.year DESC, film.title
                    """)) {
                statement.setString(1, title);
                try (var rs = statement.executeQuery()) {
                    if (rs.next()) {
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
                        film.setRating(rs.getDouble("rating"));
                        return film;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    public boolean deleteById(Integer filmId) {
        try (var connection = DatabaseConnection.get()) {
            try (var orderStatement = connection.prepareStatement("""
                        DELETE o FROM `order` o
                        JOIN `session` s ON o.session_id = s.id
                        WHERE s.film_id = ?
                    """)) {
                orderStatement.setInt(1, filmId);
                orderStatement.executeUpdate();
            }

            try (var sessionStatement = connection.prepareStatement("""
                        DELETE FROM session WHERE film_id = ?
                    """)) {
                sessionStatement.setInt(1, filmId);
                sessionStatement.executeUpdate();
            }

            try (var reviewStatement = connection.prepareStatement("""
                        DELETE FROM review WHERE film_id = ?
                    """)) {
                reviewStatement.setInt(1, filmId);
                reviewStatement.executeUpdate();
            }

            try (var filmStatement = connection.prepareStatement("""
                        DELETE FROM film WHERE id = ?
                    """)) {
                filmStatement.setInt(1, filmId);
                int rowsAffected = filmStatement.executeUpdate();
                return rowsAffected > 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean update(Film film) {
        try (var connection = DatabaseConnection.get()) {
            try (var statement = connection.prepareStatement("""
                    UPDATE film
                    SET title = ?, country = ?, year = ?, director = ?, roles = ?, genre = ?, description = ?, poster_url = ?
                    WHERE id = ?
                    """)) {
                statement.setString(1, film.getTitle());
                statement.setString(2, film.getCountry());
                statement.setInt(3, film.getYear());
                statement.setString(4, film.getDirector());
                statement.setString(5, film.getRoles());
                statement.setString(6, film.getGenre());
                statement.setString(7, film.getDescription());
                statement.setString(8, film.getPosterUrl());
                statement.setInt(9, film.getId());

                int rowsAffected = statement.executeUpdate();
                return rowsAffected > 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public List<Film> filterFilms(String sortOrder) {
        // Проверка корректности значения sortOrder
        if (!sortOrder.equals("asc") && !sortOrder.equals("desc")) {
            throw new IllegalArgumentException("Invalid sort order: " + sortOrder);
        }

        // Формируем правильный SQL-запрос для сортировки
        String sql = "SELECT film.id, film.title, film.country, film.year, film.director, film.roles, film.genre, film.description, film.poster_url, " +
                "avg(review.rating) AS rating " +
                "FROM film " +
                "LEFT JOIN review ON film.id = review.film_id " +
                "GROUP BY film.id, film.title, film.country, film.year, film.director, film.roles, film.genre, film.description, film.poster_url " +
                "ORDER BY film.year " + (sortOrder.equals("asc") ? "ASC" : "DESC");

        // Логируем SQL-запрос для отладки
        System.out.println("Executing SQL: " + sql);

        try (var connection = DatabaseConnection.get();
             var statement = connection.prepareStatement(sql);
             var rs = statement.executeQuery()) {

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
                film.setRating(rs.getDouble("rating"));
                films.add(film);
            }
            return films;
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }
}
