package com.monopoly.game.manager;

import com.monopoly.game.component.model.Event;
import com.monopoly.game.component.model.EventEnum;

public class EventManagerConsole implements EventManager{

    private final Event event = new Event();

    @Override
    public boolean choiceYes(EventEnum question) {
        return event.createAndListenerYesNoChoice(question.getValue());
    }

    @Override
    public void notifyAboutAction(String string){
        System.out.println(string);
    }
}
