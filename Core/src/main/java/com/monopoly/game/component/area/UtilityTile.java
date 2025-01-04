package com.monopoly.game.component.area;

import lombok.Getter;

@Getter
public class UtilityTile extends Tile {
    private final int cost;  // Цена покупки
    private final int multiplier; // Множитель аренды (зависит от кол-ва служб у владельца)
    public UtilityTile(int position, String name, int cost, int multiplier) {
        super(position, name);
        this.cost=cost;
        this.multiplier=multiplier;
    }

}