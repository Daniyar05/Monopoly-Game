package com.monopoly.game.component.area;

import com.monopoly.game.component.model.Player;
import com.monopoly.game.component.money.Cash;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class PropertyTile extends Tile{
    private Cash rentAmount;
    private Player owner;

    public PropertyTile(int position, String name) {
        super(position, name);
    }

}
