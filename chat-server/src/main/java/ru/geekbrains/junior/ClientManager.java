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
                if(!client.equals(this) && message!=null) {
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

                String[] message = messageFromClient.split(" ");
                String missingNameWithSymbol = null;
                String missingSymbol = null;
                for(int i = 0; i < message.length; i++) {
                    missingNameWithSymbol = message[1];
                    missingSymbol = String.valueOf(missingNameWithSymbol.charAt(0));
                }
                if(missingSymbol.equals("@")) {
                    String missingName = missingNameWithSymbol.substring(1);
                    ClientManager missingClient = findClientByName(missingName);
                    if(missingClient != null) {
                        missingClient.bufferedWriter.write(messageFromClient);
                        missingClient.bufferedWriter.newLine();
                        missingClient.bufferedWriter.flush();
                    } else {
                        this.bufferedWriter.write("Пользователь с таким именем не найден. Сообщение не отправлено");
                        this.bufferedWriter.newLine();
                        this.bufferedWriter.flush();
                    }

//                        int i = 0;
//                        while(i < clients.size()) {
//                            if(clients.get(i).name.equals(missingName)) {
//                                clients.get(i).bufferedWriter.write(messageFromClient);
//                                clients.get(i).bufferedWriter.newLine();
//                                clients.get(i).bufferedWriter.flush();
//                            }
//                        }
//                        for(ClientManager client : clients) {
//                            if(client.name.equals(missingName)) {
//                                client.bufferedWriter.write(messageFromClient);
//                                client.bufferedWriter.newLine();
//                                client.bufferedWriter.flush();
//                            }
//                        }
                } else {
                        broadCastMessage(messageFromClient);
                    }
            } catch (IOException e) {
                closeEverything(socket, bufferedWriter, bufferedReader);
            }
        }
    }

    private ClientManager findClientByName(String missingName) {
        for(ClientManager client : clients) {
            if(client.name.equals(missingName)) {
                return client;
            }
        }
        return null;
    }
}
