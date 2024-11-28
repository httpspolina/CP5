package server.controller;

import common.command.ErrorResponse;
import common.command.Response;
import common.command.SuccessResponse;
import common.command.client.ClientLoginRequest;
import common.command.client.ClientRegisterRequest;
import common.command.client.ClientResponse;
import common.model.Client;
import common.model.Role;
import server.db.ClientRepository;
import server.db.PasswordHashing;

public class ClientController {

    private final ClientRepository clientRepository;

    public ClientController() {
        this.clientRepository = new ClientRepository();
    }

    public Response processClientLoginRequest(ClientLoginRequest req) {
        try {
            Client foundClient = clientRepository.findByUsername(req.getUsername().toLowerCase());
            if (foundClient == null || foundClient.getRole() != Role.client || !PasswordHashing.verifyPassword(req.getPassword(), foundClient.getPassword())) {
                System.out.println("Неверное имя пользователя или пароль");
                return ErrorResponse.INSTANCE;
            }
            return new ClientResponse(foundClient);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ErrorResponse.INSTANCE;
    }

    public Response processClientRegisterRequest(ClientRegisterRequest req) {
        try {
            Client foundClient = clientRepository.findByUsername(req.getUsername().toLowerCase());
            if (foundClient != null) {
                System.out.println("Пользователь с таким именем уже существует");
                return ErrorResponse.INSTANCE;
            }
            Client newClient = new Client();
            newClient.setUsername(req.getUsername().toLowerCase());
            newClient.setPassword(PasswordHashing.hashPassword(req.getPassword()));
            newClient.setRole(Role.client);
            newClient.setName(req.getName());
            newClient.setEmail(req.getEmail().toLowerCase());
            newClient.setPhone(req.getPhone());
            Integer newUserId = clientRepository.register(newClient);
            if (newUserId == null) {
                System.out.println("Не удалось создать клиента");
                return ErrorResponse.INSTANCE;
            }
            return SuccessResponse.INSTANCE;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ErrorResponse.INSTANCE;
    }

}
