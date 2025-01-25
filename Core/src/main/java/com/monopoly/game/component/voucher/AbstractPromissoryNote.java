package com.monopoly.game.component.voucher;

import com.monopoly.game.component.model.Player;
import com.monopoly.game.component.money.Cash;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class AbstractPromissoryNote {
    private Cash cash;
    private Player playerTo;

    public void execute() {
        System.err.println("default execute method in AbstractPromissoryNote");
    }
}
