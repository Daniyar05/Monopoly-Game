package com.monopoly.game.component.area;

import com.monopoly.game.component.model.Player;
import com.monopoly.game.component.money.Cash;
import com.monopoly.game.config.TileConfigurator;
import com.monopoly.game.from_Server.message.GameMessage;
import com.monopoly.game.from_Server.message.MessageType;
import com.monopoly.game.manager.EventManager;

public class GoToJailTile extends Tile{
    public GoToJailTile(int position) {
        super(position, "Go to Jail");
    }

    @Override
    public void execute(Player player, EventManager eventManager) {
        TileConfigurator.configureTiles().forEach(tile -> {
            if (tile instanceof JailTile jailTile) {
                player.setPosition(jailTile.getPosition());
                eventManager.sendCommand(new GameMessage(
                        MessageType.PLAYER_MOVED,
                        player.getName(),
                        String.valueOf(player.getPosition())
                ));
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

        });
    }
}
