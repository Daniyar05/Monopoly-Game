package com.monopoly.graphics.rendering;


import com.monopoly.game.from_Server.message.EventEnum;
import com.monopoly.game.from_Server.message.GameMessage;
import com.monopoly.game.from_Server.message.MessageType;
import com.monopoly.game.manager.EventManager;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.io.PrintWriter;
import java.util.LinkedList;
import java.util.Queue;

public class ClientEventManager implements EventManager {
    private final PrintWriter out;
    private final String nickname;
    private final Queue<Runnable> taskQueue = new LinkedList<>();
    private boolean isTaskActive = false; // Флаг, указывающий на выполнение текущей задачи
    private final Object lock = new Object(); // Объект для синхронизации потоков

    public ClientEventManager(PrintWriter out, String nickname) {
        this.out = out;
        this.nickname = nickname;
    }

    @Override
    public boolean choiceYes(EventEnum question) {
        final boolean[] resultHolder = new boolean[1];

        addToQueue(() -> {
            Platform.runLater(() -> {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Choice Dialog");
                alert.setHeaderText("Make a Choice");
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

        // Ждём завершения задачи
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
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Notification");
            alert.setHeaderText(null);
            alert.setContentText(message);

            alert.setOnHidden(e -> processNextTask());
            alert.show();
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

//
//import com.monopoly.game.from_Server.message.EventEnum;
//import com.monopoly.game.from_Server.message.GameMessage;
//import com.monopoly.game.from_Server.message.MessageType;
//import com.monopoly.game.manager.EventManager;
//import javafx.application.Platform;
//import javafx.scene.control.Alert;
//import javafx.scene.control.ButtonType;
//
//import java.io.PrintWriter;
//import java.util.LinkedList;
//import java.util.Queue;
//
//public class ClientEventManager implements EventManager {
//    private final PrintWriter out;
//    private final String nickname;
//    private final Queue<String> notificationQueue = new LinkedList<>();
//    private boolean isNotificationActive = false;
//
//    public ClientEventManager(PrintWriter out, String nickname) {
//        this.out = out;
//        this.nickname = nickname;
//    }
//
//    @Override
//    public boolean choiceYes(EventEnum question) {
//        return choiceYes(question, userChoice -> sendCommand(new GameMessage(MessageType.PLAYER_CHOICE, nickname, Boolean.toString(userChoice))));
//    }
//    interface ChoiceHandler{
//        void handle(boolean userChoice);
//    }
//
//    public boolean choiceYes(EventEnum question, ChoiceHandler choiceHandler) {
//
//        Platform.runLater(() -> {
//            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
//            alert.setTitle("Choice Dialog");
//            alert.setHeaderText("Make a Choice");
//            alert.setContentText(question.getValue());
//
//            ButtonType yesButton = new ButtonType("Yes");
//            ButtonType noButton = new ButtonType("No");
//            alert.getButtonTypes().setAll(yesButton, noButton);
//
//            alert.show();
//
//
//            alert.setOnCloseRequest(event -> {
//                ButtonType result = alert.getResult();
//                boolean userChoice = result == yesButton;
//                choiceHandler.handle(userChoice);
//            });
//        });
//        return false;
//
//    }
//
//
//    @Override
//    public void notifyAboutAction(String message, String nickname) {
//        Platform.runLater(() -> {
//            synchronized (notificationQueue) {
//                notificationQueue.add(message);
//                if (!isNotificationActive) {
//                    showNextNotification();
//                }
//            }
//        });
//    }
//    private void showNextNotification() {
//        synchronized (notificationQueue) {
//            if (notificationQueue.isEmpty()) {
//                isNotificationActive = false;
//                return;
//            }
//
//            isNotificationActive = true;
//            String message = notificationQueue.poll();
//
//            Alert alert = new Alert(Alert.AlertType.INFORMATION);
//            alert.setTitle("Notification");
//            alert.setHeaderText(null);
//            alert.setContentText(message);
//            alert.initOwner(null);
//
//            alert.setOnHidden(event -> {
//                isNotificationActive = false;
//                showNextNotification();
//            });
//
//            alert.show();
//        }
//    }
//
//    @Override
//    public void sendCommand(GameMessage message) {
//        out.println(message.toString());
//    }
//}
