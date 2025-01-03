package com.monopoly.game.component.model;

import lombok.Getter;

@Getter
public enum EventEnum {
    BUY_IT("Хотите ли купить?"),
    ;

    private final String value;

    EventEnum(String value) {
        this.value=value;
    }

}
