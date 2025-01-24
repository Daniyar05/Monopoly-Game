package com.monopoly.game.component.area;

import com.monopoly.game.component.model.Player;
import com.monopoly.game.manager.EventManager;

public class JailTile extends Tile{
    public JailTile(int position) {
        super(position, "Jail");
    }

    @Override
    public void execute(Player player, EventManager eventManager) {

    }
}
