package server;

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
        while (!Thread.currentThread().isInterrupted()) {
            try {
                // ...

            } catch (Exception e) {
                if (e instanceof InterruptedException) {
                    break;
                }
                e.printStackTrace();
            }
        }
        try {
            this.clientSocket.close();
            this.objectInput.close();
            this.objectOutput.close();
        } catch (IOException e) {
        }
    }

}
