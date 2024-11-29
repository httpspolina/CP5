package server.controller;

import common.command.CommonErrorResponse;
import common.command.Response;
import common.command.client.ClientLoginRequest;
import common.command.client.ClientRegisterRequest;
import common.command.client.ClientResponse;
import common.model.Client;
import common.model.UserRole;
import server.db.ClientRepository;
import server.db.PasswordHashing;

public class ClientController {

    private final ClientRepository clientRepository;

    public ClientController() {
        this.clientRepository = new ClientRepository();
    }

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

}
