package server;

import common.command.ErrorResponse;
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
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Processor extends Thread {

    private final Socket clientSocket;
    private final ObjectInputStream objectInput;
    private final ObjectOutputStream objectOutput;

    private final AdminController adminController;
    private final ClientController clientController;
    private final SupervisorController supervisorController;

    public Processor(Socket clientSocket, ObjectInputStream objectInput, ObjectOutputStream objectOutput) {
        this.clientSocket = clientSocket;
        this.objectInput = objectInput;
        this.objectOutput = objectOutput;

        this.adminController = new AdminController();
        this.clientController = new ClientController();
        this.supervisorController = new SupervisorController();
    }

    public void run() {
        // Регистрация и логин
        while (!Thread.currentThread().isInterrupted()) {
            try {
                Response res = switch (objectInput.readObject()) {
                    case AdminLoginRequest req -> adminController.processAdminLoginRequest(req);
                    case AdminRegisterRequest req -> adminController.processAdminRegisterRequest(req);
                    case ClientLoginRequest req -> clientController.processClientLoginRequest(req);
                    case ClientRegisterRequest req -> clientController.processClientRegisterRequest(req);
                    case SupervisorLoginRequest req -> supervisorController.processSupervisorLoginRequest(req);
                    case SupervisorRegisterRequest req -> supervisorController.processSupervisorRegisterRequest(req);
                    default -> ErrorResponse.INSTANCE;
                };
                objectOutput.writeObject(res);
            } catch (EOFException e) {
                close();
                return;
            } catch (Exception e) {
                if (e instanceof InterruptedException) {
                    close();
                    return;
                }
                e.printStackTrace();
            }
        }
    }

    private void close() {
        try {
            if (this.clientSocket != null) this.clientSocket.close();
            if (this.objectInput != null) this.objectInput.close();
            if (this.objectOutput != null) this.objectOutput.close();
        } catch (IOException e) {
        }
    }

}
