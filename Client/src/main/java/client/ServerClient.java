package client;

import common.command.Request;
import common.command.Response;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ServerClient {

    public static final ServerClient INSTANCE = new ServerClient();

    private Socket socket;
    private ObjectOutputStream objectOutput;
    private ObjectInputStream objectInput;
    private boolean connected;

    private ServerClient() {
    }

    public synchronized void connect() throws Exception {
        if (connected) disconnect();
        this.socket = new Socket(ClientConfig.INSTANCE.getServerHost(), ClientConfig.INSTANCE.getServerPort());
        this.objectOutput = new ObjectOutputStream(socket.getOutputStream());
        this.objectInput = new ObjectInputStream(socket.getInputStream());
        this.connected = true;
    }

    public Response call(Request request) throws Exception {
        try {
            print("Запрос: " + request);
            objectOutput.writeObject(request);
            Response response = (Response) objectInput.readObject();
            print("Ответ: " + response);
            return response;
        } catch (Exception e) {
            disconnect();
            throw e;
        }
    }

    public synchronized void disconnect() {
        if (!connected) return;
        try {
            this.socket.close();
        } catch (Exception ignore) {
        }
        try {
            this.objectOutput.close();
        } catch (Exception ignore) {
        }
        try {
            this.objectInput.close();
        } catch (Exception ignore) {
        }
        this.socket = null;
        this.objectOutput = null;
        this.objectInput = null;
        this.connected = false;
    }

    private void print(String x) {
        System.out.println("[" + this.socket.getPort() + "] " + x);
    }

    public synchronized boolean isConnected() {
        return connected;
    }
}
