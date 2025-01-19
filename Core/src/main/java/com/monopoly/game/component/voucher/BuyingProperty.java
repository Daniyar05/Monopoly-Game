package com.monopoly.game.component.voucher;

import com.monopoly.game.component.area.PropertyTile;
import com.monopoly.game.component.model.Player;
import com.monopoly.game.component.money.Cash;

public class BuyingProperty extends AbstractPromissoryNote{
    private final PropertyTile propertyTile;

    public BuyingProperty(Player newOwner, Cash cash, PropertyTile propertyTile) {
        setCash(cash);
        setPlayerTo(newOwner);
        this.propertyTile=propertyTile;
    }

    @Override
    public void execute() {
        getPlayerTo().getWallet().subtractCash(getCash());
        getPlayerTo().addOwnedProperties(propertyTile);
        propertyTile.setOwner(getPlayerTo());

    }
}
