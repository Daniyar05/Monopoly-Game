package com.monopoly.game.manager;

import com.monopoly.game.component.model.Dice;
import com.monopoly.game.component.model.Player;
import com.monopoly.game.config.ConfigurationGame;
import lombok.RequiredArgsConstructor;




@RequiredArgsConstructor
public class GameManager {

    private final BoardManager boardManager;
    private final PlayerManager playerManager;

    public void startGame(ConfigurationGame configurationGame) {
        boardManager.start(configurationGame.getTiles());
        playerManager.start(configurationGame.getPlayers());
    }

    public void stopGame() {
        boardManager.stop();
        playerManager.stop();
    }

    public void move(){
        Player playerNow = playerManager.nowPlayer();
        int step = rollDice();
        int positionOnBoard = playerManager.move(step);
        boardManager.move(positionOnBoard, playerNow);
        moveFinish();
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
