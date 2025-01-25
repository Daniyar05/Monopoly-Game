package com.monopoly.game.component.card;

import com.monopoly.game.component.model.Player;
import com.monopoly.game.manager.EventManager;
import lombok.Getter;

@Getter
public class CommunityChestCard extends AbstractCard {
    private final String description;

    public CommunityChestCard(String description) {
        this.description = description;
    }

    public void executeEffect(Player player, EventManager eventManager) {}
}
