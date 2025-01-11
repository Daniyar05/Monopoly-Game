package com.monopoly.game.manager;

import com.monopoly.game.component.model.EventEnum;

public interface EventManager {
    boolean choiceYes(EventEnum question);

    void notifyAboutAction(String string);
}
