package com.monopoly.game.manager;

import com.monopoly.game.action.processGame.EventMoveGame;
import com.monopoly.game.component.area.Tile;
import com.monopoly.game.component.model.Board;
import com.monopoly.game.component.model.Player;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class BoardManager {
    private Board board; //initialize
    private final TileManager tileManager;
    @Getter
    private final EventManager eventManager;

    public void start(List<Tile> tiles) {
        board = new Board(tiles);
    }

    public void stop() {

    }

    public int move(int position, Player player) {
        Tile tile = board.getTile(position);
        eventManager.notifyAboutAction("Move on BoardManager - "+tile.getPosition(), player.getName());
        tileManager.move(tile, player);
        return tile.getPosition();
    }

    public void moveToJail(Player player){
        //TODO реализовать
    }

}
