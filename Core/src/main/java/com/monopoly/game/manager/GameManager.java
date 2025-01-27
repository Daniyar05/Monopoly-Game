package com.monopoly.game.manager;

import com.monopoly.game.Game;
import com.monopoly.game.component.model.Dice;
import com.monopoly.game.component.model.Player;
import com.monopoly.game.component.money.Cash;
import com.monopoly.game.config.ConfigurationGame;
import com.monopoly.game.from_Server.message.GameMessage;
import com.monopoly.game.from_Server.message.MessageType;
import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor
public class GameManager {

    private final BoardManager boardManager;
    private final PlayerManager playerManager;
    private final EventManager eventManager;


    public void startGame(ConfigurationGame configurationGame) {
        boardManager.start(configurationGame.getTiles());
        playerManager.start(configurationGame.getPlayers());
        notifyNextPlayer();
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
        if (playerNow.isCanBeBankrupt()){
            moveFinish();
        }
        int oldPositionPlayer = playerNow.getPosition();
        if (playerNow.getCountSkipSteps()>0){
            eventManager.sendCommand(new GameMessage(
                    MessageType.NOTIFICATION,
                    playerNow.getName(),
                    "Вы Находитесь в тюрьме и вам осталось сидеть %s хода".formatted(playerNow.getCountSkipSteps())
            ));
            playerNow.reduceCountSkipSteps();
            moveFinish();
            return -1;
        }
        int newPositionPlayer = playerManager.move(step);
        int positionOnBoard = boardManager.move(newPositionPlayer, playerNow);
        if (oldPositionPlayer % boardManager.tileSize() > newPositionPlayer % boardManager.tileSize()){
            playerNow.getWallet().addCash(new Cash(200));
            eventManager.sendCommand(new GameMessage(
                    MessageType.UPDATE_BALANCE,
                    playerNow.getName(),
                    String.valueOf(playerNow.getWallet().getAmount())
            ));
        }
        moveFinish();
        return positionOnBoard;
    }

    private void moveFinish() {
        playerManager.nextPlayer();
        if (checkGameOver()) {
            stopGame();
        }
        notifyNextPlayer();
    }

    public void notifyNextPlayer(){
        eventManager.sendCommand(new GameMessage(
                MessageType.NEXT_PLAYER,
                playerManager.nowPlayer().getName(),
                ""
        ));
        eventManager.sendCommand(new GameMessage(
                    MessageType.NOTIFICATION,
                    playerManager.nowPlayer().getName(),
                    "Сейчас ваш ход"
            ));
    }

    public int rollDice(){
        return Dice.roll();
    }
    private boolean checkGameOver() {
        long activePlayers = playerManager.getPlayers().stream()
                .filter(p -> !p.isBankrupt())
                .count();

        if (activePlayers == 1) {
            Player winner = playerManager.getPlayers().stream()
                    .filter(p -> !p.isBankrupt())
                    .findFirst()
                    .orElseThrow();
            eventManager.sendCommand(
                    new GameMessage(
                            MessageType.GAME_OVER,
                            winner.getName(),
                            "Победил - "+winner.getName()+" с суммой = "+winner.getWallet().getAmount()
                    )
            );
        }
        return activePlayers == 1;
    }
}
