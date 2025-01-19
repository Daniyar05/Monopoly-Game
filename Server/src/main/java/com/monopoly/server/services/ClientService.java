package com.monopoly.server.services;

import com.monopoly.game.component.area.PropertyTile;
import com.monopoly.game.component.area.Tile;
import com.monopoly.game.from_Server.message.EventEnum;
import com.monopoly.game.from_Server.message.GameMessage;
import com.monopoly.game.from_Server.message.MessageType;
import com.monopoly.game.from_Server.message.PreparationMessageType;
import com.monopoly.game.from_Server.service.ClientServiceInterface;
import com.monopoly.graphics.GameGUI;
import com.monopoly.graphics.rendering.ClientEventManager;
import com.monopoly.server.process.WaitingRoom;
import javafx.application.Platform;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import static com.monopoly.game.from_Server.message.MessageType.*;


public class ClientService implements Runnable, ClientServiceInterface {

    private final Socket socket;
    private final PrintWriter out;
    private final BufferedReader in;
    private final String nickname;
    private final Stage primaryStage;
    private GameGUI gameGUI;
    private final BlockingQueue<Boolean> responseQueue = new ArrayBlockingQueue<>(1);
    private ClientEventManager clientEventManager;

    public ClientService(String host, int port, String nickname, Stage primaryStage) {
        try {
            this.socket = new Socket(host, port);
            this.nickname = nickname;
            this.out = new PrintWriter(socket.getOutputStream(), true);
            this.clientEventManager = new ClientEventManager(out, nickname);
            this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.primaryStage = primaryStage;
        } catch (IOException e) {
            throw new RuntimeException("Ошибка подключения к серверу: " + e.getMessage(), e);
        }
    }

    @Override
    public void sendEventRequest(EventEnum question) {

    }

    @Override
    public boolean hasResponse() {
        return !responseQueue.isEmpty();
    }

    @Override
    public boolean getResponse() {
        try {
            return responseQueue.take();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Ошибка получения ответа", e);
        }
    }

    @Override
    public void sendNotification(String message) {

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
                    System.out.println("Получил сообщение от сервера о старте игры");
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
            if (gameMessage.type() instanceof MessageType prepType) {
                switch (prepType) {
                    case PLAYER_MOVED:
                        if(!"-1".equals(gameMessage.content())) {
                            gameGUI.getWindowSetting().updatePlayerPosition(gameMessage.sender(), gameMessage.content());
                        }
                        break;
                    case TILE_UPDATED:
                        gameGUI.getWindowSetting().updateTileState(gameMessage.content());
                        break;
                    case NEW_TILE_OWNER:
                        gameGUI.getWindowSetting().updateTileOwner(gameMessage.getSplitContent());
                        break;
                    case GAME_OVER:
                        gameGUI.getWindowSetting().displayGameOver(gameMessage.content());
                        break;
                    case ROLL_DICE:
                        System.out.println("Каким-то чудом попали не туда");
                        gameGUI.getWindowSetting().updatePlayerPosition(gameMessage.sender(), gameMessage.content());
                        break;
                    case PLAYER_CHOICE:
                        System.out.println("Пришло сообщение " + gameMessage);
                        if (nickname.equals(gameMessage.sender())) {
                            System.out.println("Это мое сообщение");
                            clientEventManager.choiceYes(EventEnum.BUY_IT);
                        }
                        break;
                    case NOTIFICATION:
                        System.out.println("Пришло сообщение " + gameMessage);
                        if (nickname.equals(gameMessage.sender())) {
                            System.out.println("Это мое сообщение");
//                            clientEventManager.notifyAboutAction(gameMessage.content(), gameMessage.sender());
                        }
                        break;

                    default:
                        System.err.println("Неизвестный тип сообщения: " + gameMessage.type());
                }
            }
        });
    }

    private void startGame() {
        Platform.runLater(() -> {
            gameGUI = new GameGUI(nickname, this);
            gameGUI.start(new Stage());
        });
    }

    private void closeWaitingRoom() {
        Platform.runLater(primaryStage::close);
    }
}
