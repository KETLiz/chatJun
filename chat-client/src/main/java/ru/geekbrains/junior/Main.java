package ru.geekbrains.junior;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws IOException {
        System.out.print("Введите ваше имя: ");

        Scanner sc = new Scanner(System.in);
        String name = sc.nextLine();
        InetAddress address = InetAddress.getLocalHost();
        Socket socket = new Socket(address, 3500);
        Client client = new Client(socket, name);

        InetAddress inetAddress = socket.getInetAddress();

        System.out.println("InetAddress: " + inetAddress);
        String remoteIp = inetAddress.getHostAddress();
        System.out.println("Remote IP: " + remoteIp);
        System.out.println("LocalPort: " + socket.getLocalPort());

        client.listenForMessage();

    }
}