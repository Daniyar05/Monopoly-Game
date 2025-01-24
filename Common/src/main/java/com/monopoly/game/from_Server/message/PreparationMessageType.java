package com.monopoly.game.from_Server.message;

import java.util.Scanner;

// Типы сообщений для подготовки к игре
public enum PreparationMessageType {
    READY_STATUS,    // Статус готовности игрока
    ALL_PLAYERS_READY,
    GAME_START,       // Команда на старт игры
    PLAYER_JOIN,
    GET_ALL_PLAYERS
}
