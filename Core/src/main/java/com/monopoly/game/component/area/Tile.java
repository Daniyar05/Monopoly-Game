package com.monopoly.game.component.area;

import com.monopoly.game.component.model.Player;
import lombok.Getter;

@Getter
public abstract class Tile {
    protected int position;
    protected String name;

    public Tile(int position, String name) {
        this.position = position;
        this.name = name;
    }

    public void execute(Player player) {}
}
