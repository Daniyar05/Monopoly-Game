package com.monopoly.game.manager;

import com.monopoly.game.from_Server.message.Event;
import com.monopoly.game.from_Server.message.EventEnum;
import com.monopoly.game.from_Server.message.GameMessage;

public class EventManagerConsole implements EventManager{

    private final Event event = new Event();

    @Override
    public boolean choiceYes(EventEnum question) {
        return event.createAndListenerYesNoChoice(question.getValue());
    }

    @Override
    public void notifyAboutAction(String string, String username){
        System.out.println(string);
    }

    @Override
    public void sendCommand(GameMessage gameMessage) {

    }
}
