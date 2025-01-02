package com.monopoly.game.manager;

import com.monopoly.game.component.area.*;
import com.monopoly.game.component.model.Player;
import com.monopoly.game.component.voucher.PayingRent;

public class TileManager {

    public void move(Tile tile, Player player) {
        if (tile instanceof PropertyTile){
            if (!player.equals(((PropertyTile) tile).getOwner())){
                PayingRent payingRent = new PayingRent(((PropertyTile) tile).getOwner());
                ((PropertyTile) tile).getOwner().adjustCash(payingRent, ((PropertyTile) tile));
            }
        } else {
            tile.execute(player);
        }
    }
}
