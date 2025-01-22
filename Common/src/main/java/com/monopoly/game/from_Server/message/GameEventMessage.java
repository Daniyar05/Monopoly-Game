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
        return content.toString(); // Fallback for other types
    }


    private static Object deserializeContent(String contentString) {
        if (contentString.contains("$")) {
            return contentString.split("\\$");
        }
        return contentString; // Fallback for simple strings
    }


    public String[] getSplitContent() {
        if (content instanceof String contentStr) {
            return contentStr.split("\\$");
        }
        if (content instanceof String[] contentArray) {
            return contentArray;
        }
        throw new UnsupportedOperationException("Content is not a String or String array.");
    }


    public static String stringOf(String[] strings) {
        return String.join("$", strings);
    }

}
