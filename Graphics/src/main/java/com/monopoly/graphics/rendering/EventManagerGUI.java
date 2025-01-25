package com.monopoly.graphics.rendering;

import com.monopoly.game.from_Server.message.EventEnum;
import com.monopoly.game.from_Server.message.GameMessage;
import com.monopoly.game.from_Server.message.MessageType;
import com.monopoly.game.from_Server.service.ServerServiceInterface;
import com.monopoly.game.manager.EventManager;

public class EventManagerGUI implements EventManager {
    private final ServerServiceInterface serverService;

    public EventManagerGUI(ServerServiceInterface serverService) {
        this.serverService = serverService;
    }

    @Override
    public boolean choiceYes(EventEnum question) {
        GameMessage gameMessage = new GameMessage(MessageType.PLAYER_CHOICE, question.getFrom(), question.getValue());
        serverService.sendCommandForOneClient(gameMessage);

        while (!serverService.hasResponse(question.getFrom())) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new RuntimeException("Ошибка ожидания ответа клиента", e);
            }
        }

        boolean result = Boolean.valueOf(serverService.getResponse(question.getFrom()));
        return result;
    }

    @Override
    public void notifyAboutAction(String message, String username) {
        serverService.sendCommandForOneClient(new GameMessage(MessageType.NOTIFICATION, username, message));
    }

    @Override
    public void sendCommand(GameMessage gameMessage) {
        serverService.sendCommandForOneClient(gameMessage);
    }
}


