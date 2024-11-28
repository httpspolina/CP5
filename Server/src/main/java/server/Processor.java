package server;

import common.model.ErrorResponse;
import common.model.Response;
import common.model.SuccessResponse;
import common.model.admin.AdminLoginRequest;
import common.model.admin.AdminRegisterRequest;
import common.model.client.ClientLoginRequest;
import common.model.client.ClientRegisterRequest;
import common.model.supervisor.SupervisorLoginRequest;
import common.model.supervisor.SupervisorRegisterRequest;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Processor extends Thread {

    private final Socket clientSocket;
    private final ObjectInputStream objectInput;
    private final ObjectOutputStream objectOutput;

    public Processor(Socket clientSocket, ObjectInputStream objectInput, ObjectOutputStream objectOutput) {
        this.clientSocket = clientSocket;
        this.objectInput = objectInput;
        this.objectOutput = objectOutput;
    }

    public void run() {
        // Регистрация и логин
        while (!Thread.currentThread().isInterrupted()) {
            try {
                Response res = switch (objectInput.readObject()) {
                    case AdminLoginRequest req -> processAdminLoginRequest(req);
                    case AdminRegisterRequest req -> processAdminRegisterRequest(req);
                    case ClientLoginRequest req -> processClientLoginRequest(req);
                    case ClientRegisterRequest req -> processClientRegisterRequest(req);
                    case SupervisorLoginRequest req -> processSupervisorLoginRequest(req);
                    case SupervisorRegisterRequest req -> processSupervisorRegisterRequest(req);
                    default -> ErrorResponse.INSTANCE;
                };
                objectOutput.writeObject(res);
                if (res instanceof SuccessResponse) {
                    break;
                }
            } catch (Exception e) {
                if (e instanceof InterruptedException) {
                    return;
                }
                e.printStackTrace();
            }
        }

        // Очистка ресурсов
        try {
            this.clientSocket.close();
            this.objectInput.close();
            this.objectOutput.close();
        } catch (IOException e) {
        }
    }

}
