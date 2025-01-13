package com.monopoly.game.from_Server.message;

public record GameMessage(Object type, String sender, String content) {

    @Override
    public String toString() {
        return type + "|" + sender + "|" + content;
    }

    public static GameMessage fromString(String message) {
        String[] parts = message.split("\\|", 3);
        String typeName = parts[0];
        Object type = determineType(typeName);

        return new GameMessage(
                type,
                parts[1],
                parts[2]
        );
    }

    private static Object determineType(String typeName) {
        // Определяем тип сообщения
        try {
            return PreparationMessageType.valueOf(typeName);
        } catch (IllegalArgumentException e) {
            return MessageType.valueOf(typeName);
        }
    }
}
