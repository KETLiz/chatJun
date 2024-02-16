package ru.geekbrains.junior;

import java.io.*;
import java.net.Socket;

public class Client {
    private final Socket socket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    private final String name;
    public Client(Socket socket, String name) {
        this.socket = socket;
        this.name = name;

        try {
            bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException e) {
            closeEverything(socket, bufferedWriter, bufferedReader);
        }
    }

    public void listenForMessage() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String message;
                while(socket.isConnected()) {
                    try {
                        message = bufferedReader.readLine();
                        System.out.println(message);
                    } catch(Exception e) {
                        closeEverything(socket, bufferedWriter, bufferedReader);
                    }

                }
            }
        }).start();
    }

    public void sendMessage() {
        try {

        }
    }

    private void closeEverything(Socket socket, BufferedWriter bw, BufferedReader br) {
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
            e.printStackTrace();
        }
    }
}
