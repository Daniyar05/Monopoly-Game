package com.monopoly.server.services;

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
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;


public class ClientService implements Runnable, ClientServiceInterface {

    private final Socket socket;
    private final PrintWriter out;
    private final BufferedReader in;
    private final String nickname;
    private final Stage primaryStage;
    private GameGUI gameGUI;
    private final BlockingQueue<Boolean> responseQueue = new ArrayBlockingQueue<>(1);
    private ClientEventManager clientEventManager;
    private List<String> listPlayers;
    private Map<String, Integer> playerBalances = new HashMap<>(); // Добавьте данные о деньгах игроков
    private String nowPlayer;

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
                case GET_ALL_PLAYERS -> {
                    this.listPlayers = Arrays.asList(gameMessage.content().split(","));
                }
                default -> System.err.println("Неизвестное сообщение от сервера: " + message);
            }
        }
    }
    @Override
    public Map<String, Integer> getPlayerBalances() {
        return playerBalances;
    }

    private void processServerUpdate(String message) {
        if (GameMessage.fromString(message).type()!=(MessageType.UPDATE_BALANCE)
                && GameMessage.fromString(message).type()!=(MessageType.NEXT_PLAYER)
                && GameMessage.fromString(message).type()!=(MessageType.NOTIFICATION)
                && gameGUI.getWindowSetting() == null) {
            System.err.println("WindowSetting не инициализирован.");
            return;
        }
        GameMessage gameMessage = GameMessage.fromString(message);

        Platform.runLater(() -> {
            // Обработка различных типов обновлений
            if (gameMessage.type() instanceof MessageType prepType) {
                switch (prepType) {
                    case PLAYER_MOVED ->{
                        if(!"-1".equals(gameMessage.content())) {
                            gameGUI.updatePlayerPosition(gameMessage.sender(), gameMessage.content());
                        }
                    }

                    case TILE_UPDATED->{
                        gameGUI.getWindowSetting().updateTileState(gameMessage.content());
                    }
                    case NEW_TILE_OWNER->{
                        gameGUI.getWindowSetting().updateTileOwner(gameMessage);
                    }
                    case GAME_OVER ->{
                        gameGUI.getWindowSetting().displayGameOver(gameMessage.content());
                    }
                    case ROLL_DICE ->{
                        System.out.println("Каким-то чудом попали не туда");
                        gameGUI.getWindowSetting().updatePlayerPosition(gameMessage.sender(), gameMessage.content());
                    }
                    case PLAYER_CHOICE ->{
                        System.out.println("Пришло сообщение " + gameMessage);
                        if (nickname.equals(gameMessage.sender())) {
                            clientEventManager.choiceYes(EventEnum.BUY_IT);
                        }
                    }
                    case NOTIFICATION -> {
                        System.out.println("Пришло сообщение " + gameMessage);
                        if (nickname.equals(gameMessage.sender())) {
                            clientEventManager.notifyAboutAction(gameMessage.content(), gameMessage.sender());
                        }
                    }
                    case UPDATE_BALANCE -> {
                        playerBalances.put(gameMessage.sender(), Integer.parseInt(gameMessage.content()));
                    }
                    case SELL_TILE -> {
                        System.out.println("Клиент получил сообщения об продаже поля");
                        gameGUI.getWindowSetting().deleteTileOwner(gameMessage.content());
                    }
                    case NEXT_PLAYER -> {
                        nowPlayer = gameMessage.sender();
                        if (this.gameGUI != null){
                            gameGUI.getWindowSetting().updatePlayerPosition(nowPlayer);
                        }
                    }
                    default->{
                        System.err.println("Неизвестный тип сообщения: " + gameMessage.type());
                    }
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

    @Override
    public List<String> getListPlayers() {
        return listPlayers;
    }

    @Override
    public String getNowPlayerName() {
        return nowPlayer;
    }


}
