package server.controller;

import common.command.ErrorResponse;
import common.command.Response;
import common.command.SuccessResponse;
import common.command.admin.AdminLoginRequest;
import common.command.admin.AdminRegisterRequest;
import common.model.Role;
import common.model.User;
import server.ServerConfig;
import server.db.PasswordHashing;
import server.db.UserRepository;

public class AdminController {

    private final UserRepository userRepository;

    public AdminController() {
        this.userRepository = new UserRepository();
    }

    public Response processAdminLoginRequest(AdminLoginRequest req) {
        try {
            User foundUser = userRepository.findByUsername(req.getUsername().toLowerCase());
            if (foundUser == null || foundUser.getRole() != Role.admin || !PasswordHashing.verifyPassword(req.getPassword(), foundUser.getPassword())) {
                System.out.println("Неверное имя пользователя или пароль");
                return ErrorResponse.INSTANCE;
            }
            return SuccessResponse.INSTANCE;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ErrorResponse.INSTANCE;
    }

    public Response processAdminRegisterRequest(AdminRegisterRequest req) {
        try {
            if (!req.getCode().equals(ServerConfig.getInstance().getAdminCode())) {
                System.out.println("Неправильный код администратора");
                return ErrorResponse.INSTANCE;
            }
            User foundUser = userRepository.findByUsername(req.getUsername().toLowerCase());
            if (foundUser != null) {
                System.out.println("Пользователь с таким именем уже существует");
                return ErrorResponse.INSTANCE;
            }
            User newUser = new User();
            newUser.setUsername(req.getUsername().toLowerCase());
            newUser.setPassword(PasswordHashing.hashPassword(req.getPassword()));
            newUser.setRole(Role.admin);
            Integer newUserId = userRepository.register(newUser);
            if (newUserId == null) {
                System.out.println("Не удалось создать администратора");
                return ErrorResponse.INSTANCE;
            }
            return SuccessResponse.INSTANCE;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ErrorResponse.INSTANCE;
    }

}
