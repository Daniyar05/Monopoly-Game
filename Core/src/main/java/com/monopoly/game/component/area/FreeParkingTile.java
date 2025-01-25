package com.monopoly.game.component.area;

import com.monopoly.game.component.model.Player;
import com.monopoly.game.from_Server.message.GameMessage;
import com.monopoly.game.from_Server.message.MessageType;
import com.monopoly.game.manager.EventManager;

public class FreeParkingTile extends Tile{
    public FreeParkingTile(int position, String name) {
        super(position, name);
    }
    @Override
    public void execute(Player player, EventManager eventManager) {
        eventManager.sendCommand(new GameMessage(
                MessageType.NOTIFICATION,
                player.getName(),
                "Отдохни"
        ));
    }
}
