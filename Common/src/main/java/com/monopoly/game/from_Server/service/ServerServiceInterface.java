package com.monopoly.game.from_Server.service;

import com.monopoly.game.from_Server.message.GameMessage;

public interface ServerServiceInterface {
    void sendCommandForOneClient(GameMessage gameMessage);
    boolean hasResponse(String username);
    String getResponse(String username);
}
