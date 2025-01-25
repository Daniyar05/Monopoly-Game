package com.monopoly.graphics.util;

import javafx.scene.paint.Color;

public class ColorUtil {
    public static String getColorForUser(String username) {
        if (username == null || username.isEmpty()) {
            return "#FFFFFF";
        }

        int hash = username.hashCode();
        float hue = (hash % 360 + 360) % 360;
        Color color = Color.hsb(hue, 0.7, 0.8);

        return String.format("#%02X%02X%02X",
                (int) (color.getRed() * 255),
                (int) (color.getGreen() * 255),
                (int) (color.getBlue() * 255));
    }
}
