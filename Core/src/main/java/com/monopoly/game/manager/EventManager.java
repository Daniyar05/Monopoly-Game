package com.monopoly.game.manager;

import com.monopoly.game.from_Server.message.EventEnum;

public interface EventManager {
    boolean choiceYes(EventEnum question);

    void notifyAboutAction(String string, String username);
}
