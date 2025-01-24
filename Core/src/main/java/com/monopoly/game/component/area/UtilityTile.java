package com.monopoly.game.component.area;

import com.monopoly.game.component.model.Player;
import com.monopoly.game.manager.EventManager;
import lombok.Getter;

@Getter
public class UtilityTile extends PropertyTile {
    private final int multiplier; // Множитель аренды (зависит от кол-ва служб у владельца)
    public UtilityTile(int position, String name, int cost, int multiplier, int group) {
        super(position, name, cost, cost*multiplier, group);
        this.multiplier=multiplier;
    }

}