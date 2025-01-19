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
        System.out.println("Запустил вопросник");

        // Используем CountDownLatch для синхронизации
        final CountDownLatch latch = new CountDownLatch(1);

        // Переменная для хранения ответа
        final boolean[] userChoice = new boolean[1];

        // Создаем диалоговое окно в UI-потоке
        Platform.runLater(() -> {
            System.out.println("Создание Alert");
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
                userChoice[0] = result == yesButton;
                latch.countDown();  // Сигнализируем о завершении выбора
            });
        });

        try {
            // Ожидаем, пока пользователь не сделает выбор
            latch.await(); // Блокируем текущий поток до завершения
            System.out.println("Пользователь выбрал: " + userChoice[0]);

            // Отправляем команду на сервер
            sendCommand(new GameMessage(MessageType.PLAYER_CHOICE, nickname, Boolean.toString(userChoice[0])));

            return userChoice[0];
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Ошибка при ожидании ответа", e);
        }
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

    public void sendCommand(GameMessage message) {
        out.println(message.toString());
    }
}
