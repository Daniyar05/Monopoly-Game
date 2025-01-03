package com.monopoly.game;

import com.monopoly.game.manager.*;
import com.monopoly.game.config.ConfigurationGame;

public class Game {
    private final GameManager gameManager;
    private final ConfigurationGame configurationGame;

    public Game(ConfigurationGame configurationGame) {
        EventManager eventManager = new EventManager();
        TileManager tileManager = new TileManager(eventManager);
        BoardManager boardManager = new BoardManager(tileManager, eventManager);
        PlayerManager playerManager = new PlayerManager(configurationGame.getPlayers());
        this.gameManager = new GameManager(boardManager, playerManager);
        this.configurationGame = configurationGame;

    }

    public void start(){
        gameManager.startGame(configurationGame);
    }
    public void stop(){
        gameManager.stopGame();
    }
    public void move(){
        gameManager.move();
    }
}
