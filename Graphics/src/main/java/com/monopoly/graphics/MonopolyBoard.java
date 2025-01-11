package com.monopoly.graphics;

import com.monopoly.game.Game;
import com.monopoly.game.component.model.Player;
import com.monopoly.game.component.money.Cash;
import com.monopoly.game.config.ConfigurationGame;
import com.monopoly.game.config.TileConfigurator;
import com.monopoly.graphics.rendering.EventManagerGUI;
import com.monopoly.graphics.rendering.WindowSettings;
import javafx.application.Application;
import javafx.stage.Stage;

import java.util.List;

public class MonopolyBoard extends Application {

    private static final int TILE_SIZE = 100; // Размер одной клетки
    private static final int BOARD_SIZE = 8; // Количество клеток на стороне
    private static final int WINDOW_SIZE = 800; // Фиксированный размер окна
    private static final int PLAYER_POSITION = 0; // Пример позиции игрока

    @Override
    public void start(Stage primaryStage) {

        Player player1 = new Player("First Player", new Cash(1500));
        Player player2 = new Player("Second Player", new Cash(1500));
        Game game = new Game(ConfigurationGame.builder()
                .players(List.of(player1, player2))
                .tiles(TileConfigurator.configureTiles())
                .build(),
                new EventManagerGUI());
        game.start();

        WindowSettings windowSettings = new WindowSettings(TILE_SIZE, BOARD_SIZE, WINDOW_SIZE,PLAYER_POSITION, primaryStage, game);

//        windowSettings.update(primaryStage);
    }


    public static void main(String[] args) {
        launch(args);
    }
}