package com.monopoly.server;

import com.monopoly.game.Game;
import com.monopoly.game.manager.GameManager;
import com.monopoly.graphics.GameGUI;
import com.monopoly.server.message.GameManagerServer;
import com.monopoly.server.process.WaitingRoom;
import com.monopoly.server.services.ChatClientService;
import com.monopoly.server.services.ChatServerService;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.TextInputDialog;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class Main extends Application {
    private Game game;


    @Override
    public void start(Stage primaryStage) {

        String nickname = showInputDialog("Введите ваше имя:", "Имя");
        if (nickname == null || nickname.isBlank()) {
            showError("Имя не может быть пустым. Программа завершена.");
            return;
        }
        GameManagerServer gameManagerServer = new GameManagerServer();

        boolean isHost = showConfirmDialog("Вы хотите стать хостом?", "Роль в игре");



        if (isHost) {
            ChatServerService chatServerService = new ChatServerService(1234);
            new Thread(chatServerService).start();
            Stage stage = new Stage();
            ChatClientService clientService = new ChatClientService("127.0.0.1", 1234, nickname, stage);
            new Thread(clientService).start();

            gameManagerServer.addPlayer(nickname);
            WaitingRoom waitingRoom = new WaitingRoom(
                    isHost,
                    ready -> {
                        // Обновляем статус готовности игроков
                        System.out.println("Игрок изменил статус готовности: " + ready);
                        // Здесь можно проверить общее состояние готовности
                    },
                    () -> {
                        // Логика старта игры
                        Platform.runLater(() -> {
//                            waitingRoom.close(); // Закрываем комнату ожидания

                            // Создаём общий экземпляр Game
                            Game game = gameManagerServer.getGame(); // Пример

                            // Запускаем GameGUI для текущего игрока
                            GameGUI gameGUI = new GameGUI(game, nickname);
                            try {
                                gameGUI.start(new Stage());
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        });                    },
                    nickname,
                    command -> clientService.sendCommand(command) // Используем клиент для отправки команды
            );
            waitingRoom.start(stage); // Передаём новый Stage для комнаты ожидания




            // TODO добавьте логику запуска окна для игрв
        } else {
            String hostIp = showInputDialog("Введите IP хоста:", "IP Адрес");
            if (hostIp == null || hostIp.isBlank()) {
                showError("IP адрес не может быть пустым. Программа завершена.");
                return;
            }
            Stage stage = new Stage();

            ChatClientService clientService = new ChatClientService(hostIp, 1234, nickname,stage);
            new Thread(clientService).start();

            gameManagerServer.addPlayer(nickname);

            WaitingRoom waitingRoom = new WaitingRoom(
                    isHost,
                    ready -> {
                        // Обновляем статус готовности игроков
                        System.out.println("Игрок изменил статус готовности: " + ready);
                        // Здесь можно проверить общее состояние готовности
                    },
                    () -> {
                        // Логика старта игры
                        Platform.runLater(() -> {
//                            waitingRoom.close(); // Закрываем комнату ожидания

                            // Создаём общий экземпляр Game
                            Game game = gameManagerServer.getGame(); // Пример

                            // Запускаем GameGUI для текущего игрока
                            GameGUI gameGUI = new GameGUI(game, nickname);
                            try {
                                gameGUI.start(new Stage());
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        });
                    },
                    nickname,
                    command -> clientService.sendCommand(command) // Используем клиент для отправки команды
            );
            waitingRoom.start(stage); // Передаём новый Stage для комнаты ожидания

            // TODO добавьте логику запуска окна для игрв
        }
    }

    private String showInputDialog(String message, String title) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle(title);
        dialog.setHeaderText(null);
        dialog.setContentText(message);

        return dialog.showAndWait().orElse(null);
    }

    private boolean showConfirmDialog(String message, String title) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);

        return alert.showAndWait().filter(button -> button.getText().equalsIgnoreCase("OK")).isPresent();
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Ошибка");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
