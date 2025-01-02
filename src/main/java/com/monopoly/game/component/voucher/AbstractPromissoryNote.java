package com.monopoly.game.component.voucher;

import com.monopoly.game.component.model.Player;
import com.monopoly.game.component.money.Cash;
import lombok.Setter;

@Setter
public abstract class AbstractPromissoryNote {
    private Cash cash;
    private Player playerTo;


    public int getAmountOfMoney(){
        return cash.getAmount();
    }

    public void execute() {}
}
