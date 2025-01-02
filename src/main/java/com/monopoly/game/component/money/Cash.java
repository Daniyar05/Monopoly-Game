package com.monopoly.game.component.money;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class Cash {
    private int amount;

    public void addCash(Cash cash){
        amount+=cash.getAmount();
    }

    public void subtractCash(Cash cash){
        amount-= cash.getAmount();
    }
}
