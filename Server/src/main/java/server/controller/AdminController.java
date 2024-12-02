package server.controller;

import common.command.CommonErrorResponse;
import common.command.Response;
import common.command.admin.AdminLoginRequest;
import common.command.admin.AdminRegisterRequest;
import common.command.admin.AdminResponse;
import common.command.client.FilmsResponse;
import common.command.client.FindAllFilmsRequest;
import common.model.Film;
import common.model.User;
import common.model.UserRole;
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

}
