package com.monopoly.graphics.rendering;

import com.monopoly.game.component.model.EventEnum;
import com.monopoly.game.manager.EventManager;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

public class EventManagerGUI implements EventManager {

    @Override
    public boolean choiceYes(EventEnum question) {
        final int[] userChoice = {-1}; // Массив для хранения результата, т.к. переменные в лямбдах должны быть final или effectively final

        // Выполняем код в FX-потоке
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Choice Dialog");
            alert.setHeaderText("Make a Choice");
            alert.setContentText(question.getValue()); // Вопрос из Enum

            // Добавляем кнопки "Yes" и "No"
            ButtonType yesButton = new ButtonType("Yes");
            ButtonType noButton = new ButtonType("No");
            alert.getButtonTypes().setAll(yesButton, noButton);

            // Ждем ответа пользователя
            ButtonType result = alert.showAndWait().orElse(noButton); // По умолчанию No
            if (result == yesButton){
                userChoice[0] = 1;}
            else{
                userChoice[0] = 0;}

        });

        // Ждем завершения выполнения Platform.runLater
        while (userChoice[0]==-1) {
            try {
                Thread.sleep(100); // Небольшая задержка, чтобы не перегружать поток
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new RuntimeException("Thread was interrupted while waiting for user input.", e);
            }
        }
        System.out.println(userChoice[0]);
        return userChoice[0]==1;
    }


    @Override
    public void notifyAboutAction(String string) {
        System.out.println(string);
//        Platform.runLater(() -> {
//            Alert alert = new Alert(Alert.AlertType.INFORMATION);
//            alert.setTitle("Notification");
//            alert.setHeaderText(null); // Убираем заголовок
//            alert.setContentText(string); // Устанавливаем текст сообщения
//            alert.setResizable(false); // Делаем окно фиксированного размера
//
//            // Показываем окно
//            alert.show();
//
//            // Создаем таймер для автоматического закрытия через 5 секунд
//            new javafx.animation.Timeline(
//                    new javafx.animation.KeyFrame(
//                            javafx.util.Duration.seconds(5),
//                            event -> alert.close()
//                    )
//            ).play();
//        });

    }
}
