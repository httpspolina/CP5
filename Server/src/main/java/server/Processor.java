package server;

import common.command.CommonErrorResponse;
import common.command.Response;
import common.command.admin.AdminLoginRequest;
import common.command.admin.AdminRegisterRequest;
import common.command.client.ClientLoginRequest;
import common.command.client.ClientRegisterRequest;
import common.command.supervisor.SupervisorLoginRequest;
import common.command.supervisor.SupervisorRegisterRequest;
import server.controller.AdminController;
import server.controller.ClientController;
import server.controller.SupervisorController;

import java.io.EOFException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;

public class Processor extends Thread {

    private final Socket clientSocket;
    private final ObjectInputStream objectInput;
    private final ObjectOutputStream objectOutput;

    private final AdminController adminController = new AdminController();
    private final ClientController clientController = new ClientController();
    private final SupervisorController supervisorController = new SupervisorController();

    public Processor(Socket clientSocket, ObjectInputStream objectInput, ObjectOutputStream objectOutput) {
        this.clientSocket = clientSocket;
        this.objectInput = objectInput;
        this.objectOutput = objectOutput;
    }

    public void run() {
        try {
            print("Клиент подключен");
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    Object o = objectInput.readObject();
                    print("Запрос: " + o);

                    Response res;
                    try {
                        res = switch (o) {
                            case AdminLoginRequest req -> adminController.login(req);
                            case AdminRegisterRequest req -> adminController.register(req);
                            case ClientLoginRequest req -> clientController.login(req);
                            case ClientRegisterRequest req -> clientController.register(req);
                            case SupervisorLoginRequest req -> supervisorController.login(req);
                            case SupervisorRegisterRequest req -> supervisorController.register(req);
                            default -> new CommonErrorResponse("Неподдерживаемый запрос: " + o);
                        };
                    } catch (Exception e) {
                        res = new CommonErrorResponse(e.getMessage());
                        e.printStackTrace();
                    }

                    print("Ответ: " + res);
                    objectOutput.writeObject(res);
                } catch (SocketException | EOFException e) {
                    return;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } finally {
            try {
                print("Клиент отключен");
                this.clientSocket.close();
                this.objectInput.close();
                this.objectOutput.close();
            } catch (Exception ignore) {
            }
        }
    }

    private void print(String x) {
        System.out.println("[" + clientSocket.getPort() + "] " + x);
    }

}
