package com.monopoly.graphics.rendering;

import com.monopoly.game.component.area.PropertyTile;
import com.monopoly.game.component.area.Tile;
import com.monopoly.game.config.TileConfigurator;
import com.monopoly.game.from_Server.message.GameMessage;
import com.monopoly.game.from_Server.message.MessageType;
import com.monopoly.game.from_Server.service.ClientServiceInterface;
import com.monopoly.graphics.component.Dice;
import com.monopoly.graphics.util.ColorUtil;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.util.Pair;

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

    // Карта позиций игроков (имя -> позиция)
    private final Map<String, Integer> playerPositions = new HashMap<>();
    private final Map<String, String> tileOwners = new HashMap<>(); // Добавьте данные о владельцах клеток
//    private final Map<String, Integer> playerBalances = new HashMap<>(); // Добавьте данные о деньгах игроков

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
                "/pieces/Cat.jpg"
//                "/pieces/Hat.jpg"
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

        for (int i = 0; i < BOARD_SIZE; i++) {
            ColumnConstraints colConstraints = new ColumnConstraints();
            if (i == 0 || i == BOARD_SIZE-1){
                colConstraints.setPrefWidth(tileWidth+DIFFERENCE_IN_SIZE*tileWidth);
            } else {
                colConstraints.setPrefWidth(tileWidth);
            }
            colConstraints.setHgrow(Priority.ALWAYS); // Разрешаем колонкам растягиваться
            grid.getColumnConstraints().add(colConstraints);
        }

        for (int i = 0; i < BOARD_SIZE; i++) {
            RowConstraints rowConstraints = new RowConstraints();
            if (i == 0 || i == BOARD_SIZE-1){
                rowConstraints.setPrefHeight(tileHeight+DIFFERENCE_IN_SIZE*tileHeight);
            } else {
                rowConstraints.setPrefHeight(tileHeight);
            }
            rowConstraints.setVgrow(Priority.ALWAYS); // Разрешаем строкам растягиваться
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
        // Оборачиваем фон и игровое поле в StackPane
        StackPane layeredPane = new StackPane();
        layeredPane.getChildren().add(background);

        // Создание игрового поля
        GridPane grid = new GridPane();
//        grid.setGridLinesVisible(true);

        List<Tile> tiles = TileConfigurator.configureTiles();
        createBoard(grid, tiles);
        dice.createAnimatedDice(grid, BOARD_SIZE / 2+1, BOARD_SIZE / 2+1);

        layeredPane.getChildren().add(grid);
        mainLayout.setCenter(layeredPane);

        addPlayerInfoPanel(mainLayout, clientService.getPlayerBalances());

        Scene scene = new Scene(mainLayout, WINDOW_SIZE, WINDOW_SIZE);
        primaryStage.setTitle("Monopoly Board");
        primaryStage.setMinWidth(500); // Минимальная ширина окна
        primaryStage.setMinHeight(500); // Минимальная высота окна
        primaryStage.setScene(scene);

        scene.widthProperty().addListener((obs, oldWidth, newWidth) ->
                adjustLayout(grid, background, newWidth.doubleValue(), scene.getHeight()));

        scene.heightProperty().addListener((obs, oldHeight, newHeight) ->
                adjustLayout(grid, background, scene.getWidth(), newHeight.doubleValue()));
        primaryStage.show();
    }

    private void adjustLayout(GridPane grid, ImageView background, double newWidth, double newHeight) {
        // Масштабируем фоновое изображение
        background.setFitWidth(newWidth);
        background.setFitHeight(newHeight);
        settings(grid, newWidth, newHeight);

    }


    public void updatePlayerPosition(String playerName, String newPosition) {
        // Получаем старую и новую позиции игрока
        int oldPosition = playerPositions.getOrDefault(playerName, -1);
        int newTilePosition = Integer.parseInt(newPosition)%tileButtons.size();

        // Обновляем положение игрока
        playerPositions.put(playerName, newTilePosition);
        System.out.println("Игрок " + playerName + " переместился с клетки " + oldPosition + " на клетку " + newTilePosition);

        // Обновляем старую и новую кнопки, если они есть
        if (oldPosition >= 0) {
            updateButtonTile(oldPosition);
        }
        updateButtonTile(newTilePosition);
    }



    private void replaceAndUploadPlayerPosition(String playerName, int positionNow, int oldPosition) {
        updateButtonTile(oldPosition);
        updateButtonTile(positionNow);
    }

    public void updateButtonTile(int tilePosition) {
        int tilePositionActual = tilePosition%tileButtons.size();
//        System.out.println(tilePosition);
        Button button = tileButtons.get(tilePositionActual);
        if (button == null) {
            System.err.println("Кнопка для позиции " + tilePositionActual + " не найдена!");
            return;
        }
        Tile tile = TileConfigurator.getTileByPosition(tilePositionActual); // Предполагается, что есть метод для получения клетки
        String owner = tileOwners.get(tile.getName());

        // Проверяем, находятся ли игроки на этой клетке
        List<String> names = new ArrayList<>();
//        StringBuilder playersOnTile = new StringBuilder();
        playerPositions.forEach((playerName, playerPosition) -> {
            if (playerPosition == tilePositionActual) {
                System.out.println(playerName);
                names.add(playerName);
//                playersOnTile.append(playerName).append(" ");
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
        button.setStyle("-fx-background-color: transparent;"); // Прозрачная кнопка //  -fx-border-color: black; -fx-text-fill: black
    }
    private void setOwnerStyleForButton(Button fieldButton, Tile tile, String owner) {
        // Создаём кнопку-круг для владельца
        fieldButton.setGraphic(null);

        Button ownerButton = new Button();
        ownerButton.setStyle("-fx-background-color: " + ColorUtil.getColorForUser(owner) + "; -fx-background-radius: 50%; -fx-border-radius: 50%;");
        ownerButton.setPrefSize(20, 20); // Устанавливаем размер кнопки-круга
        ownerButton.setMaxSize(20, 20);
        ownerButton.setMinSize(20, 20);

        // Добавляем обработчик клика для кнопки владельца
        ownerButton.setOnAction(e -> {
            e.consume();
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Информация о владельце");
            alert.setHeaderText("Поле: " + tile.getName());
            alert.setContentText("Владелец: " + owner);
            alert.showAndWait();
        });

        // Создаём StackPane для графики кнопки клетки
        StackPane stackPane;
        if (fieldButton.getGraphic() instanceof StackPane) {
            stackPane = (StackPane) fieldButton.getGraphic();
        } else {
            stackPane = new StackPane();
        }

        // Добавляем кнопку-круг в StackPane поверх основной кнопки
        if (!stackPane.getChildren().contains(ownerButton)) {
            stackPane.getChildren().add(ownerButton);
            StackPane.setAlignment(ownerButton, Pos.TOP_RIGHT); // Размещаем кнопку-круг в верхнем правом углу
        }

        // Устанавливаем StackPane как графический элемент кнопки клетки
        fieldButton.setGraphic(stackPane);

        // Сбрасываем текст и фон кнопки клетки
        fieldButton.setText("");
        fieldButton.setStyle("-fx-background-color: transparent;"); // Прозрачный фон
    }


//    private void setOwnerStyleForButton(Button button, Tile tile, String owner) {
//        button.setStyle("-fx-background-color: " + ColorUtil.getColorForUser(owner) + ";");
//        button.setText(tile.getName() + "\n(Owner: " + owner + ")");
//    }

//    private void setPlayerOnTileStyleForButton(Button button, String playerName) {
//        button.setText(button.getText()+"\n[" + playerName + "]");
//    }
//private void setPlayerOnTileStyleForButton(Button button, List<String> playerNames) {
//    // Проверяем, есть ли изображение для игрока
//    if (playerNames == null || playerNames.isEmpty()) {
//        return;
//    }
//    String tokenImagePath = playerPathToIco.get(playerName.trim());
//    if (tokenImagePath == null) {
//        System.err.println("Не удалось найти изображение для игрока: " + playerName);
//        return;
//    }
//
//    // Загружаем изображение фишки игрока
//    ImageView playerToken = new ImageView(new Image(getClass().getResourceAsStream(tokenImagePath)));
//    playerToken.setFitWidth(30); // Устанавливаем размер фишки
//    playerToken.setFitHeight(30);
//
//    // Добавляем всплывающее окно с информацией об игроке
//    Tooltip tooltip = new Tooltip("Игрок: " + playerName + "\nБаланс: $" + playerBalances.getOrDefault(playerName, 0));
//    Tooltip.install(playerToken, tooltip);
//
//    // Добавляем обработчик клика на фишку
//    playerToken.setOnMouseClicked(e -> {
//        Alert alert = new Alert(Alert.AlertType.INFORMATION);
//        alert.setTitle("Информация об игроке");
//        alert.setHeaderText(playerName);
//        alert.setContentText("Баланс: $" + playerBalances.getOrDefault(playerName, 0));
//        alert.showAndWait();
//    });
//
//    // Получаем или создаём StackPane для кнопки
//    StackPane stackPane;
//    if (button.getGraphic() instanceof StackPane) {
//        stackPane = (StackPane) button.getGraphic();
//    } else {
//        stackPane = new StackPane();
//        button.setGraphic(stackPane); // Устанавливаем StackPane как графику кнопки
//    }
//
//    // Добавляем фишку игрока в StackPane
//    stackPane.getChildren().add(playerToken);
//    StackPane.setAlignment(playerToken, Pos.CENTER); // Размещаем фишку в центре кнопки
//}
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

    // Очищаем предыдущие элементы, чтобы избежать дублирования
//    stackPane.getChildren().clear();

    // Добавляем фишки для всех игроков
    int index = 0;
    for (String playerName : playerNames) {
        // Проверяем наличие изображения для игрока
        String tokenImagePath = playerPathToIco.get(playerName.trim());
        if (tokenImagePath == null) {
            System.err.println("Не удалось найти изображение для игрока: " + playerName);
            continue;
        }

        // Загружаем изображение фишки игрока
        ImageView playerToken = new ImageView(new Image(getClass().getResourceAsStream(tokenImagePath)));
        playerToken.setFitWidth(20); // Устанавливаем размер фишки
        playerToken.setFitHeight(20);

        setTooltipForPlayer(playerName, playerToken, stackPane);


        // Определяем позиции для фишек на одной клетке
        double[][] positions = {
                {-20, -20}, {20, -20}, {-20, 20}, {20, 20}, // Для до четырёх игроков
                {0, -20}, {0, 20}, {-20, 0}, {20, 0}        // Дополнительные позиции
        };
        double xOffset = positions[index % positions.length][0];
        double yOffset = positions[index % positions.length][1];
        StackPane.setAlignment(playerToken, Pos.CENTER);
        StackPane.setMargin(playerToken, new Insets(yOffset, 0, 0, xOffset)); // Смещение фишки

        index++;
    }
}

    private void setTooltipForPlayer(String playerName, ImageView playerToken, StackPane stackPane){
        // Добавляем всплывающее окно с информацией об игроке
        Tooltip tooltip = new Tooltip("Игрок: " + playerName + "\nБаланс: $" + clientService.getPlayerBalances().getOrDefault(playerName, 0));
        Tooltip.install(playerToken, tooltip);

//        // Добавляем обработчик клика на фишку
//        playerToken.setOnMouseClicked(e -> {
//            Alert alert = new Alert(Alert.AlertType.INFORMATION);
//            alert.setTitle("Информация об игроке");
//            alert.setHeaderText(playerName);
//            alert.setContentText("Баланс: $" + playerBalances.getOrDefault(playerName, 0));
//            alert.showAndWait();
//        });

        // Добавляем фишку в StackPane с расчётом позиции
        stackPane.getChildren().add(playerToken);
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

//    public void updatePlayerBalance(String playerName, int newBalance) {
//        playerBalances.put(playerName, newBalance);
//
//        // Ищем кнопку баланса игрока и обновляем её текст
//        VBox playerInfoBox = (VBox) stage.getScene().getRoot().lookup(".player-info-box"); // Селектор по стилю
//        if (playerInfoBox != null) {
//            for (javafx.scene.Node node : playerInfoBox.getChildren()) {
//                if (node instanceof Button button && button.getText().startsWith(playerName)) {
//                    button.setText(playerName + ": $" + newBalance);
//                    break;
//                }
//            }
//        }
//    }


    public void updateTileOwner(GameMessage gameMessage) {
        // Обновляем владельца клетки
        String nameTile = TileConfigurator.getTileByPosition(Integer.parseInt(gameMessage.content())).getName();
        tileOwners.put(nameTile, gameMessage.sender());
        System.out.println("Клетка " + gameMessage.content() + " теперь принадлежит " + gameMessage.sender());

        // Обновляем только соответствующую кнопку
        Tile tile = TileConfigurator.getTileByPosition(Integer.parseInt(gameMessage.content()));
        if (tile != null) {
            updateButtonTile(Integer.parseInt(gameMessage.content()));
        }
    }


    private void registerPlayerPath(String playerName, String tokenImagePath) {
        playerPathToIco.put(playerName, tokenImagePath);
    }

    public void updateTileState(String content) {
    }

}
