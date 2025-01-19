package com.monopoly.server.services;

import com.monopoly.game.from_Server.message.GameMessage;
import com.monopoly.game.from_Server.service.ServerServiceInterface;
import com.monopoly.server.handler.ClientHandler;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;


public class ServerService implements Runnable, ServerServiceInterface {

    private final ServerSocket serverSocket;
    private final Map<String, Boolean> playerReadyStatus = new HashMap<>();
    private static final List<ClientHandler> clients = new CopyOnWriteArrayList<>();

    private static final List<String> playerNames = new CopyOnWriteArrayList<>();

    public static List<String> getPlayerNames() {
        return playerNames;
    }

    public static final Map<String, String> responseMap = new HashMap<>();

    public void delete(String playerName) {
        responseMap.remove(playerName);
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

    @Override
    public void sendCommandForOneClient(GameMessage gameMessage) {
        broadcast(gameMessage.toString());
    }

    @Override
    public boolean hasResponse(String username) {
        return responseMap.containsKey(username);
    }

    @Override
    public String getResponse(String username) {
        String response = responseMap.get(username);
        responseMap.remove(username);
        return response;
    }

//    public void sendRequestToClient(String clientName, GameMessage message) {
//        for (ClientHandler client : clients) {
//
//            if (client.getNickname().equals(clientName)) {
//                client.sendMessage(message.toString());
//                break;
//            }
//        }
//    }

}