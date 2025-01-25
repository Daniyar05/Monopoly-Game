package com.monopoly.game.component.area;

import lombok.Getter;

@Getter
public class UtilityTile extends PropertyTile {
    private final int multiplier; // Множитель аренды
    public UtilityTile(int position, String name, int cost, int multiplier, int group) {
        super(position, name, cost, cost*multiplier/100, group);
        this.multiplier=multiplier;
    }
}