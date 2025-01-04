package com.monopoly.game.manager;

import com.monopoly.game.component.model.Event;
import com.monopoly.game.component.model.EventEnum;

public class EventManager {

    private final Event event = new Event();

    public boolean choiceYes(EventEnum question) {
        return event.createAndListenerYesNoChoice(question.getValue());
    }

    public void notifyAboutAction(String string){
        System.out.println(string);
    }
}
