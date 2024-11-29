package server.controller;

import common.command.CommonErrorResponse;
import common.command.Response;
import common.command.client.*;
import common.model.Client;
import common.model.Film;
import common.model.UserRole;
import server.db.ClientRepository;
import server.db.FilmRepository;
import server.db.PasswordHashing;

import java.util.List;

public class ClientController {

    private final ClientRepository clientRepository = new ClientRepository();
    private final FilmRepository filmRepository = new FilmRepository();

    public Response login(ClientLoginRequest req) throws Exception {
        Client foundClient = clientRepository.findByUsername(req.getUsername().toLowerCase());
        if (foundClient == null || foundClient.getRole() != UserRole.CLIENT || !PasswordHashing.verifyPassword(req.getPassword(), foundClient.getPassword())) {
            return new CommonErrorResponse("Неправильное имя пользователя или пароль");
        }
        return new ClientResponse(foundClient);
    }

    public Response register(ClientRegisterRequest req) throws Exception {
        Client existingClient = clientRepository.findByUsername(req.getUsername().toLowerCase());
        if (existingClient != null) {
            return new CommonErrorResponse("Пользователь с таким именем уже существует");
        }
        Client newClient = new Client();
        newClient.setUsername(req.getUsername().toLowerCase());
        newClient.setPassword(PasswordHashing.hashPassword(req.getPassword()));
        newClient.setRole(UserRole.CLIENT);
        newClient.setName(req.getName());
        newClient.setEmail(req.getEmail().toLowerCase());
        newClient.setPhone(req.getPhone());
        Integer newUserId = clientRepository.create(newClient);
        if (newUserId == null) {
            return new CommonErrorResponse("Не удалось создать клиента");
        }
        Client foundClient = clientRepository.findByUsername(req.getUsername().toLowerCase());
        return new ClientResponse(foundClient);
    }

    public Response getAllFilms(GetAllFilmsRequest req) throws Exception {
        List<Film> films = filmRepository.findAll();
        return new GetAllFilmsResponse(films);
    }

}
