package com.monopoly.game.from_Server.service;

import com.monopoly.game.from_Server.message.EventEnum;
import com.monopoly.game.from_Server.message.GameMessage;

import java.util.List;
import java.util.Map;

public interface ClientServiceInterface {

    void sendEventRequest(EventEnum question); // Отправить запрос клиенту
    boolean hasResponse(); // Проверить, есть ли ответ
    boolean getResponse(); // Получить ответ
    void sendNotification(String message); // Отправить уведомление клиенту

//    void sendMessage(String s);

    void sendCommand(GameMessage message);

    Map<String, Integer> getPlayerBalances();

    List<String> getListPlayers();

    String getNowPlayerName();
}
