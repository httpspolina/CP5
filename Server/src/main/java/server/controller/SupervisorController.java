package server.controller;

import common.command.ErrorResponse;
import common.command.Response;
import common.command.SuccessResponse;
import common.command.supervisor.SupervisorLoginRequest;
import common.command.supervisor.SupervisorRegisterRequest;
import common.model.Role;
import common.model.User;
import server.ServerConfig;
import server.db.PasswordHashing;
import server.db.UserRepository;

public class SupervisorController {

    private final UserRepository userRepository;

    public SupervisorController() {
        this.userRepository = new UserRepository();
    }

    public Response processSupervisorLoginRequest(SupervisorLoginRequest req) {
        try {
            User foundUser = userRepository.findByUsername(req.getUsername().toLowerCase());
            if (foundUser == null || foundUser.getRole() != Role.supervisor || !PasswordHashing.verifyPassword(req.getPassword(), foundUser.getPassword())) {
                System.out.println("Неверное имя пользователя или пароль");
                return ErrorResponse.INSTANCE;
            }
            return SuccessResponse.INSTANCE;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ErrorResponse.INSTANCE;
    }

    public Response processSupervisorRegisterRequest(SupervisorRegisterRequest req) {
        try {
            if (!req.getCode().equals(ServerConfig.getInstance().getSupervisorCode())) {
                System.out.println("Неправильный код руководителя");
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
            newUser.setRole(Role.supervisor);
            Integer newUserId = userRepository.register(newUser);
            if (newUserId == null) {
                System.out.println("Не удалось создать руководителя");
                return ErrorResponse.INSTANCE;
            }
            return SuccessResponse.INSTANCE;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ErrorResponse.INSTANCE;
    }

}
