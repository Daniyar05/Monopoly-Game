package com.monopoly.game.manager;

import com.monopoly.game.from_Server.message.EventEnum;
import com.monopoly.game.from_Server.message.GameMessage;

public interface EventManager {
    boolean choiceYes(EventEnum question);
    void notifyAboutAction(String string, String username);
    void sendCommand(GameMessage gameMessage);
}
