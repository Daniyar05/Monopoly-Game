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
        if (tile instanceof PropertyTile propertyTile){
            Player owner = propertyTile.getOwner();
            if (owner == null) {
                sendCommandForMove(player.getName(), String.valueOf(tile.getPosition()));
                EventEnum eventEnum = EventEnum.BUY_IT;
                eventEnum.setPlayerName(player.getName());
                if (player.enoughCash(propertyTile.getCost()) & eventManager.choiceYes(eventEnum)){
                    eventManager.notifyAboutAction("Buying tile", player.getName()); // FIXME - удалить
                    eventManager.sendCommand(new GameMessage(MessageType.NEW_TILE_OWNER, player.getName(), String.valueOf(tile.getPosition())));

                    BuyingProperty buyingProperty = new BuyingProperty(player,propertyTile.getCost(), propertyTile);
                    buyingProperty.execute();
                    eventManager.sendCommand(new GameMessage(
                            MessageType.UPDATE_BALANCE,
                            player.getName(),
                            String.valueOf(player.getWallet().getAmount())
                    ));
                } else {
                    eventManager.notifyAboutAction("Вы отказались покупать ", player.getName()); // FIXME - удалить
                }
            }else if (!player.equals(owner)){
                PayingRent payingRent = new PayingRent(player);
                owner.adjustCash(payingRent, propertyTile);
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
                eventManager.notifyAboutAction(
                        "Вы попали на поле %s и уплатил $%s игроку %s".formatted(tile.getName(), payingRent.getCash().getAmount(), owner.getName()),
                        player.getName());
//                eventManager.sendCommand(new GameMessage(
//                        MessageType.NOTIFICATION,
//                        player.getName(),
//                        "Вы попали на поле %s и уплатил $%s игроку %s".formatted(tile.getName(), payingRent.getCash().getAmount(), owner.getName())
//                ));
                eventManager.notifyAboutAction(
                        "Игрок %s попал на ваше поле %s и уплатил $%s".formatted(player.getName(), tile.getName(), payingRent.getCash().getAmount()),
                        owner.getName());
//                eventManager.sendCommand(new GameMessage(
//                        MessageType.NOTIFICATION,
//                        owner.getName(),
//                        "Игрок %s попал на ваше поле %s и уплатил $%s".formatted(player.getName(), tile.getName(), payingRent.getCash().getAmount())
//                ));
            }
        }

        else {
            tile.execute(player, eventManager);
        }
    }

    private void sendCommandForMove(String player, String position) {
        eventManager.sendCommand(new GameMessage(
                MessageType.PLAYER_MOVED,
                player,
                position
        ));
    }
}