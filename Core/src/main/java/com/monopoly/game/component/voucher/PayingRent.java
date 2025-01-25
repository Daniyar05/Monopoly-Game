package com.monopoly.game.component.voucher;

import com.monopoly.game.component.model.Player;
import lombok.Setter;

@Setter
public class PayingRent extends AbstractPromissoryNote{
    private final Player playerFrom;

    public PayingRent(Player playerFrom) {
        this.playerFrom = playerFrom;
    }


    @Override
    public void execute() {
        addCashForPlayer(getPlayerTo());
        subtractCashForPlayer(playerFrom);
    }


    public void addCashForPlayer(Player playerTo){
        playerTo.getWallet().addCash(getCash());
    }
    public void subtractCashForPlayer(Player playerTo){
        playerTo.getWallet().subtractCash(getCash());
    }
}