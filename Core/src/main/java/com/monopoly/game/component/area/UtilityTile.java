package com.monopoly.game.component.area;

import lombok.Getter;

@Getter
public class UtilityTile extends PropertyTile {
    private final int multiplier; // Множитель аренды (зависит от кол-ва служб у владельца)
    public UtilityTile(int position, String name, int cost, int multiplier, int group) {
        super(position, name, cost, cost*multiplier/100, group);
        this.multiplier=multiplier;
    }
}