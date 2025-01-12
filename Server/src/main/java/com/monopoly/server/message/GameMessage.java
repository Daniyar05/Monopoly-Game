package com.monopoly.server.message;

public record GameMessage(MessageType type, String sender, String content) {

    @Override
    public String toString() {
        return type + "|" + sender + "|" + content;
    }

    public static GameMessage fromString(String message) {
        String[] parts = message.split("\\|", 3);
        return new GameMessage(
                MessageType.valueOf(parts[0]),
                parts[1],
                parts[2]
        );
    }
}
