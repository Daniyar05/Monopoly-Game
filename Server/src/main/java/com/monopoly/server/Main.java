package com.monopoly.server;

import com.monopoly.graphics.GameGUI;
import com.monopoly.server.message.GameManagerServer;
import com.monopoly.server.process.WaitingRoom;
import com.monopoly.server.services.ClientService;
import com.monopoly.server.services.ServerService;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.TextInputDialog;
import javafx.stage.Stage;

public class Main extends Application {

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
            initializeHost(primaryStage, nickname, gameManagerServer);
        } else {
            initializeClient(primaryStage, nickname, gameManagerServer);
        }
    }

    private void initializeHost(Stage primaryStage, String nickname, GameManagerServer gameManagerServer) {
        ServerService serverService = new ServerService(1234);
        new Thread(serverService).start();

        Stage waitingRoomStage = new Stage();
        ClientService clientService = new ClientService("127.0.0.1", 1234, nickname, waitingRoomStage);
        new Thread(clientService).start();

//        gameManagerServer.addPlayer(nickname);

        WaitingRoom waitingRoom = new WaitingRoom(
                true,
                ready -> System.out.println("Игрок изменил статус готовности: " + ready),
                ()->toString(),

//                () -> startGame(gameManagerServer, nickname),
                nickname,
                clientService::sendCommand
        );

        waitingRoom.start(waitingRoomStage);
    }

    private void initializeClient(Stage primaryStage, String nickname, GameManagerServer gameManagerServer) {
        String hostIp = showInputDialog("Введите IP хоста:", "IP Адрес");
        if (hostIp == null || hostIp.isBlank()) {
            hostIp="127.0.0.1";
//            showError("IP адрес не может быть пустым. Программа завершена.");
//            return;
        }

        Stage waitingRoomStage = new Stage();
        ClientService clientService = new ClientService(hostIp, 1234, nickname, waitingRoomStage);
        new Thread(clientService).start();


//        gameManagerServer.addPlayer(nickname);

        WaitingRoom waitingRoom = new WaitingRoom(
                false,
                ready -> System.out.println("Игрок изменил статус готовности: " + ready),
//                () -> startGame(gameManagerServer, nickname),
                ()->toString(),
                nickname,
                command -> clientService.sendCommand(command)
        );

        waitingRoom.start(waitingRoomStage);
    }

//    private void startGame(GameManagerServer gameManagerServer, String nickname) {
//        Platform.runLater(() -> {
//            System.out.println("created startGame - "+nickname);
//            GameGUI gameGUI = new GameGUI(gameManagerServer.getGame(), nickname);
//            try {
//                gameGUI.start(new Stage());
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        });
//    }

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
