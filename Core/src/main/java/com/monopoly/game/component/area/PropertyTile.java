package com.monopoly.game.component.area;

import com.monopoly.game.component.model.Player;
import com.monopoly.game.component.money.Cash;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class PropertyTile extends Tile{
    private Cash cost;
    private Cash rent;
    private Player owner;
    private final int group;

    public PropertyTile(int position, String name, int costAmount, int rentAmount, int group) {
        super(position, name);
        this.cost = new Cash(costAmount);
        this.rent = new Cash(rentAmount);
        this.group = group;
    }

    public Cash getRent() {
        return owner.ownsFullGroup(group) ? new Cash(rent.getAmount() * 2) : rent;
    }

}
