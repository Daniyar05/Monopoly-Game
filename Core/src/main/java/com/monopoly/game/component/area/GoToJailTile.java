package com.monopoly.game.component.area;

import com.monopoly.game.component.model.Player;

public class GoToJailTile extends Tile{
    public GoToJailTile(int position) {
        super(position, "Go to Jail");
    }

    @Override
    public void execute(Player player) {
        // TODO добавить реализацию
        // TODO продумать работу с meneger's из таких ячеек
        //  (можно сет сделать, можно изменить сигнатуру, добавть доп конфигурацию, и так уже держать все менеджеры)
    }
}