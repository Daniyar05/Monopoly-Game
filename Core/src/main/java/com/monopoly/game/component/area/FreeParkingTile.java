package com.monopoly.game.component.area;

import com.monopoly.game.component.model.Player;

public class FreeParkingTile extends Tile{
    public FreeParkingTile(int position, String name) {
        super(position, name);
    }
    @Override
    public void execute(Player player) {
        return; //отдохни
    }
}
