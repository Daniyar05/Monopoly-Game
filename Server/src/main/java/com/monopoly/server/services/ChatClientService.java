package com.monopoly.server.services;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ChatClientService implements Runnable {

    private final Socket socket;
    private final PrintWriter out;
    private final BufferedReader in;
    private final String nickname;

    public ChatClientService(String host, int port, String nickname) {
        try {
            this.socket = new Socket(host, port);
            this.nickname = nickname;
            this.out = new PrintWriter(socket.getOutputStream(), true);
            this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException e) {
            throw new RuntimeException("Ошибка подключения к серверу: " + e.getMessage(), e);
        }
    }

    public void sendCommand(String command) {
        out.println(nickname + ": " + command);
    }

    @Override
    public void run() {
        try {
            String serverResponse;
            while ((serverResponse = in.readLine()) != null) {
                processServerMessage(serverResponse);
            }
        } catch (IOException e) {
            System.err.println("Соединение с сервером потеряно.");
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                System.err.println("Ошибка при закрытии сокета: " + e.getMessage());
            }
        }
    }

    private void processServerMessage(String message) {
        // Здесь обрабатываются сообщения, полученные с сервера
        System.out.println("Сообщение от сервера: " + message);
    }
}
