package com.monopoly.graphics.rendering;

import com.monopoly.game.Game;
import com.monopoly.game.component.area.Tile;
import com.monopoly.game.config.TileConfigurator;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

import java.util.List;

public class WindowSettingForGUI {
    private int TILE_SIZE = 100; // Размер одной клетки
    private int BOARD_SIZE = 8; // Количество клеток на стороне
    private int WINDOW_SIZE = 800; // Фиксированный размер окна
    private int PLAYER_POSITION = 0; // Пример позиции игрока

    public WindowSettingForGUI(int TILE_SIZE, int BOARD_SIZE, int WINDOW_SIZE, int PLAYER_POSITION, Stage primaryStage) {
        this.TILE_SIZE=TILE_SIZE;
        this.BOARD_SIZE=BOARD_SIZE;
        this.WINDOW_SIZE=WINDOW_SIZE;
        this.PLAYER_POSITION=PLAYER_POSITION;
        update(primaryStage);
//        // Основной контейнер
//        BorderPane mainLayout = new BorderPane();
//        // Создание игрового поля
//        GridPane grid = new GridPane();
//        List<Tile> tiles = TileConfigurator.configureTiles();
//        createBoard(grid, tiles);
//
//        createDice(game,grid, 2,2);
//        settings(grid);
//        // Добавление игрового поля в центральную часть
//        mainLayout.setCenter(grid);
//        // Создание сцены
//        Scene scene = new Scene(mainLayout, WINDOW_SIZE, WINDOW_SIZE);
//        primaryStage.setTitle("Monopoly Board");
//        primaryStage.setScene(scene);
//        primaryStage.show();
    }

    private void createBoard(GridPane grid, List<Tile> tiles) {
        int count = 0;
        for (int i = BOARD_SIZE - 1; i >= 0; i--) {
            createTileButton(tiles.get(count++), grid, i, BOARD_SIZE - 1);
        }
        for (int i = BOARD_SIZE - 2; i >= 0; i--) {
            createTileButton(tiles.get(count++), grid, 0, i);
        }
        for (int i = 1; i < BOARD_SIZE; i++) {
            createTileButton(tiles.get(count++), grid, i, 0);
        }
        for (int i = 1; i < BOARD_SIZE - 1; i++) {
            createTileButton(tiles.get(count++), grid, BOARD_SIZE - 1, i);
        }
    }


    private void settings(GridPane grid) {
        // Настройка пропорций ячеек
        for (int i = 0; i < BOARD_SIZE; i++) {
            ColumnConstraints colConstraints = new ColumnConstraints();
            colConstraints.setPercentWidth(100.0 / BOARD_SIZE);
            grid.getColumnConstraints().add(colConstraints);

            RowConstraints rowConstraints = new RowConstraints();
            rowConstraints.setPercentHeight(100.0 / BOARD_SIZE);
            grid.getRowConstraints().add(rowConstraints);
        }
    }

//    private void createDice(Game game, GridPane grid, int x, int y){
//        Button button = new Button("Dice");
//        button.setPrefSize(TILE_SIZE, TILE_SIZE);
//        button.setOnAction(e -> {
//            button.setDisable(true);
//            new Thread(() -> {
//                game.move();
//                Platform.runLater(() -> button.setDisable(false));
//            }).start();
//
//        });
//        grid.add(button, x, y);
//    }

    private void createTileButton(Tile tile, GridPane grid, int x, int y) {
        Button button = new Button(tile.getName());
        button.setPrefSize(TILE_SIZE, TILE_SIZE);
        button.setOnAction(e -> openTileDescription(tile));

        // Добавляем точку, если клетка соответствует местоположению игрока
        if (isPlayerOnTile(x, y)) {
            Circle circle = new Circle(10, Color.RED); // точка игрока
            button.setGraphic(circle);
        }
        grid.add(button, x, y);
    }

    private boolean isPlayerOnTile(int x, int y) {
        // Проверим, находится ли игрок на этой клетке (например, игрок на клетке 0,0)
        return (x == 0 && y == 0); // Пример для игрока в начале (0,0)
    }

    private void openTileDescription(Tile tile) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Tile Description");
        alert.setHeaderText(tile.getName());
        alert.setContentText("Description: " + tile.getName());
        alert.showAndWait();
    }

    public void update(Stage primaryStage) {
        // Основной контейнер
        BorderPane mainLayout = new BorderPane();
        // Создание игрового поля
        GridPane grid = new GridPane();
        List<Tile> tiles = TileConfigurator.configureTiles();
        createBoard(grid, tiles);

//        createDice(game,grid, 2,2);

        settings(grid);
        // Добавление игрового поля в центральную часть
        mainLayout.setCenter(grid);
        // Создание сцены
        Scene scene = new Scene(mainLayout, WINDOW_SIZE, WINDOW_SIZE);
        primaryStage.setTitle("Monopoly Board");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
