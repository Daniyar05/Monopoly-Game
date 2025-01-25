package com.monopoly.server;

import com.monopoly.server.message.GameManagerServer;
import com.monopoly.server.process.WaitingRoom;
import com.monopoly.server.services.ClientService;
import com.monopoly.server.services.ServerService;
import javafx.application.Application;
import javafx.scene.control.Alert;
import javafx.scene.control.TextInputDialog;
import javafx.stage.Stage;

public class Main extends Application {
    private final int port = 12355;

    private static final GameManagerServer gameManagerServer = new GameManagerServer();

    public static GameManagerServer getGameManagerServer() {
        return gameManagerServer;
    }

    @Override
    public void start(Stage primaryStage) {
        String nickname = showInputDialog("Введите ваше имя:", "Имя");
        if (nickname == null || nickname.isBlank()) {
            showError("Имя не может быть пустым. Программа завершена.");
            return;
        }

        boolean isHost = showConfirmDialog("Вы хотите стать хостом?", "Роль в игре");

        if (isHost) {
            initializeHost(nickname);
        } else {
            initializeClient(nickname);
        }
    }

    private void initializeHost(String nickname) {
        ServerService serverService = new ServerService(port);
        new Thread(serverService).start();
        gameManagerServer.setServerService(serverService);
        Stage waitingRoomStage = new Stage();
        ClientService clientService = new ClientService("127.0.0.1", port, nickname, waitingRoomStage);
        new Thread(clientService).start();


        WaitingRoom waitingRoom = new WaitingRoom(
                true,
                ready -> System.out.println("Игрок изменил статус готовности: " + ready),
                nickname,
                clientService::sendCommand
        );

        waitingRoom.start(waitingRoomStage);
    }

    private void initializeClient(String nickname) {
        String hostIp = showInputDialog("Введите IP хоста:", "IP Адрес");
        if (hostIp == null || hostIp.isBlank()) {
            hostIp="127.0.0.1";
        }

        Stage waitingRoomStage = new Stage();
        ClientService clientService = new ClientService(hostIp, port, nickname, waitingRoomStage);
        new Thread(clientService).start();



        WaitingRoom waitingRoom = new WaitingRoom(
                false,
                ready -> System.out.println("Игрок изменил статус готовности: " + ready),
                nickname,
                command -> clientService.sendCommand(command)
        );

        waitingRoom.start(waitingRoomStage);
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
