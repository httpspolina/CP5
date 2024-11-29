package server.controller;

import common.command.CommonErrorResponse;
import common.command.Response;
import common.command.supervisor.SupervisorLoginRequest;
import common.command.supervisor.SupervisorRegisterRequest;
import common.command.supervisor.SupervisorResponse;
import common.model.User;
import common.model.UserRole;
import server.ServerConfig;
import server.db.PasswordHashing;
import server.db.UserRepository;

public class SupervisorController {

    private final UserRepository userRepository = new UserRepository();

    public Response login(SupervisorLoginRequest req) throws Exception {
        User foundUser = userRepository.findByUsername(req.getUsername().toLowerCase());
        if (foundUser == null || foundUser.getRole() != UserRole.SUPERVISOR || !PasswordHashing.verifyPassword(req.getPassword(), foundUser.getPassword())) {
            return new CommonErrorResponse("Неправильное имя пользователя или пароль");
        }
        return new SupervisorResponse(foundUser);
    }

    public Response register(SupervisorRegisterRequest req) throws Exception {
        if (!req.getCode().equals(ServerConfig.INSTANCE.getSupervisorCode())) {
            return new CommonErrorResponse("Неправильный код руководителя");
        }
        User existingUser = userRepository.findByUsername(req.getUsername().toLowerCase());
        if (existingUser != null) {
            return new CommonErrorResponse("Пользователь с таким именем уже существует");
        }
        User newUser = new User();
        newUser.setUsername(req.getUsername().toLowerCase());
        newUser.setPassword(PasswordHashing.hashPassword(req.getPassword()));
        newUser.setRole(UserRole.SUPERVISOR);
        Integer newUserId = userRepository.create(newUser);
        if (newUserId == null) {
            return new CommonErrorResponse("Не удалось создать руководителя");
        }
        User foundUser = userRepository.findByUsername(req.getUsername().toLowerCase());
        return new SupervisorResponse(foundUser);
    }

}
