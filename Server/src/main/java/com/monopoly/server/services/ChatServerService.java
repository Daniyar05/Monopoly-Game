package com.monopoly.server.services;

import com.monopoly.server.message.GameMessage;
import com.monopoly.server.message.MessageType;
import com.monopoly.server.message.PreparationMessageType;

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

    public static List<String> getPlayerNames() {
        return playerNames;
    }

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
            broadcast(new GameMessage(PreparationMessageType.ALL_PLAYERS_READY, "Server", "").toString());
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
            GameMessage message = GameMessage.fromString(command);

            if (message.type() instanceof PreparationMessageType prepType) {
                switch (prepType) {
                    case READY_STATUS -> {
                        String contentParts = message.content();
                        String playerName = message.sender();
                        boolean isReady = Boolean.parseBoolean(contentParts);
                        playerReadyStatus.put(playerName, isReady);
                        if (!playerNames.contains(playerName)) {
                            playerNames.add(playerName);
                        }
                        checkAllPlayersReady();
                    }
                    case ALL_PLAYERS_READY -> {
                        System.out.println("Все игроки готовы!");
                    }
                    case GAME_START -> {
                        System.out.println("Начинаем игру!");
                        broadcast(new GameMessage(PreparationMessageType.GAME_START, "Server", String.join(",", getPlayerNames())).toString());
                    }
                }
            } else if (message.type() instanceof MessageType gameType) {
                switch (gameType) {
                    case PLAYER_MOVE -> {
                        System.out.println("Ход игрока: " + message.content());
                    }
                }
            } else {
                System.err.println("Неизвестный тип сообщения: " + message.type());
            }
        }

        public void sendMessage(String message) {
            out.println(message);
        }
    }
}