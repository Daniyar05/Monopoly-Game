package com.monopoly.game;

import com.monopoly.game.manager.*;
import com.monopoly.game.util.ConfigurationGame;

public class Game {
    private final GameManager gameManager;
    private final ConfigurationGame configurationGame;

    public Game(ConfigurationGame configurationGame) {
//        MoneyManager moneyManager = new MoneyManager();
        EventManager eventManager = new EventManager();
        TileManager tileManager = new TileManager();
        BoardManager boardManager = new BoardManager(tileManager, eventManager);
        PlayerManager playerManager = new PlayerManager();
        this.gameManager = new GameManager(boardManager, playerManager);
        this.configurationGame = configurationGame;
    }
    public void start(){
        gameManager.startGame(configurationGame);
    }
    public void stop(){
        gameManager.stopGame();
    }
}
