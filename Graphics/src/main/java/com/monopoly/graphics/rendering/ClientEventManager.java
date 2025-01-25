package com.monopoly.graphics.rendering;


import com.monopoly.game.from_Server.message.EventEnum;
import com.monopoly.game.from_Server.message.GameMessage;
import com.monopoly.game.from_Server.message.MessageType;
import com.monopoly.game.manager.EventManager;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.PrintWriter;
import java.util.LinkedList;
import java.util.Queue;

public class ClientEventManager implements EventManager {
    private final PrintWriter out;
    private final String nickname;
    private final Stage stage;
    private final Queue<Runnable> taskQueue = new LinkedList<>();
    private boolean isTaskActive = false;
    private final Object lock = new Object();

    public ClientEventManager(PrintWriter out, String nickname, Stage stage) {
        this.out = out;
        this.nickname = nickname;
        this.stage = stage;
    }

    @Override
    public boolean choiceYes(EventEnum question) {
        final boolean[] resultHolder = new boolean[1];

        addToQueue(() -> {
            Platform.runLater(() -> {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Choice Dialog");
                alert.setHeaderText("Запрос на приобретение");
                alert.setContentText(question.getValue());

                ButtonType yesButton = new ButtonType("Yes");
                ButtonType noButton = new ButtonType("No");
                alert.getButtonTypes().setAll(yesButton, noButton);

                alert.showAndWait().ifPresent(buttonType -> {
                    boolean userChoice = buttonType == yesButton;
                    resultHolder[0] = userChoice;
                    sendCommand(new GameMessage(MessageType.PLAYER_CHOICE, nickname, Boolean.toString(userChoice)));
                    processNextTask();
                });
            });
        });

        synchronized (resultHolder) {
            try {
                while (!isTaskActive) {
                    resultHolder.wait();
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        return resultHolder[0];
    }

    @Override
    public void notifyAboutAction(String message, String nickname) {
        addToQueue(() -> Platform.runLater(() -> {

            Stage notificationStage = new Stage();
            notificationStage.initOwner(stage);
            notificationStage.setAlwaysOnTop(true);
            notificationStage.setResizable(false);
            Label notificationLabel = new Label(message);
            StackPane pane = new StackPane(notificationLabel);
            Scene scene = new Scene(pane);

            notificationStage.setScene(scene);
            notificationStage.setWidth(300);
            notificationStage.setHeight(100);

            notificationStage.setX(stage.getX() + stage.getWidth()/2-100);
            notificationStage.setY(stage.getY() + stage.getHeight()/2-200);
            notificationStage.show();

            new Thread(() -> {
                try {
                    Thread.sleep(3000);
                    Platform.runLater(() -> {
                        notificationStage.close();
                        processNextTask();
                    });
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }).start();

        }));
    }


    private void addToQueue(Runnable task) {
        synchronized (lock) {
            taskQueue.offer(task);
            if (!isTaskActive) {
                processNextTask();
            }
        }
    }

    private void processNextTask() {
        synchronized (lock) {
            if (taskQueue.isEmpty()) {
                isTaskActive = false;
                return;
            }

            isTaskActive = true;
            Runnable task = taskQueue.poll();
            task.run();
        }
    }

    @Override
    public void sendCommand(GameMessage message) {
        out.println(message.toString());
    }
}
