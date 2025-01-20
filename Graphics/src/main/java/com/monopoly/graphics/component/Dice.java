package com.monopoly.graphics.component;

import com.monopoly.game.from_Server.message.GameMessage;
import com.monopoly.game.from_Server.message.MessageType;
import com.monopoly.game.from_Server.service.ClientServiceInterface;
import com.monopoly.graphics.rendering.ClientEventManager;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.util.Duration;


public class Dice {

    private final int TILE_SIZE;
    private ClientServiceInterface clientService;
    private String nickname;

    public Dice(int TILE_SIZE, ClientServiceInterface clientService, String nickname) {
        this.TILE_SIZE = TILE_SIZE;
        this.clientService = clientService;
        this.nickname = nickname;
    }

    public void createAnimatedDice(GridPane grid, int x, int y) {
        // Загружаем изображения граней кубика
        System.out.println("createAnimatedDice");
        Image[] diceImages = new Image[6];
        for (int i = 1; i <= 6; i++) {
            diceImages[i - 1] = new Image(getClass().getResourceAsStream("/dice" + i + ".png"));
        }

        // Компонент для отображения кубика
        ImageView diceView = new ImageView(diceImages[0]);
        diceView.setFitWidth(TILE_SIZE);
        diceView.setFitHeight(TILE_SIZE);

        // Анимация смены граней кубика
        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(100), e -> {
            int randomFace = (int) (Math.random() * 6);
            diceView.setImage(diceImages[randomFace]);
        }));
        timeline.setCycleCount(Timeline.INDEFINITE);

        // Обработчик нажатия на кубик
        diceView.setOnMouseClicked(e -> {
            if (timeline.getStatus() == Animation.Status.RUNNING) {
                timeline.stop(); // Останавливаем анимацию
                int rolledValue = (int) (Math.random() * 6) + 1; // Случайное число от 1 до 6
                diceView.setImage(diceImages[rolledValue - 1]);

                // Отправка значения на сервер
                clientService.sendCommand(new GameMessage(
                        MessageType.ROLL_DICE,
                        nickname,
                        String.valueOf(rolledValue)
                ));
            } else {
                timeline.play(); // Запускаем анимацию
            }
        });

        // Добавляем кубик в сетку
        grid.add(diceView, x, y);
    }

}
