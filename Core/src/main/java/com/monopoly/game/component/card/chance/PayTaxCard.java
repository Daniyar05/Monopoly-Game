package com.monopoly.game.component.card.chance;

import com.monopoly.game.component.card.ChanceCard;
import com.monopoly.game.component.model.Player;
import com.monopoly.game.component.money.Cash;
import com.monopoly.game.from_Server.message.GameMessage;
import com.monopoly.game.from_Server.message.MessageType;
import com.monopoly.game.manager.EventManager;

public class PayTaxCard extends ChanceCard {
    private final int amount;

    public PayTaxCard(String description, int amount) {
        super(description);
        this.amount = amount;
    }

    @Override
    public void executeEffect(Player player, EventManager eventManager) {
        player.getWallet().subtractCash(new Cash(amount));

        eventManager.sendCommand(new GameMessage(
                MessageType.UPDATE_BALANCE,
                player.getName(),
                String.valueOf(player.getWallet().getAmount())
        ));
        eventManager.sendCommand(new GameMessage(
                MessageType.NOTIFICATION,
                player.getName(),
                getDescription()
        ));
    }
}
