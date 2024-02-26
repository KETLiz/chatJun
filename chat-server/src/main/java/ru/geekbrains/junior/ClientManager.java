package ru.geekbrains.junior;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class ClientManager implements Runnable{
    private Socket socket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    private String name;
    public static ArrayList<ClientManager> clients = new ArrayList<>();

    public ClientManager(Socket socket) {
        try {
            this.socket = socket;
            bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            clients.add(this);
            name = bufferedReader.readLine();
            System.out.println(name + " подключился к беседе.");
            broadCastMessage("Server: " + name + " подключился к беседе.");
        } catch (Exception e) {
            closeEverything(socket, bufferedWriter, bufferedReader);
        }
    }

    private void closeEverything(Socket socket, BufferedWriter bw, BufferedReader br) {
        removeClient();
        try {
            if(socket != null) {
                socket.close();
            }
            if(bw != null) {
                bw.close();
            }
            if(br != null) {
                br.close();
            }
        } catch (IOException e) {
            closeEverything(socket, bufferedWriter, bufferedReader);
        }
    }

    private void removeClient() {
        clients.remove(this);
        System.out.println(name + " покинул чат.");
        broadCastMessage("Server: " + name + " покинул чат.");
    }

    private void broadCastMessage(String message) {
        for(ClientManager client : clients) {
            try {
                if(!client.name.equals(this.name) && message!=null) {
                    client.bufferedWriter.write(message);
                    client.bufferedWriter.newLine();
                    client.bufferedWriter.flush();
                }
            } catch (Exception e) {
                closeEverything(socket, bufferedWriter, bufferedReader);
            }
        }
    }

    @Override
    public void run() {
        String messageFromClient;
        while(!socket.isClosed()) {
            try {
                messageFromClient = bufferedReader.readLine();
                broadCastMessage(messageFromClient);
            } catch (Exception e) {
                closeEverything(socket, bufferedWriter, bufferedReader);
            }
        }
    }
}
