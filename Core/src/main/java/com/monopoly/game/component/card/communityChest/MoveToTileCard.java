package com.monopoly.game.component.card.communityChest;

import com.monopoly.game.component.card.ChanceCard;
import com.monopoly.game.component.card.CommunityChestCard;
import com.monopoly.game.component.model.Player;
import com.monopoly.game.from_Server.message.GameMessage;
import com.monopoly.game.from_Server.message.MessageType;
import com.monopoly.game.manager.EventManager;

public class MoveToTileCard extends CommunityChestCard {
    private final int targetPosition;

    public MoveToTileCard(String description, int targetPosition) {
        super(description);
        this.targetPosition = targetPosition;
    }

    @Override
    public void executeEffect(Player player, EventManager eventManager) {
        player.changePosition(targetPosition - player.getPosition());
        eventManager.sendCommand(new GameMessage(
                MessageType.PLAYER_MOVED,
                player.getName(),
                String.valueOf(player.getPosition())
        ));
        eventManager.sendCommand(new GameMessage(
                MessageType.NOTIFICATION,
                player.getName(),
                getDescription()
        ));
    }
}