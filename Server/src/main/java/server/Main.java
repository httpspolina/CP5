package server;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Main {
    private static final int PORT = ServerConfig.getInstance().getServerPort();

    public static void main(String[] args) throws Exception {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Сервер запущен. Ожидание подключения...");

            while (!Thread.currentThread().isInterrupted()) {
                try {
                    Socket clientSocket = serverSocket.accept();
                    ObjectInputStream objectInput = new ObjectInputStream(clientSocket.getInputStream());
                    ObjectOutputStream objectOutput = new ObjectOutputStream(clientSocket.getOutputStream());

                    System.out.println("Клиент подключен: " + clientSocket.getInetAddress());
                    new Processor(clientSocket, objectInput, objectOutput).start();
                } catch (Exception e) {
                    if (e instanceof InterruptedException) {
                        return;
                    }
                    e.printStackTrace();
                }
            }
        }
    }
}
