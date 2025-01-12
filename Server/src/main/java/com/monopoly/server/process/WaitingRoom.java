package com.monopoly.server.process;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.concurrent.atomic.AtomicBoolean;

public class WaitingRoom extends Application {

    private final AtomicBoolean isReady = new AtomicBoolean(false);

    @Override
    public void start(Stage stage) {
        VBox root = new VBox(20);
        root.setAlignment(Pos.CENTER);

        Label title = new Label("Ожидание других игроков...");
        Label statusLabel = new Label("Ваш статус: Не готов");

        Button readyButton = new Button("Готов");
        readyButton.setOnAction(e -> {
            isReady.set(true);
            statusLabel.setText("Ваш статус: Готов");
            sendReadyStatus(true); // Отправка статуса готовности
        });

        Button notReadyButton = new Button("Не готов");
        notReadyButton.setOnAction(e -> {
            isReady.set(false);
            statusLabel.setText("Ваш статус: Не готов");
            sendReadyStatus(false); // Отправка статуса готовности
        });

        HBox buttonBox = new HBox(10, readyButton, notReadyButton);
        buttonBox.setAlignment(Pos.CENTER);

        root.getChildren().addAll(title, statusLabel, buttonBox);

        Scene scene = new Scene(root, 400, 200);
        stage.setScene(scene);
        stage.setTitle("Ожидание игроков");
        stage.show();
    }

    private void sendReadyStatus(boolean ready) {
        // Логика отправки статуса на сервер
        System.out.println("Статус готовности отправлен: " + (ready ? "Готов" : "Не готов"));
    }

    public static void main(String[] args) {
        launch(args);
    }
}
