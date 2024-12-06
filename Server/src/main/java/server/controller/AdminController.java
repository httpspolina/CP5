package server.controller;

import common.command.CommonErrorResponse;
import common.command.Response;
import common.command.SuccessResponse;
import common.command.admin.*;
import common.command.client.*;
import common.model.*;
import server.ServerConfig;
import server.db.FilmRepository;
import server.db.PasswordHashing;
import server.db.UserRepository;

import java.util.List;

public class AdminController {

    private final UserRepository userRepository = new UserRepository();
    private final FilmRepository filmRepository = new FilmRepository();

    public Response login(AdminLoginRequest req) throws Exception {
        User foundUser = userRepository.findByUsername(req.getUsername().toLowerCase());
        if (foundUser == null || foundUser.getRole() != UserRole.ADMIN || !PasswordHashing.verifyPassword(req.getPassword(), foundUser.getPassword())) {
            return new CommonErrorResponse("Неправильное имя пользователя или пароль");
        }
        return new AdminResponse(foundUser);
    }

    public Response register(AdminRegisterRequest req) throws Exception {
        if (!req.getCode().equals(ServerConfig.INSTANCE.getAdminCode())) {
            return new CommonErrorResponse("Неправильный код администратора");
        }
        User existingUser = userRepository.findByUsername(req.getUsername().toLowerCase());
        if (existingUser != null) {
            return new CommonErrorResponse("Пользователь с таким именем уже существует");
        }
        User newUser = new User();
        newUser.setUsername(req.getUsername().toLowerCase());
        newUser.setPassword(PasswordHashing.hashPassword(req.getPassword()));
        newUser.setRole(UserRole.ADMIN);
        Integer newUserId = userRepository.create(newUser);
        if (newUserId == null) {
            return new CommonErrorResponse("Не удалось создать администратора");
        }
        User foundUser = userRepository.findByUsername(req.getUsername().toLowerCase());
        return new AdminResponse(foundUser);
    }

    public Response findAllFilms(FindAllFilmsRequest req) throws Exception {
        List<Film> films = filmRepository.findAll();
        return new FilmsResponse(films);
    }

    public Response findFilmById(FindFilmByIdRequest req) throws Exception {
        Film film = filmRepository.findById(req.getFilmId());
        if (film == null) {
            return new CommonErrorResponse("Не удалось найти фильм");
        }
        return new FilmResponse(film);
    }

    public Response findFilmByTitle(FindFilmByTitleRequest req) throws Exception {
        String title = req.getTitle();
        Film film = filmRepository.findByTitle(title);
        if (film == null) {
            return new CommonErrorResponse("Фильм не найден.");
        }
        return new FilmResponse(film);
    }

    public Response addFilm(AddFilmRequest req) {
        Film film = new Film();
        film.setId(req.getFilm().getId());
        film.setTitle(req.getFilm().getTitle());
        film.setCountry(req.getFilm().getCountry());
        film.setYear(req.getFilm().getYear());
        film.setDirector(req.getFilm().getDirector());
        film.setRoles(req.getFilm().getRoles());
        film.setGenre(req.getFilm().getGenre());
        film.setDescription(req.getFilm().getDescription());
        film.setPosterUrl(req.getFilm().getPosterUrl());

        Integer newFilmId = filmRepository.create(film);
        if (newFilmId == null) {
            return new CommonErrorResponse("Не удалось добавить фильм");
        }
        return SuccessResponse.INSTANCE;
    }

    public Response deleteFilm(DeleteFilmRequest req) {
        boolean success = filmRepository.deleteById(req.getFilmId());
        if (!success) {
            return new CommonErrorResponse("Не удалось удалить фильм.");
        }
        return SuccessResponse.INSTANCE;
    }

    public Response update(UpdateFilmRequest req) {
        Film film = req.getFilm();

        boolean updated = filmRepository.update(film);

        if (!updated) {
            return new CommonErrorResponse("Не удалось обновить фильм");
        }

        Film updatedFilm = filmRepository.findById(film.getId());
        return new FilmResponse(updatedFilm);
    }
}
