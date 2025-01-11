package com.monopoly.server.util;

import java.io.*;
import java.net.Socket;

public class ClientHandler implements Runnable {
    private Socket clientSocket;
    private Server server;
    private PrintWriter out;
    private BufferedReader in;
    private int clientId;

    public ClientHandler(Socket socket, Server server) {
        this.clientSocket = socket;
        this.server = server;
    }

    @Override
    public void run() {
        try {
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            out = new PrintWriter(clientSocket.getOutputStream(), true);

            // Идентификация клиента
            out.println("Enter your name: ");
            String name = in.readLine();
            clientId = name.hashCode(); // Простая генерация ID клиента
            server.addClient(clientId, this);
            server.broadcast("Player " + name + " joined the game.");

            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                System.out.println("Received: " + inputLine);
                server.broadcast("Player " + name + ": " + inputLine);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            server.removeClient(clientId);
            try {
                clientSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void sendMessage(String message) {
        out.println(message);
    }
}
