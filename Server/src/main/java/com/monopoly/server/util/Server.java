package com.monopoly.server.util;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;

public class Server {
    private static final int PORT = 12345; // Порт для подключения
    private ConcurrentHashMap<Integer, ClientHandler> clients = new ConcurrentHashMap<>(); // Активные клиенты

    public void start() {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Server started on port " + PORT);

            while (true) {
                Socket clientSocket = serverSocket.accept(); // Принимаем клиента
                ClientHandler clientHandler = new ClientHandler(clientSocket, this);
                new Thread(clientHandler).start(); // Новый поток для клиента
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public synchronized void broadcast(String message) {
        // Рассылка сообщения всем клиентам
        for (ClientHandler client : clients.values()) {
            client.sendMessage(message);
        }
    }

    public void addClient(int clientId, ClientHandler clientHandler) {
        clients.put(clientId, clientHandler);
    }

    public void removeClient(int clientId) {
        clients.remove(clientId);
    }

    public static void main(String[] args) {
        new Server().start();
    }
}

