package com.monopoly.server.services;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

public class ChatServerService implements Runnable {

    private final ServerSocket serverSocket;
    private static final Map<String, Boolean> playerReadyStatus = new HashMap<>();
    private static final List<ClientHandler> clients = new CopyOnWriteArrayList<>();

    private static final List<String> playerNames = new CopyOnWriteArrayList<>();
    public static List<String> getPlayerNames(){return playerNames;}

    public ChatServerService(int port) {
        try {
            this.serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            throw new RuntimeException("Не удалось открыть серверный сокет на порту " + port, e);
        }
    }
    public void setPlayerReady(String player, boolean isReady) {
        playerReadyStatus.put(player, isReady);
        checkAllPlayersReady();
    }
    private static void checkAllPlayersReady() {
        if (playerReadyStatus.values().stream().allMatch(ready -> ready)) {
            broadcast("ALL_PLAYERS_READY");
//            broadcast("START_GAME:" + String.join(",", playerNames)); // Отправляем список имён
        }
    }
    @Override
    public void run() {
        System.out.println("Сервер запущен и ожидает подключения...");
        while (true) {
            try {
                Socket clientSocket = serverSocket.accept();
                ClientHandler clientHandler = new ClientHandler(clientSocket);
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

    private static class ClientHandler implements Runnable {
        private final Socket clientSocket;
        private final PrintWriter out;
        private final BufferedReader in;

        private ClientHandler(Socket socket) {
            try {
                this.clientSocket = socket;
                this.out = new PrintWriter(socket.getOutputStream(), true);
                this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            } catch (IOException e) {
                throw new RuntimeException("Ошибка при создании обработчика клиента", e);
            }
        }

        @Override
        public void run() {
            try {
                String clientMessage;
                while ((clientMessage = in.readLine()) != null) {
                    System.out.println("Получено от клиента: " + clientMessage);
                    // Обработка команд клиента
                    handleClientCommand(clientMessage);
                }
            } catch (IOException e) {
                System.err.println("Клиент отключился: " + e.getMessage());
            } finally {
                try {
                    clientSocket.close();
                } catch (IOException e) {
                    System.err.println("Ошибка при закрытии соединения с клиентом: " + e.getMessage());
                }
            }
        }

        private void handleClientCommand(String command) {
            if (command.contains("JOIN:")) {
                String[] parts = command.split(":");
                String playerName = parts[2];

                playerNames.add(playerName);
                playerReadyStatus.put(playerName, false);
                System.out.println("Игрок присоединился: " + playerName);
            } else if (command.contains("READY:")) {
                String[] parts = command.split(":");
                String playerName = parts[2];
                boolean isReady = Boolean.parseBoolean(parts[3]);
                playerReadyStatus.put(playerName, isReady);
                checkAllPlayersReady();
            } else if (command.contains("START_GAME:")) {
                System.out.println("Получена команда START_GAME");
                broadcast("START_GAME:"); // Отправляем всем клиентам команду на запуск игры
            } else {
                System.err.println("Не обрабатываемая команда"+command);
            }
        }

        public void sendMessage(String message) {
            out.println(message);
        }
    }
}
