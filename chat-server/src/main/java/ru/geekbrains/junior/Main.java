package ru.geekbrains.junior;


import java.io.IOException;
import java.net.ServerSocket;

public class Main {
    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(3500);
        Server server = new Server(serverSocket);
        System.out.println("mur");
    }
}