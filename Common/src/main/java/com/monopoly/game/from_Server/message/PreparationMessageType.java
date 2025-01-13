package com.monopoly.game.from_Server.message;

// Типы сообщений для подготовки к игре
public enum PreparationMessageType {
    READY_STATUS,    // Статус готовности игрока
    ALL_PLAYERS_READY,
    GAME_START,       // Команда на старт игры
    GO_START,
    PLAYER_JOIN
}
