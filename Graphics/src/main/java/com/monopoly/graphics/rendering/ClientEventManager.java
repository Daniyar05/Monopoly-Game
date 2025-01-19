package com.monopoly.graphics.rendering;

import com.monopoly.game.from_Server.message.EventEnum;
import com.monopoly.game.from_Server.message.GameMessage;
import com.monopoly.game.from_Server.message.MessageType;
import com.monopoly.game.manager.EventManager;
import com.monopoly.graphics.GameGUI;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.io.PrintWriter;
import java.util.concurrent.CountDownLatch;

public class ClientEventManager implements EventManager {
    private final PrintWriter out;
//    private final BlockingQueue<Boolean> responseQueue = new ArrayBlockingQueue<>(1);
    private final String nickname;

    public ClientEventManager(PrintWriter out, String nickname) {
        this.out = out;
        this.nickname = nickname;
    }

    @Override
    public boolean choiceYes(EventEnum question) {
        return choiceYes(question, new ChoiceHandler() {
            @Override
            public void handle(boolean userChoice) {
//                System.out.println("Пользователь выбрал: " + userChoice);
                // После завершения выбора вызываем sendCommand
                sendCommand(new GameMessage(MessageType.PLAYER_CHOICE, nickname, Boolean.toString(userChoice)));
            }
        });
    }
    interface ChoiceHandler {
        void handle(boolean userChoice);
    }
    public boolean choiceYes(EventEnum question, ChoiceHandler choiceHandler) {
//        System.out.println("Запустил вопросник");

        // Переменная для хранения ответа
        // Создаем диалоговое окно в UI-потоке
        Platform.runLater(() -> {
//            System.out.println("Создание Alert");
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Choice Dialog");
            alert.setHeaderText("Make a Choice");
            alert.setContentText(question.getValue());

            ButtonType yesButton = new ButtonType("Yes");
            ButtonType noButton = new ButtonType("No");
            alert.getButtonTypes().setAll(yesButton, noButton);

            // Обрабатываем выбор пользователя
            alert.show(); // Используем show() для немодального окна

            // Добавляем обработчик выбора
            alert.setOnCloseRequest(event -> {
                ButtonType result = alert.getResult();
                boolean userChoice = result == yesButton;
                choiceHandler.handle(userChoice);
            });
        });
        return false;

    }



    @Override
    public void notifyAboutAction(String message, String nickname) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Notification");
            alert.setHeaderText(null);
            alert.setContentText(message);
            alert.initOwner(null); // Не блокировать основное окно
            alert.show();
        });
    }

    @Override
    public void sendCommand(GameMessage message) {
//        System.out.println("Отправленно на сервер");
        out.println(message.toString());
    }
}
