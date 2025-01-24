package com.monopoly.game.from_Server.message;

public record GameEventMessage(Object type, String sender, Object content) {

    @Override
    public String toString() {
        return type + "|" + sender + "|" + serializeContent(content);
    }

    public static GameEventMessage fromString(String message) {
        String[] parts = message.split("\\|", 3);
        String typeName = parts[0];
        Object type = determineType(typeName);
        Object content = deserializeContent(parts[2]);
        return new GameEventMessage(
                type,
                parts[1],
                content
        );
    }


    private static Object determineType(String typeName) {
        try {
            return PreparationMessageType.valueOf(typeName);
        } catch (IllegalArgumentException e) {
            return MessageType.valueOf(typeName);
        }
    }


    private static String serializeContent(Object content) {
        if (content instanceof String) {
            return (String) content;
        }
        if (content instanceof String[]) {
            return String.join("$", (String[]) content);
        }
        return content.toString();
    }


    private static Object deserializeContent(String contentString) {
        if (contentString.contains("$")) {
            return contentString.split("\\$");
        }
        return contentString;
    }
}