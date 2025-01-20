package com.monopoly.graphics.util;

import javafx.scene.paint.Color;

public class ColorUtil {
    public static String getColorForUser(String username) {
        if (username == null || username.isEmpty()) {
            return "#FFFFFF"; // Белый цвет по умолчанию
        }

        // Хэшируем имя пользователя для получения уникального цвета
        int hash = username.hashCode();
        float hue = (hash % 360 + 360) % 360; // Обеспечиваем, что hue будет в пределах 0-360
        Color color = Color.hsb(hue, 0.7, 0.8); // Генерируем цвет с насыщенностью 70% и яркостью 80%

        return String.format("#%02X%02X%02X",
                (int) (color.getRed() * 255),
                (int) (color.getGreen() * 255),
                (int) (color.getBlue() * 255));
    }
}
