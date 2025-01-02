package com.monopoly.game.manager;

import com.monopoly.game.component.area.PropertyTile;
import com.monopoly.game.component.area.StartTile;
import com.monopoly.game.component.area.Tile;
import com.monopoly.game.component.model.Board;
import com.monopoly.game.component.model.Player;
import com.monopoly.game.component.voucher.PayingRent;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class BoardManager {
    private Board board; //initialize
    private final TileManager tileManager;
    private final EventManager eventManager;


    public void start() {

    }

    public void stop() {

    }

    public void move(int position, Player player) {
        Tile tile = board.getTile(position);
        tileManager.move(tile, player);
    }
}
