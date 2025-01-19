package com.monopoly.graphics.rendering;

import com.monopoly.game.component.area.PropertyTile;
import com.monopoly.game.component.area.Tile;
import com.monopoly.game.config.TileConfigurator;
import com.monopoly.game.from_Server.message.GameMessage;
import com.monopoly.game.from_Server.message.MessageType;
import com.monopoly.game.from_Server.service.ClientServiceInterface;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WindowSettingForGUI {
    private int TILE_SIZE = 100; // Размер одной клетки
    private int BOARD_SIZE = 8; // Количество клеток на стороне
    private int WINDOW_SIZE = 800; // Фиксированный размер окна
//    private int PLAYER_POSITION = 0; // Пример позиции игрока
    private final ClientServiceInterface clientService;
    private final String nickname;
    private Stage stage;

    // Карта позиций игроков (имя -> позиция)
    private final Map<String, Integer> playerPositions = new HashMap<>();
    Map<String, String> tileOwners = new HashMap<>(); // Добавьте данные о владельцах клеток
    Map<String, Integer> playerBalances = new HashMap<>(); // Добавьте данные о деньгах игроков

    public WindowSettingForGUI(int TILE_SIZE, int BOARD_SIZE, int WINDOW_SIZE, int PLAYER_POSITION, Stage primaryStage, ClientServiceInterface clientService, String nickname) {
        this.TILE_SIZE=TILE_SIZE;
        this.BOARD_SIZE=BOARD_SIZE;
        this.WINDOW_SIZE=WINDOW_SIZE;
//        this.PLAYER_POSITION=PLAYER_POSITION;
        this.clientService = clientService;
        this.nickname = nickname;
        playerPositions.put(nickname, PLAYER_POSITION);
        update(primaryStage);
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
// Добавление кубика на интерфейс
    private void createDiceButton(GridPane grid, int x, int y) {
        Button diceButton = new Button("Roll Dice");
        diceButton.setPrefSize(TILE_SIZE, TILE_SIZE);
        diceButton.setOnAction(e -> {
            // Отправка запроса на сервер для генерации числа
//            clientService.sendCommand("ROLL_DICE");
            clientService.sendCommand(new GameMessage(
                    MessageType.ROLL_DICE,
                    nickname,
                    ""
            ));

        });
        grid.add(diceButton, x, y);
    }

    private void createTileButton(Tile tile, GridPane grid, int x, int y) {
        Button button = new Button(tile.getName());
        button.setPrefSize(TILE_SIZE, TILE_SIZE);
        button.setOnAction(e -> openTileDescription(tile));

        // Проверяем, куплена ли клетка
        String owner = tileOwners.get(tile.getName());
        if (owner != null) {
            button.setStyle("-fx-background-color: lightblue;"); // Цвет клетки
            button.setText(button.getText() + "\n(Owner: " + owner + ")");
        }

        // Проверяем, находятся ли игроки на этой клетке
        StringBuilder playersOnTile = new StringBuilder();
        playerPositions.forEach((playerName, position) -> {
            if (isPlayerOnTile(playerName, x, y)) {
                playersOnTile.append(playerName).append(" ");
            }
        });

        // Если есть игроки на клетке, добавляем их в отображение
        if (!playersOnTile.isEmpty()) {
            button.setText(button.getText() + "\n[" + playersOnTile.toString().trim() + "]");
        }

        grid.add(button, x, y);
    }
    private boolean isPlayerOnTile(String playerName, int x, int y) {
        Integer playerPosition = playerPositions.get(playerName);
        if (playerPosition == null) {
            return false;
        }
        int[] playerCoordinates = calculatePlayerCoordinates(playerPosition);
        return (x == playerCoordinates[1] && y == playerCoordinates[0]);
    }


    //    private boolean isPlayerOnTile(int x, int y, int position) {
//        int playerX = position % BOARD_SIZE;
//        int playerY = position / BOARD_SIZE;
//        return x == playerX && y == playerY;
//    }
private int[] calculatePlayerCoordinates(int position) {
    int totalTiles = BOARD_SIZE * 4 - 4; // Общее количество клеток на периметре
    int normalizedPosition = position % totalTiles;

    if (normalizedPosition < BOARD_SIZE) {
        return new int[]{BOARD_SIZE - 1, BOARD_SIZE - 1 - normalizedPosition};
    } else if (normalizedPosition < BOARD_SIZE * 2 - 1) {
        return new int[]{BOARD_SIZE - 1 - (normalizedPosition - (BOARD_SIZE - 1)), 0};
    } else if (normalizedPosition < BOARD_SIZE * 3 - 2) {
        return new int[]{0, normalizedPosition - (BOARD_SIZE * 2 - 2)};
    } else {
        return new int[]{normalizedPosition - (BOARD_SIZE * 3 - 3), BOARD_SIZE - 1};
    }
}


    private void openTileDescription(Tile tile) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Tile Description");
        alert.setHeaderText(tile.getName());
        alert.setContentText("Description: " + tile.getName());
        alert.showAndWait();
    }

    public void update(Stage primaryStage) {
        this.stage=primaryStage;
        BorderPane mainLayout = new BorderPane();

        // Создание игрового поля
        GridPane grid = new GridPane();
        List<Tile> tiles = TileConfigurator.configureTiles();
        createBoard(grid, tiles);

//        createDice(game,grid, 2,2);
        createDiceButton(grid, BOARD_SIZE / 2, BOARD_SIZE / 2);

        settings(grid);
        // Добавление игрового поля в центральную часть
        mainLayout.setCenter(grid);
        addPlayerInfoPanel(mainLayout, playerBalances);
        // Создание сцены
        Scene scene = new Scene(mainLayout, WINDOW_SIZE, WINDOW_SIZE);
        primaryStage.setTitle("Monopoly Board");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    public void updatePlayerPosition(String playerName, String position) {
        // Обновляем положение игрока
        playerPositions.put(playerName, Integer.parseInt(position));
        System.out.println("Игрок " + playerName + " переместился на клетку " + position);

        // Обновление GUI
        update(stage);
    }

    public void updateTileState(String tileData) {
        // Обновляем графическое представление клетки на основе новых данных
        System.out.println("Обновление клетки: " + tileData);
    }
    public void displayGameOver(String winner) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Конец игры");
        alert.setHeaderText("Победитель: " + winner);
        alert.setContentText("Спасибо за игру!");
        alert.showAndWait();
    }

    private void addPlayerInfoPanel(BorderPane mainLayout, Map<String, Integer> playerBalances) {
        VBox playerInfoBox = new VBox();
        playerInfoBox.setSpacing(10); // Отступы между элементами
        playerInfoBox.setStyle("-fx-padding: 10; -fx-background-color: #f0f0f0;");

        playerBalances.forEach((playerName, balance) -> {
            Button playerInfo = new Button(playerName + ": $" + balance);
            playerInfo.setStyle("-fx-font-size: 14; -fx-background-color: transparent;");
            playerInfo.setDisable(true); // Только для отображения
            playerInfoBox.getChildren().add(playerInfo);
        });

        mainLayout.setRight(playerInfoBox); // Боковая панель справа
    }
    public void updateTileOwner(String[] tile) {
        // Обновляем владельца клетки
        tileOwners.put(tile[0], tile[1]);
        System.out.println("Клетка " + tile[0] + " теперь принадлежит " + tile[1]);

        // Перерисовываем интерфейс
        update(stage);
    }
}
