package com.monopoly.game.manager;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class GameManager {

    private final BoardManager boardManager;
    private final PlayerManager playerManager;

//    private List<Player> players;           // Список игроков
//    private Board board;                    // Игровое поле
//    private int currentPlayerIndex;         // Индекс текущего игрока
//    private GameStatus gameStatus;          // Текущий статус игры (WAITING, IN_PROGRESS, FINISHED)
}
