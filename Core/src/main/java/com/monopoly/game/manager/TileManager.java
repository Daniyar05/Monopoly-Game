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
            if (((PropertyTile) tile).getOwner() == null) {
                EventEnum eventEnum = EventEnum.BUY_IT;
                eventEnum.setPlayerName(player.getName());
                if (player.enoughCash(((PropertyTile) tile).getCost()) & eventManager.choiceYes(eventEnum)){
                    eventManager.notifyAboutAction("Buying tile", player.getName()); // FIXME - удалить
                    eventManager.sendCommand(new GameMessage(MessageType.NEW_TILE_OWNER, player.getName(), String.valueOf(tile.getPosition())));

                    BuyingProperty buyingProperty = new BuyingProperty(player,((PropertyTile) tile).getCost(), (PropertyTile) tile);
                    buyingProperty.execute();
                } else {
                    eventManager.notifyAboutAction("Rejection buying tile", player.getName()); // FIXME - удалить
                }
            }else if (!player.equals(((PropertyTile) tile).getOwner())){
                PayingRent payingRent = new PayingRent(player);
                ((PropertyTile) tile).getOwner().adjustCash(payingRent, ((PropertyTile) tile));
            }
        } else {
            tile.execute(player);
        }
    }
}
