package com.monopoly.server.message;

import com.monopoly.game.Game;
import com.monopoly.game.component.model.Player;
import com.monopoly.game.component.money.Cash;
import com.monopoly.game.config.ConfigurationGame;
import com.monopoly.game.config.TileConfigurator;
import com.monopoly.game.from_Server.message.GameMessage;
import com.monopoly.game.from_Server.message.MessageType;
import com.monopoly.game.from_Server.message.PreparationMessageType;
import com.monopoly.graphics.rendering.EventManagerGUI;
import com.monopoly.server.services.ClientService;
import com.monopoly.server.services.ServerService;

import java.util.ArrayList;
import java.util.List;
import java.util.OptionalInt;

import static com.monopoly.server.services.ServerService.getPlayerNames;

public class GameManagerServer {

    private Game game;
    private static final int BASE_AMOUNT_CASH = 1500;
    private final List<Player> players;
    private ServerService serverService;

    public GameManagerServer() {
        this.players = new ArrayList<>();
    }

    public void addPlayer(String username) {
        players.add(new Player(username, new Cash(BASE_AMOUNT_CASH)));
    }

    public void startGame() {
//        ServerService.broadcast(new GameMessage(PreparationMessageType.GAME_START, getPlayerNames(),"").toString());
        if (game == null) {
            EventManagerGUI eventManager = new EventManagerGUI(serverService);
            this.game = new Game(ConfigurationGame.builder()
                    .players(players)
                    .tiles(TileConfigurator.configureTiles())
                    .build(), eventManager);
            game.start();

            ServerService.broadcast(new GameMessage(PreparationMessageType.GET_ALL_PLAYERS, "Server",String.join(",", getPlayerNames())).toString());

            for (Player player : players) {
                ServerService.broadcast(new GameMessage(
                        MessageType.UPDATE_BALANCE,
                        player.getName(),
                        String.valueOf(player.getWallet().getAmount())
                ).toString());
            }
        } else {
            System.out.println("Game is already running");
        }
    }

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

    public Player sellTile(String playerName, String tileName) {
        return game.sellTile(playerName, tileName);
    }

    public int getPositionTileByName(String tileName) {
        return game.getPositionTileByName(tileName);
    }
}
