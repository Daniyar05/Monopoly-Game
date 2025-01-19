package com.monopoly.game.from_Server.service;

import com.monopoly.game.from_Server.message.GameMessage;

public interface ServerServiceInterface {
    public void sendCommandForOneClient(GameMessage gameMessage);

    public boolean hasResponse(String username);

    public String getResponse(String username);
}
