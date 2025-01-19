package com.monopoly.game.action.processGame;

import com.monopoly.game.component.model.Player;
import lombok.Getter;

//@Getter
public record EventMoveGame(Player player, BasePlayerAction playerAction, Object objectTransaction) {

}
