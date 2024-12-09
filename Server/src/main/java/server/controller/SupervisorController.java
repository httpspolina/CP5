package server.controller;

import common.command.CommonErrorResponse;
import common.command.Response;
import common.command.supervisor.*;
import common.model.User;
import common.model.UserRole;
import server.ServerConfig;
import server.db.PasswordHashing;
import server.db.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

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

    public Response findAllUsers() throws Exception {
        List<User> users = userRepository.findAllUsers();

        if (users.isEmpty()) {
            return new CommonErrorResponse("Нет пользователей в системе.");
        }

        List<User> filteredUsers = users.stream()
                .filter(user -> user.getRole() == UserRole.CLIENT || user.getRole() == UserRole.ADMIN)
                .collect(Collectors.toList());

        if (filteredUsers.isEmpty()) {
            return new CommonErrorResponse("Нет пользователей с ролью CLIENT или ADMIN.");
        }

        UsersResponse response = new UsersResponse();
        response.setUsers(filteredUsers);

        return response;
    }

    public Response findUserById(FindUserByIdRequest req) throws Exception {
        if (req.getUserId() == null) {
            return new CommonErrorResponse("Не указан ID пользователя");
        }

        User user = userRepository.findById(req.getUserId());
        if (user == null) {
            return new CommonErrorResponse("Пользователь не найден");
        }

        UserResponse response = new UserResponse();
        response.setUser(user);
        return response;
    }

    public Response deleteUser(DeleteUserRequest req) throws Exception {
        if (req.getUserId() == null) {
            return new CommonErrorResponse("Не указан ID пользователя");
        }

        User user = userRepository.findById(req.getUserId());
        if (user == null) {
            return new CommonErrorResponse("Пользователь не найден");
        }

        boolean success = userRepository.deleteUserById(req.getUserId());
        if (!success) {
            return new CommonErrorResponse("Не удалось удалить пользователя");
        }

        return new CommonErrorResponse("Пользователь успешно удалён");
    }

    public Response findUsersByUsername(FindUserByUsernameRequest req) throws Exception {
        if (req.getUsername() == null || req.getUsername().isEmpty()) {
            return new CommonErrorResponse("Имя пользователя не указано.");
        }

        List<User> users = userRepository.findUsersByUsername(req.getUsername());
        if (users.isEmpty()) {
            return new CommonErrorResponse("Пользователи с таким именем не найдены.");
        }

        UsersResponse response = new UsersResponse();
        response.setUsers(users);
        return response;
    }
}
