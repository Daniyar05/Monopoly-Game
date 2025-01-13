package com.monopoly.game.from_Server.service;

import com.monopoly.game.from_Server.message.GameMessage;

public interface ClientServiceInterface {
//    void sendMessage(String s);

    void sendCommand(GameMessage message);

}
