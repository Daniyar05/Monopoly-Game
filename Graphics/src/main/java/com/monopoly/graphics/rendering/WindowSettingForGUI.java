package com.monopoly.graphics.rendering;

import com.monopoly.game.component.area.PropertyTile;
import com.monopoly.game.component.area.Tile;
import com.monopoly.game.config.TileConfigurator;
import com.monopoly.game.from_Server.message.GameMessage;
import com.monopoly.game.from_Server.message.MessageType;
import com.monopoly.game.from_Server.service.ClientServiceInterface;
import com.monopoly.graphics.component.Dice;
import com.monopoly.graphics.util.ColorUtil;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WindowSettingForGUI {
    private final int BOARD_SIZE; // Количество клеток на стороне
    private final int WINDOW_SIZE; // Фиксированный размер окна
    private final double DIFFERENCE_IN_SIZE=0.5;
    private final ClientServiceInterface clientService;
    private final String nickname;
    private Stage stage;
    private final Dice dice;
    private double currentWidth;
    private double currentHeight;

    // Карта позиций игроков (имя -> позиция)
    private final Map<String, Integer> playerPositions = new HashMap<>();
    private final Map<String, String> tileOwners = new HashMap<>(); // Добавьте данные о владельцах клеток

    private final Map<Integer, Button> tileButtons = new HashMap<>();
    private final Map<String, String> playerPathToIco = new HashMap<>(); // Игрок -> путь к его изображению
    private final List<String> playersNames;
    public WindowSettingForGUI(int TILE_SIZE, int BOARD_SIZE, int WINDOW_SIZE, int PLAYER_POSITION, Stage primaryStage, ClientServiceInterface clientService, String nickname) {
        this.BOARD_SIZE=BOARD_SIZE;
        this.WINDOW_SIZE=WINDOW_SIZE;
        this.clientService = clientService;
        this.nickname = nickname;
        playersNames= clientService.getListPlayers();
        addPathToIcoForAllPlayer();
        playerPositions.put(nickname, PLAYER_POSITION);
        dice = new Dice(TILE_SIZE, clientService,nickname);
        update(primaryStage);
    }

    private void addPathToIcoForAllPlayer() {
        List<String> tokenPaths = List.of(
                "/pieces/Car.jpg",
                "/pieces/Cat.jpg",
                "/pieces/Base1.jpg",
                "/pieces/Base2.jpg"

        );
        for (int i = 0; i < playersNames.size(); i++) {
            String playerName = playersNames.get(i);
            String tokenImagePath = tokenPaths.get(i % tokenPaths.size()); // Повторяем пути, если игроков больше, чем фишек
            registerPlayerPath(playerName, tokenImagePath);
        }
    }

    private void createBoard(GridPane grid, List<Tile> tiles) {
        int count = 0;
        for (int i = BOARD_SIZE - 1; i >= 0; i--) {
            Button button = createTileButton(tiles.get(count++), grid, i, BOARD_SIZE - 1);
            tileButtons.put(tiles.get(count-1).getPosition(), button);

        }
        for (int i = BOARD_SIZE - 2; i >= 0; i--) {
            Button button = createTileButton(tiles.get(count++), grid, 0, i);
            tileButtons.put(tiles.get(count-1).getPosition(), button);

        }
        for (int i = 1; i < BOARD_SIZE; i++) {
            Button button = createTileButton(tiles.get(count++), grid, i, 0);
            tileButtons.put(tiles.get(count-1).getPosition(), button);

        }
        for (int i = 1; i < BOARD_SIZE - 1; i++) {
            Button button = createTileButton(tiles.get(count++), grid, BOARD_SIZE - 1, i);
            tileButtons.put(tiles.get(count-1).getPosition(), button);

        }
    }


    private void settings(GridPane grid, double newWidth, double newHeight) {
        double tileWidth = newWidth / BOARD_SIZE;
        double tileHeight = newHeight / BOARD_SIZE;

        grid.getColumnConstraints().clear();
        grid.getRowConstraints().clear();
        grid.setPrefSize(currentWidth, currentHeight);
        for (int i = 0; i < BOARD_SIZE; i++) {
            ColumnConstraints colConstraints = new ColumnConstraints();
            if (i == 0 || i == BOARD_SIZE-1){
                colConstraints.setPrefWidth(tileWidth+DIFFERENCE_IN_SIZE*tileWidth);
            } else {
                colConstraints.setPrefWidth(tileWidth);
            }
            colConstraints.setHgrow(Priority.ALWAYS);
            grid.getColumnConstraints().add(colConstraints);
        }

        for (int i = 0; i < BOARD_SIZE; i++) {
            RowConstraints rowConstraints = new RowConstraints();
            if (i == 0 || i == BOARD_SIZE-1){
                rowConstraints.setPrefHeight(tileHeight+DIFFERENCE_IN_SIZE*tileHeight);
            } else {
                rowConstraints.setPrefHeight(tileHeight);
            }
            rowConstraints.setVgrow(Priority.ALWAYS);
            grid.getRowConstraints().add(rowConstraints);
        }

    }


    private Button createTileButton(Tile tile, GridPane grid, int x, int y) {
        Button button = new Button();
        button.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        button.setPrefSize(Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE);


        button.setOnAction(e -> openTileDescription(tile));
        // Проверяем, куплена ли клетка
        String owner = tileOwners.get(tile.getName());
        List<String> names = new ArrayList<>();
        playerPositions.forEach((playerName, position) -> {
            if (isPlayerOnTile(playerName, x, y)) {
                names.add(playerName);
            }
        });
        addInformAndStyleForButton(button,tile,owner, names);

        grid.add(button, x, y);
        return button;
    }



    private boolean isPlayerOnTile(String playerName, int x, int y) {
        Integer playerPosition = playerPositions.get(playerName);
        if (playerPosition == null) {
            return false;
        }
        int[] playerCoordinates = calculatePlayerCoordinates(playerPosition);
        return (x == playerCoordinates[1] && y == playerCoordinates[0]);
    }


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

        ImageView background = new ImageView(new Image(getClass().getResourceAsStream("/Board.jpg")));
        background.setPreserveRatio(false);
        background.setFitWidth(WINDOW_SIZE);
        background.setFitHeight(WINDOW_SIZE);

        StackPane layeredPane = new StackPane();
        layeredPane.getChildren().add(background);

        GridPane grid = new GridPane();
        List<Tile> tiles = TileConfigurator.configureTiles();
        createBoard(grid, tiles);
        dice.createAnimatedDice(grid, BOARD_SIZE / 2+1, BOARD_SIZE / 2+1);

        layeredPane.getChildren().add(grid);
        mainLayout.setCenter(layeredPane);


        Scene scene = new Scene(mainLayout, WINDOW_SIZE, WINDOW_SIZE);
        primaryStage.setTitle("Monopoly Board");
        primaryStage.setMinWidth(500);
        primaryStage.setMinHeight(500);
        primaryStage.setScene(scene);
        currentWidth = scene.getWidth();
        currentHeight = scene.getHeight();
        scene.widthProperty().addListener((obs, oldWidth, newWidth) -> {
            if (!newWidth.equals(currentWidth)) {
                adjustLayout(grid, background, newWidth.doubleValue(), scene.getHeight());
            }
        });

        scene.heightProperty().addListener((obs, oldHeight, newHeight) -> {
            if (!newHeight.equals(currentHeight)) {
                adjustLayout(grid, background, scene.getWidth(), newHeight.doubleValue());
            }
        });
        primaryStage.show();
    }

    private void adjustLayout(GridPane grid, ImageView background, double newWidth, double newHeight) {
        currentWidth = newWidth;
        currentHeight = newHeight;
        background.setFitWidth(currentWidth);
        background.setFitHeight(currentHeight);
        settings(grid, currentWidth, currentHeight);
    }

    public void updatePlayerPosition(String playerName) {
        int position = playerPositions.getOrDefault(playerName, -1);
        updateButtonTile(position);

    }
    public void updatePlayerPosition(String playerName, String newPosition) {
        int oldPosition = playerPositions.getOrDefault(playerName, -1);
        int newTilePosition = Integer.parseInt(newPosition)%tileButtons.size();

        playerPositions.put(playerName, newTilePosition);
        System.out.println("Игрок " + playerName + " переместился с клетки " + oldPosition + " на клетку " + newTilePosition);

        if (oldPosition >= 0) {
            updateButtonTile(oldPosition);
        }
        updateButtonTile(newTilePosition);
    }


    public void updateButtonTile(int tilePosition) {
//        System.out.println("Обновлено поле "+tilePosition);

        int tilePositionActual = tilePosition%tileButtons.size();
        Button button = tileButtons.get(tilePositionActual);
        if (button == null) {
            System.err.println("Кнопка для позиции " + tilePositionActual + " не найдена!");
            return;
        }
        Tile tile = TileConfigurator.getTileByPosition(tilePositionActual);
        String owner = tileOwners.get(tile.getName());

        List<String> names = new ArrayList<>();
        playerPositions.forEach((playerName, playerPosition) -> {
            if (playerPosition == tilePositionActual) {
                names.add(playerName);
            }
        });

        addInformAndStyleForButton(button,tile,owner, names);

    }

    private void addInformAndStyleForButton(Button button, Tile tile, String owner, List<String> playersOnTile) {
        if (owner != null) {
            setOwnerStyleForButton(button,tile,owner);
        } else {
            setDefaultStyleForButton(button);
        }
        if (!playersOnTile.isEmpty()) {
            setPlayerOnTileStyleForButton(button, playersOnTile);
        }
    }

    private void setDefaultStyleForButton(Button button) {
        button.setGraphic(null);
        button.setText("");
        button.setStyle("-fx-background-color: transparent;"); // Прозрачная кнопка
    }
    private void setOwnerStyleForButton(Button fieldButton, Tile tile, String owner) {
        fieldButton.setGraphic(null);

        Button ownerButton = new Button();
        ownerButton.setStyle("-fx-background-color: " + ColorUtil.getColorForUser(owner)
                + "; -fx-background-radius: 50%; -fx-border-radius: 50%;");
        ownerButton.setPrefSize(20, 20);
        ownerButton.setMaxSize(20, 20);
        ownerButton.setMinSize(20, 20);

        ownerButton.setOnAction(e -> {
            e.consume();
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Управление собственностью");
            alert.setHeaderText("Поле: " + tile.getName());

            VBox vbox = new VBox(10);
            vbox.getChildren().addAll(
                    new Label("Владелец: " + owner),
                    new Label("Стоимость: $" + ((PropertyTile)tile).getCost().getAmount())
            );

            if (owner.equals(nickname)) {
                Button sellButton = new Button("Продать собственность");
                sellButton.setStyle("-fx-background-color: #ff4444; -fx-text-fill: white;");
                sellButton.setOnAction(event -> {
                    handleSellProperty(tile);
                    alert.close();
                });
                vbox.getChildren().add(sellButton);
            }

            alert.getDialogPane().setContent(vbox);
            alert.showAndWait();
        });


        StackPane stackPane;
        if (fieldButton.getGraphic() instanceof StackPane) {
            stackPane = (StackPane) fieldButton.getGraphic();
        } else {
            stackPane = new StackPane();
        }



        if (!stackPane.getChildren().contains(ownerButton)) {
            stackPane.getChildren().add(ownerButton);
            StackPane.setAlignment(ownerButton, Pos.TOP_RIGHT);
        }

        fieldButton.setGraphic(stackPane);

        fieldButton.setText("");
        fieldButton.setStyle("-fx-background-color: transparent;");
    }

    private void handleSellProperty(Tile tile) {
        if (tile instanceof PropertyTile) {
            clientService.sendCommand(new GameMessage(
                    MessageType.SELL_TILE,
                    nickname,
                    tile.getName()
                    )
            );
        }
    }

    private void setPlayerOnTileStyleForButton(Button button, List<String> playerNames) {
        if (playerNames == null || playerNames.isEmpty()) {
            return;
        }

        StackPane stackPane;
        if (button.getGraphic() instanceof StackPane) {
            stackPane = (StackPane) button.getGraphic();
        } else {
            stackPane = new StackPane();
            button.setGraphic(stackPane);
        }

        int index = 0;
        for (String playerName : playerNames) {
            String tokenImagePath = playerPathToIco.get(playerName.trim());
            if (tokenImagePath == null) {
                System.err.println("Не удалось найти изображение для игрока: " + playerName);
                continue;
            }

            ImageView playerToken = new ImageView(new Image(getClass().getResourceAsStream(tokenImagePath)));
            int i = 30;
            playerToken.setFitWidth(i);
            playerToken.setFitHeight(i);

            if (playerName.equals(clientService.getNowPlayerName())) {
                playerToken.setStyle("-fx-effect: dropshadow(gaussian, green, 25, 0, 0, 0);");
            }

            setTooltipForPlayer(playerName, playerToken, stackPane);

            double[][] positions = {
                    {-i, -i}, {i, -i}, {-i, i}, {i, i} // Для до четырёх игроков
            };
            double xOffset = positions[index % positions.length][0];
            double yOffset = positions[index % positions.length][1];
            StackPane.setAlignment(playerToken, Pos.CENTER);
            StackPane.setMargin(playerToken, new Insets(yOffset, 0, 0, xOffset));

            index++;
        }
}

    private void setTooltipForPlayer(String playerName, ImageView playerToken, StackPane stackPane){
        Tooltip tooltip = new Tooltip("Игрок: " + playerName + "\nБаланс: $" + clientService.getPlayerBalances().getOrDefault(playerName, 0));
        Tooltip.install(playerToken, tooltip);
        stackPane.getChildren().add(playerToken);
    }



    public void displayGameOver(String winner) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Конец игры");
        alert.setHeaderText("Победитель: " + winner);
        alert.setContentText("Спасибо за игру!");
        alert.setOnHidden(event -> {
            stage.close();
            Platform.exit();
        });

        alert.showAndWait();
    }


    public void updateTileOwner(GameMessage gameMessage) {
        String nameTile = TileConfigurator.getTileByPosition(Integer.parseInt(gameMessage.content())).getName();
        tileOwners.put(nameTile, gameMessage.sender());
        System.out.println("Клетка " + gameMessage.content() + " теперь принадлежит " + gameMessage.sender());

        Tile tile = TileConfigurator.getTileByPosition(Integer.parseInt(gameMessage.content()));
        if (tile != null) {
            updateButtonTile(Integer.parseInt(gameMessage.content()));
        }
    }
    public void deleteTileOwner(String tilePosition){
        Tile tile = TileConfigurator.getTileByPosition(Integer.parseInt(tilePosition));
        tileOwners.remove(tile.getName());
        updateButtonTile(tile.getPosition());

    }


    private void registerPlayerPath(String playerName, String tokenImagePath) {
        playerPathToIco.put(playerName, tokenImagePath);
    }

    public void updateTileState(String content) {
    }

}
