package com.monopoly.game.component.card.chance;

import com.monopoly.game.component.card.ChanceCard;
import com.monopoly.game.component.model.Player;
import com.monopoly.game.from_Server.message.GameMessage;
import com.monopoly.game.from_Server.message.MessageType;
import com.monopoly.game.manager.EventManager;

public class GetOutOfJailCard extends ChanceCard {
    public GetOutOfJailCard(String description) {
        super(description);
    }

    @Override
    public void executeEffect(Player player, EventManager eventManager) {
        player.addJailFreeCard();
        eventManager.sendCommand(new GameMessage(
                MessageType.NOTIFICATION,
                player.getName(),
                getDescription()
        ));
    }
}