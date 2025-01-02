package com.monopoly.game;

import com.monopoly.game.component.model.Player;
import com.monopoly.game.component.money.Cash;
import com.monopoly.game.util.ConfigurationGame;

import java.util.List;

public class Main {

    public static void main(String[] args) {
        Player player1 = new Player("First Player", new Cash(100));
        Player player2 = new Player("Second Player", new Cash(100));
        Game game = new Game(ConfigurationGame.builder()
                .players(List.of(player1, player2))
                .build());
    }
}
