package com.monopoly.game.manager;

import com.monopoly.game.component.area.PropertyTile;
import com.monopoly.game.component.area.Tile;
import com.monopoly.game.component.model.Player;
import com.monopoly.game.component.voucher.PayingRent;
import com.monopoly.game.component.voucher.BuyingProperty;
import com.monopoly.game.from_Server.message.EventEnum;
import com.monopoly.game.from_Server.message.GameMessage;
import com.monopoly.game.from_Server.message.MessageType;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class TileManager {
    private final EventManager eventManager;
    public void move(Tile tile, Player player) {
        if (tile instanceof PropertyTile){
            Player owner = ((PropertyTile) tile).getOwner();
            if (owner == null) {
                EventEnum eventEnum = EventEnum.BUY_IT;
                eventEnum.setPlayerName(player.getName());
                if (player.enoughCash(((PropertyTile) tile).getCost()) & eventManager.choiceYes(eventEnum)){
                    eventManager.notifyAboutAction("Buying tile", player.getName()); // FIXME - удалить
                    eventManager.sendCommand(new GameMessage(MessageType.NEW_TILE_OWNER, player.getName(), String.valueOf(tile.getPosition())));

                    BuyingProperty buyingProperty = new BuyingProperty(player,((PropertyTile) tile).getCost(), (PropertyTile) tile);
                    buyingProperty.execute();
                    eventManager.sendCommand(new GameMessage(
                            MessageType.UPDATE_BALANCE,
                            player.getName(),
                            String.valueOf(player.getWallet().getAmount())
                    ));
                } else {
                    eventManager.notifyAboutAction("Rejection buying tile", player.getName()); // FIXME - удалить
                }
            }else if (!player.equals(owner)){
                PayingRent payingRent = new PayingRent(player);
                owner.adjustCash(payingRent, ((PropertyTile) tile));
                eventManager.sendCommand(new GameMessage(
                        MessageType.UPDATE_BALANCE,
                        player.getName(),
                        String.valueOf(player.getWallet().getAmount())
                ));
                eventManager.sendCommand(new GameMessage(
                        MessageType.UPDATE_BALANCE,
                        owner.getName(),
                        String.valueOf(owner.getWallet().getAmount())
                ));
            }
        } else {
            tile.execute(player, eventManager);
        }
    }
}
