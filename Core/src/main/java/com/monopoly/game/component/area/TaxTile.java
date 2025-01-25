package com.monopoly.game.component.area;

import com.monopoly.game.component.card.chance.PayTaxCard;
import com.monopoly.game.component.model.Player;
import com.monopoly.game.manager.EventManager;
import lombok.Getter;

@Getter
public class TaxTile extends Tile {
    private final int fixedTax;
    private final double percentageTax;


    public TaxTile(int position, String name, int fixedTax, double percentageTax) {
        super(position, name);
        this.fixedTax = fixedTax;
        this.percentageTax = percentageTax;
    }

    @Override
    public void execute(Player player, EventManager eventManager) {
        new PayTaxCard(name, fixedTax).executeEffect(player, eventManager);
    }
}
