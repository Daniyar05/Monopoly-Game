package com.monopoly.graphics;

import com.monopoly.game.component.area.Tile;
import com.monopoly.game.config.TileConfigurator;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.List;

public class MonopolyBoard extends Application {

    private static final int TILE_SIZE = 100; // Размер одной клетки
    private static final int BOARD_SIZE = 8; // Количество клеток на стороне
    private static final int WINDOW_SIZE = 800; // Фиксированный размер окна
    private final List<Tile> tiles = TileConfigurator.configureTiles();
    @Override
    public void start(Stage primaryStage) {
        GridPane grid = new GridPane();
        grid.setGridLinesVisible(true);

        int count = 1;

        // Нижняя строка (справа налево)
        for (int i = BOARD_SIZE - 1; i >= 0; i--) {
            grid.add(createTile("Tile " + count++), i, BOARD_SIZE - 1);
        }

        // Левая колонка (снизу вверх)
        for (int i = BOARD_SIZE - 2; i >= 0; i--) {
            grid.add(createTile("Tile " + count++), 0, i);
        }

        // Верхняя строка (слева направо)
        for (int i = 1; i < BOARD_SIZE; i++) {
            grid.add(createTile("Tile " + count++), i, 0);
        }

        // Правая колонка (сверху вниз)
        for (int i = 1; i < BOARD_SIZE - 1; i++) {
            grid.add(createTile("Tile " + count++), BOARD_SIZE - 1, i);
        }

        // Настройка пропорций ячеек
        for (int i = 0; i < BOARD_SIZE; i++) {
            ColumnConstraints colConstraints = new ColumnConstraints();
            colConstraints.setPercentWidth(100.0 / BOARD_SIZE);
            colConstraints.setFillWidth(true);
            grid.getColumnConstraints().add(colConstraints);

            RowConstraints rowConstraints = new RowConstraints();
            rowConstraints.setPercentHeight(100.0 / BOARD_SIZE);
            rowConstraints.setFillHeight(true);
            grid.getRowConstraints().add(rowConstraints);
        }

        // Создание сцены
        Scene scene = new Scene(grid, WINDOW_SIZE, WINDOW_SIZE);
        primaryStage.setTitle("Monopoly Board");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private Pane createTile(String labelText) {
        StackPane tile = new StackPane();
        tile.setStyle("-fx-border-color: black; -fx-border-width: 2;");
        tile.setBackground(new Background(new BackgroundFill(Color.LIGHTGREEN, null, null)));
        Text label = new Text(labelText);
        tile.getChildren().add(label);
        return tile;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
