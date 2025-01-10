package com.monopoly.game.component.money;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class Cash implements Comparable<Cash> {
    private int amount;

    public void addCash(Cash cash){
        amount+=cash.getAmount();
    }

    public void subtractCash(Cash cash){
        amount-= cash.getAmount();
    }

    @Override
    public int compareTo(Cash cash2) {
        return Integer.compare(amount, cash2.amount);
    }
}
