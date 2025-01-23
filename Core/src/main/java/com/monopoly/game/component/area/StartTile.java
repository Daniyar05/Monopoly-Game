package com.monopoly.game.component.area;

import com.monopoly.game.component.model.Player;
import com.monopoly.game.component.money.Cash;
import com.monopoly.game.manager.EventManager;

public class StartTile extends Tile{
    private final Cash baseCash=new Cash(200);

    public StartTile(int position) {
        super(position, "Go");
    }
    @Override
    public void execute(Player player, EventManager eventManager) {
        player.getWallet().addCash(baseCash);
    }
}
