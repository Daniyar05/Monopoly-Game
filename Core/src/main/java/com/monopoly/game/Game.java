package com.monopoly.game;

import com.monopoly.game.action.event.GameStatus;
import com.monopoly.game.config.ConfigurationGame;
import com.monopoly.game.manager.*;

public class Game {
    private final GameManager gameManager;
    private final ConfigurationGame configurationGame;
    private GameStatus gameStatus;

    public Game(ConfigurationGame configurationGame, EventManager eventManager) {
        TileManager tileManager = new TileManager(eventManager);
        BoardManager boardManager = new BoardManager(tileManager, eventManager);
        PlayerManager playerManager = new PlayerManager(configurationGame.getPlayers());
        this.gameManager = new GameManager(boardManager, playerManager, eventManager);
        this.configurationGame = configurationGame;

    }

    public void start(){
        gameManager.startGame(configurationGame);
        gameStatus=GameStatus.WAITING;
    }
    public void stop(){
        gameManager.stopGame();
        gameStatus = GameStatus.FINISHED;
    }
    public void move(){
        if (GameStatus.WAITING.equals(gameStatus)){
            gameStatus = GameStatus.IN_PROGRESS;
            gameManager.move();
            gameStatus=GameStatus.WAITING;
        }
    }
}
