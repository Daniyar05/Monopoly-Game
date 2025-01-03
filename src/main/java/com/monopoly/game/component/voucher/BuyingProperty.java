package com.monopoly.game.component.voucher;

import com.monopoly.game.component.area.PropertyTile;
import com.monopoly.game.component.model.Player;
import com.monopoly.game.component.money.Cash;

public class BuyingProperty extends AbstractPromissoryNote{
    private Cash cash;
    private Player newOwner;
    private PropertyTile propertyTile;

    public BuyingProperty(Player newOwner, Cash cash, PropertyTile propertyTile) {
        this.cash = cash;
        this.newOwner= newOwner;
        this.propertyTile=propertyTile;
    }

    @Override
    public void execute() {
        newOwner.getWallet().subtractCash(cash);
        newOwner.addOwnedProperties(propertyTile);
        propertyTile.setOwner(newOwner);
    }
}
