package com.monopoly.game.component.area;

import com.monopoly.game.component.model.Player;
import com.monopoly.game.component.money.Cash;

public class StartTile extends Tile{
    private final Cash baseCash=new Cash(200);

    public StartTile(int position, String name) {
        super(position, name);
    }
    @Override
    public void execute(Player player) {
        player.getCash().addCash(baseCash);
    }
}
