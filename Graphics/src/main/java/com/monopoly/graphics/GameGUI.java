package com.monopoly.graphics;

import com.monopoly.game.from_Server.service.ClientServiceInterface;
import com.monopoly.graphics.rendering.WindowSettingForGUI;
import javafx.application.Application;
import javafx.stage.Stage;


public class GameGUI extends Application {
    private static final int TILE_SIZE = 80;
    private static final int BOARD_SIZE = 11;
    private static final int WINDOW_SIZE = 880;
    private static final int PLAYER_POSITION = 0;
    private final ClientServiceInterface clientService;

    private WindowSettingForGUI windowSetting;
//    private Stage stage;

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
//        this.stage=stage;
        this.windowSetting = new WindowSettingForGUI(TILE_SIZE, BOARD_SIZE, WINDOW_SIZE,PLAYER_POSITION, stage, clientService, playerName);
    }

    public static void main(String[] args) {
        launch(args);
    }

    public void updatePlayerPosition(String sender, String content) {
        windowSetting.updatePlayerPosition(sender, content);
    }
}
