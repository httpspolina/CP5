package server.db;

import common.model.Client;
import common.model.Film;
import common.model.Review;
import common.model.UserRole;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class FilmRepositoryTest {

    FilmRepository filmRepository = new FilmRepository();
    ClientRepository clientRepository = new ClientRepository();
    ReviewRepository reviewRepository = new ReviewRepository();

    @Test
    void test() throws Exception {
        Film film = new Film();
        film.setTitle(UUID.randomUUID().toString());
        film.setCountry(UUID.randomUUID().toString());
        film.setYear(2025);
        film.setDirector(UUID.randomUUID().toString());
        film.setRoles(UUID.randomUUID().toString());
        film.setGenre(UUID.randomUUID().toString());
        film.setDescription(UUID.randomUUID().toString());
        film.setPosterUrl(UUID.randomUUID().toString());
        Integer filmId = filmRepository.create(film);
        assertThat(filmId).isNotNull().isPositive();

        Client newClient = new Client();
        newClient.setUsername(UUID.randomUUID().toString());
        newClient.setPassword(PasswordHashing.hashPassword(UUID.randomUUID().toString()));
        newClient.setRole(UserRole.CLIENT);
        newClient.setName(UUID.randomUUID().toString());
        newClient.setEmail(UUID.randomUUID().toString());
        newClient.setPhone(UUID.randomUUID().toString());
        Integer clientId = clientRepository.create(newClient);
        assertThat(clientId).isNotNull().isPositive();

        Review review1 = new Review();
        review1.setFilmId(filmId);
        review1.setClientId(clientId);
        review1.setRating(2);
        review1.setDescription(UUID.randomUUID().toString());
        Integer reviewId1 = reviewRepository.create(review1);
        assertThat(reviewId1).isNotNull().isPositive();

        Review review2 = new Review();
        review2.setFilmId(filmId);
        review2.setClientId(clientId);
        review2.setRating(3);
        review2.setDescription(UUID.randomUUID().toString());
        Integer reviewId2 = reviewRepository.create(review2);
        assertThat(reviewId2).isNotNull().isPositive();

        List<Film> films = filmRepository.findAll();
        assertThat(films).isNotEmpty();

        Film foundFilm = films.stream().filter(f -> f.getId().equals(filmId)).findFirst().get();
        assertThat(foundFilm).isNotNull();
        assertThat(foundFilm.getId()).isEqualTo(filmId);
        assertThat(foundFilm.getTitle()).isEqualTo(film.getTitle());
        assertThat(foundFilm.getCountry()).isEqualTo(film.getCountry());
        assertThat(foundFilm.getYear()).isEqualTo(film.getYear());
        assertThat(foundFilm.getDirector()).isEqualTo(film.getDirector());
        assertThat(foundFilm.getRoles()).isEqualTo(film.getRoles());
        assertThat(foundFilm.getGenre()).isEqualTo(film.getGenre());
        assertThat(foundFilm.getDescription()).isEqualTo(film.getDescription());
        assertThat(foundFilm.getPosterUrl()).isEqualTo(film.getPosterUrl());
        assertThat(foundFilm.getRating()).isEqualTo(2.5);

        Film foundFilm2 = filmRepository.findById(filmId);
        assertThat(foundFilm2).isNotNull();
        assertThat(foundFilm2.getId()).isEqualTo(filmId);
        assertThat(foundFilm2.getTitle()).isEqualTo(film.getTitle());
        assertThat(foundFilm2.getCountry()).isEqualTo(film.getCountry());
        assertThat(foundFilm2.getYear()).isEqualTo(film.getYear());
        assertThat(foundFilm2.getDirector()).isEqualTo(film.getDirector());
        assertThat(foundFilm2.getRoles()).isEqualTo(film.getRoles());
        assertThat(foundFilm2.getGenre()).isEqualTo(film.getGenre());
        assertThat(foundFilm2.getDescription()).isEqualTo(film.getDescription());
        assertThat(foundFilm2.getPosterUrl()).isEqualTo(film.getPosterUrl());
        assertThat(foundFilm2.getRating()).isEqualTo(2.5);

        List<Review> reviews = foundFilm2.getReviews();
        assertThat(reviews).isNotNull().hasSize(2);

        Review foundReview1 = reviews.get(0);
        assertThat(foundReview1.getId()).isEqualTo(reviewId1);
        assertThat(foundReview1.getFilmId()).isEqualTo(review1.getFilmId());
        assertThat(foundReview1.getClientId()).isEqualTo(review1.getClientId());
        assertThat(foundReview1.getRating()).isEqualTo(review1.getRating());
        assertThat(foundReview1.getDescription()).isEqualTo(review1.getDescription());
        assertThat(foundReview1.getClient().getName()).isEqualTo(newClient.getName());

        Review foundReview2 = reviews.get(1);
        assertThat(foundReview2.getId()).isEqualTo(reviewId2);
        assertThat(foundReview2.getFilmId()).isEqualTo(review2.getFilmId());
        assertThat(foundReview2.getClientId()).isEqualTo(review2.getClientId());
        assertThat(foundReview2.getRating()).isEqualTo(review2.getRating());
        assertThat(foundReview2.getDescription()).isEqualTo(review2.getDescription());
        assertThat(foundReview2.getClient().getName()).isEqualTo(newClient.getName());
    }
}