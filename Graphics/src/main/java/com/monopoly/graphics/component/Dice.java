package com.monopoly.graphics.component;

import com.monopoly.game.from_Server.message.GameMessage;
import com.monopoly.game.from_Server.message.MessageType;
import com.monopoly.game.from_Server.service.ClientServiceInterface;
import javafx.animation.*;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.util.Duration;

public class Dice {

    private final int TILE_SIZE;
    private final ClientServiceInterface clientService;
    private final String nickname;
    private Canvas diceCanvas; // Canvas для рисования кубика
    private Timeline animationTimeline;

    public Dice(int TILE_SIZE, ClientServiceInterface clientService, String nickname) {
        this.TILE_SIZE = TILE_SIZE;
        this.clientService = clientService;
        this.nickname = nickname;
    }

    public void createAnimatedDice(GridPane grid, int x, int y) {
        // Инициализируем Canvas для рисования кубика
        diceCanvas = new Canvas(TILE_SIZE, TILE_SIZE);
        GraphicsContext gc = diceCanvas.getGraphicsContext2D();

        // Загружаем изображения граней кубика
        Image[] diceImages = new Image[6];
        for (int i = 1; i <= 6; i++) {
            diceImages[i - 1] = new Image(getClass().getResourceAsStream("/dice" + i + ".png"));
        }

        // Рисуем изначальное состояние кубика (грань "1")
        drawDiceFace(gc, diceImages[0]);

        // Анимация смены граней кубика
        animationTimeline = new Timeline(new KeyFrame(Duration.millis(100), e -> {
            int randomFace = (int) (Math.random() * 6);
            drawDiceFace(gc, diceImages[randomFace]); // Рисуем текущую грань кубика
        }));
        animationTimeline.setCycleCount(Timeline.INDEFINITE);

        diceCanvas.setOnMouseClicked(e -> {
            if (animationTimeline.getStatus() == Animation.Status.RUNNING || !nickname.equals(clientService.getNowPlayerName())) {
                return;
            }

            animationTimeline.play();

            new Timeline(new KeyFrame(Duration.seconds(2), ev -> {
                animationTimeline.stop();
                int rolledValue = (int) (Math.random() * 6) + 1;
                drawDiceFace(gc, diceImages[rolledValue - 1]);
                clientService.sendCommand(new GameMessage(
                        MessageType.ROLL_DICE,
                        nickname,
                        String.valueOf(rolledValue)
                ));
            })).play();
        });

        grid.add(diceCanvas, x, y);
    }

    private void drawDiceFace(GraphicsContext gc, Image diceFace) {
        // Очищаем Canvas
        gc.clearRect(0, 0, TILE_SIZE, TILE_SIZE);

        // Рисуем границу кубика
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(3);
        gc.strokeRect(0, 0, TILE_SIZE, TILE_SIZE);

        // Рисуем изображение грани кубика
        gc.drawImage(diceFace, 5, 5, TILE_SIZE - 10, TILE_SIZE - 10);
    }
}
