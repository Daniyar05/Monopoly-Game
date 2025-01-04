package com.monopoly.game.component.area;

import com.monopoly.game.component.model.Player;

public class JailTile extends Tile{
    public JailTile(int position) {
        super(position, "Jail");
    }

    @Override
    public void execute(Player player) {
        if (player.getCountSkipSteps() > 0){
            player.reduceCountSkipSteps();
        } else {
            //TODO добавить реализацию
        }
    }
}
