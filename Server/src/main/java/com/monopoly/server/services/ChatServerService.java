package com.monopoly.server.services;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class ChatServerService implements Runnable {

    private final ServerSocket serverSocket;
    private static final List<ClientHandler> clients = new CopyOnWriteArrayList<>();

    public ChatServerService(int port) {
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
            System.out.println("Обрабатывается команда: " + command);
            broadcast("Ответ на команду: " + command);
        }

        public void sendMessage(String message) {
            out.println(message);
        }
    }
}
