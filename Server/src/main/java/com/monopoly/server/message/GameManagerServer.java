package com.monopoly.server.message;

import com.monopoly.game.Game;
import com.monopoly.game.component.model.Player;
import com.monopoly.game.component.money.Cash;
import com.monopoly.game.config.ConfigurationGame;
import com.monopoly.game.config.TileConfigurator;
import com.monopoly.graphics.rendering.EventManagerGUI;
import com.monopoly.server.services.ClientService;
import com.monopoly.server.services.ServerService;

import java.util.ArrayList;
import java.util.List;

public class GameManagerServer {

    private Game game;
    private static final int BASE_AMOUNT_CASH = 1500;
    private final List<Player> players;
    private ServerService serverService;

    public GameManagerServer() {
        this.players = new ArrayList<>();
    }

//    public GameManagerServer(List<String> playerNames) {
//        this.players = new ArrayList<>();
//        playerNames.forEach(name -> players.add(new Player(name, new Cash(BASE_AMOUNT_CASH))));
//    }

    public void addPlayer(String username) {
        players.add(new Player(username, new Cash(BASE_AMOUNT_CASH)));
    }

    public void startGame() {
//        ServerService.broadcast(new GameMessage(PreparationMessageType.GAME_START, getPlayerNames(),"").toString());
        if (game == null) {
            EventManagerGUI eventManager = new EventManagerGUI(serverService); //TODO исправить null - затычка
            this.game = new Game(ConfigurationGame.builder()
                    .players(players)
                    .tiles(TileConfigurator.configureTiles())
                    .build(), eventManager); //TODO  это окно только на сервере запускается!
            game.start();
        } else {
            System.out.println("Game is already running");
        }
    }

//    private String getPlayerNames() {
//        return String.join(",", players.stream().map(Player::getName).toList());
//    }
//
//    public Game getGame() {
//        return game;
//    }

    public int move(String sender, int step) {
        if (game.getNowPlayer().getName().equals(sender)){
            return game.move(step);
        }
        return -1;
    }

    public void addPlayers(List<String> playerNames) {
        for (String playerName : playerNames) {
            addPlayer(playerName);
        }
    }

    public void setServerService(ServerService serverService) {
        this.serverService=serverService;
    }
}
