package com.monopoly.server.process;

import com.monopoly.game.from_Server.message.GameMessage;
import com.monopoly.game.from_Server.message.PreparationMessageType;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.function.Consumer;

public class WaitingRoom {
    private Stage stage;
    private boolean isReady = false;
    private final boolean isHost;
    private Consumer<Boolean> onReadyChanged;
    private final String nickname;
    private final Consumer<GameMessage> clientServiceCommandSender;
    private Button startGameButton;

    public WaitingRoom(boolean isHost, Consumer<Boolean> onReadyChanged,
                       String nickname, Consumer<GameMessage> clientServiceCommandSender) {
        this.isHost = isHost;
        this.onReadyChanged = onReadyChanged;
        this.nickname = nickname;
        this.clientServiceCommandSender = clientServiceCommandSender;
    }

    public void start(Stage stage) {
        this.stage=stage;
        VBox root = new VBox(20);
        root.setAlignment(Pos.CENTER);

        Label title = new Label("Ожидание других игроков...");
        Label statusLabel = new Label("Ваш статус: Не готов");

        Button readyButton = new Button("Готов");
        readyButton.setOnAction(e -> {
            isReady = true;
            statusLabel.setText("Ваш статус: Готов");
            onReadyChanged.accept(true);
            sendReadyStatus(true);
        });


        HBox buttonBox = new HBox(10, readyButton);
        buttonBox.setAlignment(Pos.CENTER);

        root.getChildren().addAll(title, statusLabel, buttonBox);

        if (isHost) {
            startGameButton = new Button("Начать игру");
            startGameButton.setDisable(true);
            startGameButton.setOnAction(e -> {
                sendStartGameCommand();
            });
            root.getChildren().add(startGameButton);

        }

        Scene scene = new Scene(root, 400, 200);
        stage.setScene(scene);
        stage.setTitle("Комната ожидания");
        stage.setUserData(this);
        stage.show();

    }

    private void sendReadyStatus(boolean ready) {

        clientServiceCommandSender.accept(
                new GameMessage(
                        PreparationMessageType.READY_STATUS,
                        nickname,
                        String.valueOf(ready)
                )
        );
//        System.out.println("Статус готовности отправлен: " + (ready ? "Готов" : "Не готов"));
    }

    private void sendStartGameCommand() {
        clientServiceCommandSender.accept(new GameMessage(
                PreparationMessageType.GAME_START,
                nickname,
                ""
        )); // Отправляем команду каждому игроку
        System.out.println("Команда старт игры отправлена серверу " );//+ playerName);
//        }
    }

    public void updateAllPlayersReady(boolean allReady) {
        if (isHost && startGameButton != null) {
            Platform.runLater(() -> startGameButton.setDisable(!allReady));
        }
    }

    public void close(){
        Platform.runLater(() -> {
            if (stage != null) {
                stage.close();
            }
        });    }
}