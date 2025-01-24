package com.monopoly.game.component.model;

import com.monopoly.game.component.area.Tile;
import lombok.Getter;

import java.util.List;

public class Board {
    @Getter
    private final List<Tile> tiles;

    public Board(List<Tile> tiles) {
        this.tiles = tiles;
    }

    public Tile getTile(int position) {
        return tiles.get(position % tiles.size());

    }
}
