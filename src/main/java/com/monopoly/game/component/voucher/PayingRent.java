package com.monopoly.game.component.voucher;

import com.monopoly.game.component.model.Player;
import com.monopoly.game.component.money.Cash;
import lombok.Setter;

public class PayingRent extends AbstractPromissoryNote{
    @Setter
    private Cash cash;
    private final Player playerFrom;
    private Player playerTo;

    public PayingRent(Player playerFrom) {
        this.playerFrom = playerFrom;
    }


    @Override
    public void execute() {
        addCashForPlayer(playerTo);
        subtractCashForPlayer(playerFrom);
    }


    public void addCashForPlayer(Player playerTo){
        playerTo.getWallet().addCash(cash);
    }
    public void subtractCashForPlayer(Player playerTo){
        playerTo.getWallet().subtractCash(cash);
    }
}