package com.monopoly.game.manager;

import com.monopoly.game.component.model.Dice;
import com.monopoly.game.component.model.Player;
import com.monopoly.game.config.ConfigurationGame;
import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor
public class GameManager {

    private final BoardManager boardManager;
    private final PlayerManager playerManager;
    private final EventManager eventManager;



    public void startGame(ConfigurationGame configurationGame) {
        boardManager.start(configurationGame.getTiles());
        playerManager.start(configurationGame.getPlayers());
    }

    public void stopGame() {
        boardManager.stop();
        playerManager.stop();
    }

    public int move(){
        Player playerNow = playerManager.nowPlayer();
        eventManager.notifyAboutAction("Player - '"+playerNow.getName()+"' start moved", playerNow.getName());
        int step = rollDice();
        int positionPlayer = playerManager.move(step);
        int positionOnBoard = boardManager.move(positionPlayer, playerNow);
        moveFinish();
        eventManager.notifyAboutAction("Player - '"+playerNow.getName()+"' finish moved", playerNow.getName());
        return positionOnBoard;
    }

    public int move(int step){
        Player playerNow = playerManager.nowPlayer();
//        eventManager.notifyAboutAction("Player - '"+playerNow.getName()+"' start moved", playerNow.getName());
        int positionPlayer = playerManager.move(step);
        int positionOnBoard = boardManager.move(positionPlayer, playerNow);
        moveFinish();
//        eventManager.notifyAboutAction("Player - '"+playerNow.getName()+"' finish moved", playerNow.getName());
        return positionOnBoard;
    }

    private void moveFinish() {
        playerManager.nextPlayer();
    }

    public int rollDice(){
        return Dice.roll();
    }


//    private List<Player> players;           // Список игроков
//    private Board board;                    // Игровое поле
//    private int currentPlayerIndex;         // Индекс текущего игрока
//    private GameStatus gameStatus;          // Текущий статус игры (WAITING, IN_PROGRESS, FINISHED)
}
