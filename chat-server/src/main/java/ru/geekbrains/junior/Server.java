package ru.geekbrains.junior;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    private final ServerSocket serverSocket;

    public Server(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
    }

    public void runServer() throws IOException {
        while(!serverSocket.isClosed()) {
            Socket socket = serverSocket.accept();
            System.out.println("Подключился новый клиент!");
            ClientManager clientManager = new ClientManager(socket);
            Thread thread = new Thread(clientManager);
            thread.start();
        }
    }
}
