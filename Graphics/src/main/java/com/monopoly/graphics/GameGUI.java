package com.monopoly.graphics;

import com.monopoly.game.from_Server.message.GameMessage;
import com.monopoly.game.from_Server.message.MessageType;
import com.monopoly.game.from_Server.service.ClientServiceInterface;
import com.monopoly.graphics.rendering.WindowSettingForGUI;
import javafx.application.Application;
import javafx.stage.Stage;


public class GameGUI extends Application {
    private static final int TILE_SIZE = 100; // Размер одной клетки
    private static final int BOARD_SIZE = 8; // Количество клеток на стороне
    private static final int WINDOW_SIZE = 800; // Фиксированный размер окна
    private static final int PLAYER_POSITION = 0; // Пример позиции игрока
    private final ClientServiceInterface clientService;

    private WindowSettingForGUI windowSetting;
    private Stage stage;

    public WindowSettingForGUI getWindowSetting() {
        return windowSetting;
    }

    private final String playerName;

    public GameGUI(String playerName, ClientServiceInterface clientService) {
        this.clientService=clientService;
        this.playerName=playerName;
    }
    @Override
    public void start(Stage stage)  {
        this.stage=stage;
        this.windowSetting = new WindowSettingForGUI(TILE_SIZE, BOARD_SIZE, WINDOW_SIZE,PLAYER_POSITION, stage, clientService, playerName);
    }

    public void updateWindow(){
        this.windowSetting.update(stage);
    }

    public static void main(String[] args) {
        launch(args);
    }
//    public void sendMoveCommand(String moveData) {
////        clientService.sendCommand("MOVE " + moveData);
//        clientService.sendCommand(new GameMessage(
//                MessageType.PLAYER_MOVED,
//                playerName,
//                //FIXME добавить полезную информацию (желательно систематизировать вложенным запросом
//                moveData
//
//        ));
//
//    }

}
