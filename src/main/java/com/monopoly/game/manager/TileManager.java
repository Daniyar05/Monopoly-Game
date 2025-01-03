package com.monopoly.game.manager;

import com.monopoly.game.component.area.*;
import com.monopoly.game.component.model.EventEnum;
import com.monopoly.game.component.model.Player;
import com.monopoly.game.component.voucher.BuyingProperty;
import com.monopoly.game.component.voucher.PayingRent;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class TileManager {
    private final EventManager eventManager;
    public void move(Tile tile, Player player) {
        if (tile instanceof PropertyTile){
            if (((PropertyTile) tile).getOwner() == null) {
                if (eventManager.choiceYes(EventEnum.BUY_IT) & player.enoughCash(((PropertyTile) tile).getCost())){
                    System.out.println("Buying tile");
                    //TODO проверить достаточно ли средств
                    BuyingProperty buyingProperty = new BuyingProperty(player,((PropertyTile) tile).getCost(), (PropertyTile) tile);
                    buyingProperty.execute();
                } else {
                    System.out.println("Rejection buying tile");
                }
            }else if (!player.equals(((PropertyTile) tile).getOwner())){
                PayingRent payingRent = new PayingRent(((PropertyTile) tile).getOwner());
                ((PropertyTile) tile).getOwner().adjustCash(payingRent, ((PropertyTile) tile));
            }
        } else {
            tile.execute(player);
        }
    }
}
