package com.monopoly.game.from_Server.service;

import com.monopoly.game.from_Server.message.GameMessage;

import java.util.List;
import java.util.Map;

public interface ClientServiceInterface {
    void sendCommand(GameMessage message);
    Map<String, Integer> getPlayerBalances();
    List<String> getListPlayers();
    String getNowPlayerName();
}
