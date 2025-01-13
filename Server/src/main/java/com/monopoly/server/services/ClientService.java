package com.monopoly.server.services;

import com.monopoly.game.from_Server.message.GameMessage;
import com.monopoly.game.from_Server.message.MessageType;
import com.monopoly.game.from_Server.message.PreparationMessageType;
import com.monopoly.game.from_Server.service.ClientServiceInterface;
import com.monopoly.graphics.GameGUI;
import com.monopoly.server.process.WaitingRoom;
import javafx.application.Platform;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import static com.monopoly.game.from_Server.message.MessageType.*;


public class ClientService implements Runnable, ClientServiceInterface {

    private final Socket socket;
    private final PrintWriter out;
    private final BufferedReader in;
    private final String nickname;
    private final Stage primaryStage;
    private GameGUI gameGUI;


    public ClientService(String host, int port, String nickname, Stage primaryStage) {
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
    @Override
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
        if (gameMessage.type() instanceof MessageType) {
            processServerUpdate(message);
            return;
        }

        if (gameMessage.type() instanceof PreparationMessageType prepType) {
            switch (prepType) {
                case ALL_PLAYERS_READY -> {
                    System.out.println("Все игроки готовы! Ожидайте начала игры.");
                    Platform.runLater(() -> {
                        if (primaryStage.getUserData() instanceof WaitingRoom waitingRoom) {
                            waitingRoom.updateAllPlayersReady(true);
                        }
                    });
                }
                case GAME_START -> {
                    closeWaitingRoom();
                    startGame();
                }
                default -> System.err.println("Неизвестное сообщение от сервера: " + message);
            }
        }
    }

    private void processServerUpdate(String message) {
        if (gameGUI.getWindowSetting() == null) {
            System.err.println("WindowSetting не инициализирован.");
            return;
        }
        GameMessage gameMessage = GameMessage.fromString(message);

        Platform.runLater(() -> {
            // Обработка различных типов обновлений
            System.out.println(gameMessage.type());
            if (gameMessage.type() instanceof MessageType prepType) {
                switch (prepType) {
                    case PLAYER_MOVED:
                        System.out.println("PLAYER_MOVED on ClientService");
                        gameGUI.getWindowSetting().updatePlayerPosition(gameMessage.sender(), gameMessage.content());
                        break;
                    case TILE_UPDATED:
                        gameGUI.getWindowSetting().updateTileState(gameMessage.content());
                        break;
                    case GAME_OVER:
                        gameGUI.getWindowSetting().displayGameOver(gameMessage.content());
                        break;
                    case ROLL_DICE:
                        System.out.println("Каким-то чудом попали не туда");
                        gameGUI.getWindowSetting().updatePlayerPosition(gameMessage.sender(), gameMessage.content());
                        break;
                    default:
                        System.err.println("Неизвестный тип сообщения: " + gameMessage.type());
                }
            }
        });
    }

    private void startGame() {
        Platform.runLater(() -> {
            //TODO разные экземпляры Game
//            Main.getGameManagerServer().startGame();

            gameGUI = new GameGUI(nickname, this);
            gameGUI.start(new Stage());
        });
    }

    private void closeWaitingRoom() {
        Platform.runLater(primaryStage::close);
    }
}
