package com.monopoly.server.message;

import com.monopoly.game.Game;
import com.monopoly.game.component.model.Player;
import com.monopoly.game.component.money.Cash;
import com.monopoly.game.config.ConfigurationGame;
import com.monopoly.game.config.TileConfigurator;
import com.monopoly.game.manager.EventManagerConsole;
import com.monopoly.graphics.rendering.EventManagerGUI;
import com.monopoly.server.services.ChatServerService;

import java.util.ArrayList;
import java.util.List;

public class GameManagerServer {
    private Game game;
    private final int baseAmountCash = 1500;
    private List<Player> players;
    public GameManagerServer() {
        players=new ArrayList<>();
    }


    public void addPlayer(String username){
        players.add(new Player(username, new Cash(baseAmountCash)));
    }

    public GameManagerServer(List<String> namePlayer) {
        List<Player> players = new ArrayList<>();
        namePlayer.forEach(x->players.add(new Player(x,new Cash(baseAmountCash))));
        this.players=players;
    }

    public void startGame() {
        ChatServerService.broadcast("START_GAME");
        this.game = new Game(ConfigurationGame.builder()
                .players(players)
                .tiles(TileConfigurator.configureTiles())
                .build(), new EventManagerGUI());

        game.start(); // Запускаем серверную логику игры
    }

    public Game getGame() {
        return game;
    }
}
