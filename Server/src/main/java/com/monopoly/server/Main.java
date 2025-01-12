package com.monopoly.server;

import com.monopoly.game.Game;
import com.monopoly.graphics.GameGUI;
import com.monopoly.server.process.WaitingRoom;
import com.monopoly.server.services.ChatClientService;
import com.monopoly.server.services.ChatServerService;
import javafx.application.Application;
import javafx.scene.control.Alert;
import javafx.scene.control.TextInputDialog;
import javafx.stage.Stage;

public class Main extends Application {
    Game game;

    @Override
    public void start(Stage primaryStage) {

        String nickname = showInputDialog("Введите ваше имя:", "Имя");
        if (nickname == null || nickname.isBlank()) {
            showError("Имя не может быть пустым. Программа завершена.");
            return;
        }

        boolean isHost = showConfirmDialog("Вы хотите стать хостом?", "Роль в игре");

        if (isHost) {
            ChatServerService chatServerService = new ChatServerService(12345);
            new Thread(chatServerService).start();

            WaitingRoom.launch();

            ChatClientService clientService = new ChatClientService("127.0.0.1", 12345, nickname);
            new Thread(clientService).start();


            // TODO добавьте логику запуска окна для игрв
        } else {
            String hostIp = showInputDialog("Введите IP хоста:", "IP Адрес");
            if (hostIp == null || hostIp.isBlank()) {
                showError("IP адрес не может быть пустым. Программа завершена.");
                return;
            }

            WaitingRoom.launch();

            ChatClientService clientService = new ChatClientService(hostIp, 12345, nickname);
            new Thread(clientService).start();

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
