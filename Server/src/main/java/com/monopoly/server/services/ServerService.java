package com.monopoly.server.services;

import com.monopoly.server.handler.ClientHandler;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;


public class ServerService implements Runnable {

    private final ServerSocket serverSocket;
    private final Map<String, Boolean> playerReadyStatus = new HashMap<>();
    private static final List<ClientHandler> clients = new CopyOnWriteArrayList<>();

    private static final List<String> playerNames = new CopyOnWriteArrayList<>();

    public static List<String> getPlayerNames() {
        return playerNames;
    }

    public ServerService(int port) {
        try {
            this.serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            throw new RuntimeException("Не удалось открыть серверный сокет на порту " + port, e);
        }
    }

    @Override
    public void run() {
        System.out.println("Сервер запущен и ожидает подключения...");
        while (true) {
            try {
                Socket clientSocket = serverSocket.accept();
                ClientHandler clientHandler = new ClientHandler(clientSocket, playerReadyStatus, playerNames);
                clients.add(clientHandler);
                new Thread(clientHandler).start();
            } catch (IOException e) {
                System.err.println("Ошибка при подключении клиента: " + e.getMessage());
            }
        }
    }

    public static void broadcast(String message) {
        for (ClientHandler client : clients) {
            client.sendMessage(message);
        }
    }

}