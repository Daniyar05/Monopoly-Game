package com.monopoly.server.services;

import com.monopoly.game.Game;
import com.monopoly.graphics.GameGUI;
import com.monopoly.server.message.GameManagerServer;
import com.monopoly.server.message.GameMessage;
import com.monopoly.server.message.MessageType;
import com.monopoly.server.message.PreparationMessageType;
import javafx.application.Platform;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Arrays;
import java.util.List;

public class ChatClientService implements Runnable {

    private final Socket socket;
    private final PrintWriter out;
    private final BufferedReader in;
    private final String nickname;
    private final Stage primaryStage;
    private Game game;

    public ChatClientService(String host, int port, String nickname, Stage primaryStage) {
        try {
            this.socket = new Socket(host, port);
            this.nickname = nickname;
            this.out = new PrintWriter(socket.getOutputStream(), true);
            this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.primaryStage = primaryStage;
        } catch (IOException e) {
            throw new RuntimeException("Ошибка подключения к серверу: " + e.getMessage(), e);
        }
    }

    public void sendCommand(GameMessage message) {
        out.println(message.toString());
    }

    @Override
    public void run() {
        try {
            sendCommand(new GameMessage(PreparationMessageType.PLAYER_JOIN, nickname, ""));
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
        GameMessage gameMessage = GameMessage.fromString(message);

        if (gameMessage.type() == PreparationMessageType.ALL_PLAYERS_READY) {
            System.out.println("Все игроки готовы! Ожидайте начала игры.");
        } else if (gameMessage.type() == PreparationMessageType.GAME_START) {
            System.out.println(gameMessage.sender());
            closeWaitingRoom();
//            String[] playerNames = gameMessage.content().split(",");
            startGame();
        } else {
            System.err.println("Неизвестное сообщение от сервера: " + message);
        }
    }

    private void startGame() {
        Platform.runLater(() -> {
//            List<String> names = Arrays.asList(playerName);
//            GameManagerServer gameManagerServer = new GameManagerServer(names);
//            this.game = gameManagerServer.getGame();

            GameGUI gameGUI = new GameGUI(game, nickname);
            gameGUI.start(new Stage());
        });
    }

    private void closeWaitingRoom() {
        System.out.println("closable");
        Platform.runLater(primaryStage::close);
    }
}
