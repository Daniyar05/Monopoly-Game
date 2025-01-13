package com.monopoly.server.handler;

import com.monopoly.game.from_Server.message.GameMessage;
import com.monopoly.game.from_Server.message.MessageType;
import com.monopoly.game.from_Server.message.MessageType.*;

import com.monopoly.game.from_Server.message.PreparationMessageType;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.List;
import java.util.Map;

import static com.monopoly.server.Main.getGameManagerServer;
import static com.monopoly.server.services.ServerService.*;

public class ClientHandler implements Runnable {
    private final Map<String, Boolean> playerReadyStatus;
    private final List<String> playerNames;
    private final Socket clientSocket;
    private final PrintWriter out;
    private final BufferedReader in;


    public ClientHandler(Socket socket, Map<String, Boolean> playerReadyStatus, List<String> playerNames) {
        this.playerNames=playerNames;
        this.playerReadyStatus = playerReadyStatus;
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
                    getGameManagerServer().startGame();
                    broadcast(new GameMessage(PreparationMessageType.GAME_START, "Server", String.join(",", getPlayerNames())).toString());
                }
                case PLAYER_JOIN -> {
                    String playerName = message.sender();
                    playerReadyStatus.put(playerName, false);
                }

            }
        } else if (message.type() instanceof MessageType gameType) {
            switch (gameType) {
                case PLAYER_MOVED -> {
                    System.out.println("Ход игрока: " + message.content());
                }
                case TILE_UPDATED -> {
                    String playerName = message.sender();
                }
                case ROLL_DICE -> {
                    System.out.println("ROLL_DICE is can");
                    int position = getGameManagerServer().move(message.sender());
                    if (position != -1) {
                        broadcast(new GameMessage(
                                MessageType.PLAYER_MOVED,
                                message.sender(),
                                String.valueOf(position)
                        ).toString());
                    }
                }

            }
        } else {
            System.err.println("Неизвестный тип сообщения: " + message.type());
        }
    }

    public void sendMessage(String message) {
        out.println(message);
    }
    private void checkAllPlayersReady() {
        if (playerReadyStatus.values().stream().allMatch(ready -> ready)) {
            broadcast(new GameMessage(PreparationMessageType.ALL_PLAYERS_READY, "Server", "").toString());
        }
    }
}