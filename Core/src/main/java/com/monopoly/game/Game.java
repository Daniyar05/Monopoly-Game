package com.monopoly.game;

import com.monopoly.game.action.event.GameStatus;
import com.monopoly.game.component.model.Player;
import com.monopoly.game.config.ConfigurationGame;
import com.monopoly.game.manager.*;

public class Game {
    private final GameManager gameManager;
    private final ConfigurationGame configurationGame;
    private final TileManager tileManager;
    private final BoardManager boardManager;
    private final PlayerManager playerManager;
    private GameStatus gameStatus;

    public Game(ConfigurationGame configurationGame, EventManager eventManager) {
        tileManager = new TileManager(eventManager);
        boardManager = new BoardManager(tileManager, eventManager);
        playerManager = new PlayerManager(configurationGame.getPlayers());
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
    public int move(){
        if (GameStatus.WAITING.equals(gameStatus)){
            System.out.println("Waiting for move in old function");
            gameStatus = GameStatus.IN_PROGRESS;
            int position = gameManager.move();
            gameStatus=GameStatus.WAITING;
            return position;
        }
        return -1;
    }
    public int move(int step){
        if (GameStatus.WAITING.equals(gameStatus)){
            gameStatus = GameStatus.IN_PROGRESS;
            int position = gameManager.move(step);
            gameStatus=GameStatus.WAITING;
            return position;
        }
        return -1;
    }

    public Player getNowPlayer() {
        return playerManager.nowPlayer();
    }
}
