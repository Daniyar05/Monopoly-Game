package com.monopoly.game.from_Server.service;

import com.monopoly.game.from_Server.message.EventEnum;
import com.monopoly.game.from_Server.message.GameMessage;

public interface ClientServiceInterface {

    void sendEventRequest(EventEnum question); // Отправить запрос клиенту
    boolean hasResponse(); // Проверить, есть ли ответ
    boolean getResponse(); // Получить ответ клиента
    void sendNotification(String message); // Отправить уведомление клиенту

//    void sendMessage(String s);

    void sendCommand(GameMessage message);

}
