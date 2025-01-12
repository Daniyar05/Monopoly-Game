package com.monopoly.server.services;

import com.monopoly.game.Game;
import com.monopoly.graphics.GameGUI;
import com.monopoly.server.message.GameManagerServer;
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
    private Game game;
    private final Stage primaryStage; // Stage для текущего окна

    public ChatClientService(String host, int port, String nickname, Stage primaryStage) {
        try {
            this.socket = new Socket(host, port);
            this.nickname = nickname;
            this.out = new PrintWriter(socket.getOutputStream(), true);
            this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.primaryStage=primaryStage;
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
            sendCommand("JOIN:" + nickname); // Сообщаем серверу имя игрока
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
        if (message.equals("ALL_PLAYERS_READY")) {
            System.out.println("Все игроки готовы! Ожидайте начала игры.");
        } else if (message.contains("START_GAME:")) {
            closeWaitingRoom();
            String playerNames = message.substring(11); // Получаем список имён игроков
            System.out.println("Players - "+playerNames+" message - "+message);
            startGame(playerNames.split(","));
//            System.out.println("Игра начинается!");
//            Platform.runLater(() -> {
//                GameGUI gameGUI = new GameGUI();
//                gameGUI.start(new Stage());
//            });
        }
    }
    private void startGame(String[] playerNames) {
        Platform.runLater(() -> {
            List<String> names = Arrays.asList(playerNames);
            GameManagerServer gameManagerServer = new GameManagerServer(names);
            Game game = gameManagerServer.getGame();
            GameGUI gameGUI = new GameGUI(game, nickname);
            gameGUI.start(new Stage());
        });
    }
    private void closeWaitingRoom() {
        Platform.runLater(() -> {
            // Закрыть текущий экран (WaitingRoom)
            primaryStage.close();
        });
    }
}
