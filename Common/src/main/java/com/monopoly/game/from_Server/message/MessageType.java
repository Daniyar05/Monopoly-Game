package com.monopoly.game.from_Server.message;

public enum MessageType {
    PLAYER_MOVED,     // Ход игрока
    NEW_TILE_OWNER,
    GAME_OVER,
    ROLL_DICE,
    PLAYER_CHOICE,
    NOTIFICATION,
    UPDATE_BALANCE,
    SELL_TILE,
    NEXT_PLAYER
}
