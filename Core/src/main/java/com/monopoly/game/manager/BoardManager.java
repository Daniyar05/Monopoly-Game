package com.monopoly.game.manager;

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
    public int tileSize(){
        return board.getTiles().size();
    }

    public int move(int position, Player player) {
        Tile tile = board.getTile(position);
//        eventManager.notifyAboutAction("Move on BoardManager - "+tile.getPosition(), player.getName());
        tileManager.move(tile, player);
        return player.getPosition();
    }

    public Tile getTileByName(String tileName) {
        if (board == null || board.getTiles() == null) {
            throw new IllegalStateException("Игровое поле не инициализировано");
        }
        return board.getTiles().stream()
                .filter(tile -> tileName != null && tileName.equals(tile.getName()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Клетка '" + tileName + "' не найдена"));
    }
}
