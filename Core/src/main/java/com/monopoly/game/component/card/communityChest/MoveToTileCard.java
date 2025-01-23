package com.monopoly.game.component.card.communityChest;

import com.monopoly.game.component.card.ChanceCard;
import com.monopoly.game.component.card.CommunityChestCard;
import com.monopoly.game.component.model.Player;
import com.monopoly.game.component.money.Cash;
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
        eventManager.sendCommand(new GameMessage(
                MessageType.NOTIFICATION,
                player.getName(),
                getDescription()
        ));
        player.changePosition(targetPosition - player.getPosition());
        if (player.getPosition() == 10) {
            executeLogicJail(player, eventManager);
        }
        else if (player.getPosition() == 0) {
            executeLogicStart(player, eventManager);
        }
        eventManager.sendCommand(new GameMessage(
                MessageType.PLAYER_MOVED,
                player.getName(),
                String.valueOf(player.getPosition())
        ));
    }

    private void executeLogicJail(Player player, EventManager eventManager) {
        if (player.isHasJailFreeCard()){
            eventManager.sendCommand(new GameMessage(
                    MessageType.NOTIFICATION,
                    player.getName(),
                    "Вы попали в тюрьму. Но у вас была карта, чтобы ее покинуть."
            ));
        }
        else {
            player.setCountSkipSteps(3);
            player.getWallet().subtractCash(new Cash(50));
            eventManager.sendCommand(new GameMessage(
                    MessageType.UPDATE_BALANCE,
                    player.getName(),
                    String.valueOf(player.getWallet().getAmount())
            ));
            eventManager.sendCommand(new GameMessage(
                    MessageType.NOTIFICATION,
                    player.getName(),
                    "Вы попали в тюрьму и пропустите 3 хода. А также у вас списано $50"
            ));
        }
    }
    private void executeLogicStart(Player player, EventManager eventManager) {
        player.getWallet().addCash(new Cash(200));
        eventManager.sendCommand(new GameMessage(
                MessageType.UPDATE_BALANCE,
                player.getName(),
                String.valueOf(player.getWallet().getAmount())
        ));
    }

}