package com.monopoly.game.from_Server.message;

public enum EventEnum {
    BUY_IT("Хотите ли купить?"),
    ;

    private final String value;
    private String playerName;

    EventEnum(String value) {
        this.value=value;
    }

    public String getValue() {
        return value;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public String getFrom() {
        return playerName;
    }
}