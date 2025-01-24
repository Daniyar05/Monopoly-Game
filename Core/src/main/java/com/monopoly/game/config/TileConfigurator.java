package com.monopoly.game.config;

import com.monopoly.game.component.area.*;

import java.util.List;

public class TileConfigurator {

    public static List<Tile> configureTiles() {
        return List.of(
                new StartTile(0),
                new PropertyTile(1, "Mediterranean Avenue", 60, 2, 1),   // Группа 1 (Коричневые)
                new CommunityChestTile(2),
                new PropertyTile(3, "Baltic Avenue", 60, 4, 1),          // Группа 1
                new TaxTile(4, "Income Tax", 200, 0.1),
                new PropertyTile(5, "Reading Railroad", 200, 25, 10),    // Группа 10 (Железные дороги)
                new PropertyTile(6, "Oriental Avenue", 100, 6, 2),       // Группа 2 (Светло-синие)
                new ChanceTile(7),
                new PropertyTile(8, "Vermont Avenue", 100, 6, 2),        // Группа 2
                new PropertyTile(9, "Connecticut Avenue", 120, 8, 2),    // Группа 2
                new JailTile(10),

                new PropertyTile(11, "St. Charles Place", 140, 10, 3),   // Группа 3 (Розовые)
                new UtilityTile(12, "Electric Company", 150, 10, 11),     // Группа 11 (Коммунальные)
                new PropertyTile(13, "States Avenue", 140, 10, 3),       // Группа 3
                new PropertyTile(14, "Virginia Avenue", 160, 12, 3),     // Группа 3
                new PropertyTile(15, "Pennsylvania Railroad", 200, 25, 10), // Группа 10
                new PropertyTile(16, "St. James Place", 180, 14, 4),     // Группа 4 (Оранжевые)
                new CommunityChestTile(17),
                new PropertyTile(18, "Tennessee Avenue", 180, 14, 4),    // Группа 4
                new PropertyTile(19, "New York Avenue", 200, 16, 4),     // Группа 4
                new FreeParkingTile(20, "Free Parking"),

                new PropertyTile(21, "Kentucky Avenue", 220, 18, 5),     // Группа 5 (Красные)
                new ChanceTile(22),
                new PropertyTile(23, "Indiana Avenue", 220, 18, 5),      // Группа 5
                new PropertyTile(24, "Illinois Avenue", 240, 20, 5),     // Группа 5
                new PropertyTile(25, "B&O Railroad", 200, 25, 10),       // Группа 10
                new PropertyTile(26, "Atlantic Avenue", 260, 22, 6),     // Группа 6 (Желтые)
                new PropertyTile(27, "Ventnor Avenue", 260, 22, 6),      // Группа 6
                new UtilityTile(28, "Water Works", 150, 10, 11),          // Группа 11
                new PropertyTile(29, "Marvin Gardens", 280, 24, 6),      // Группа 6
                new GoToJailTile(30),

                new PropertyTile(31, "Pacific Avenue", 300, 26, 7),      // Группа 7 (Зеленые)
                new PropertyTile(32, "North Carolina Avenue", 300, 26, 7), // Группа 7
                new CommunityChestTile(33),
                new PropertyTile(34, "Pennsylvania Avenue", 320, 28, 7), // Группа 7
                new PropertyTile(35, "Short Line Railroad", 200, 25, 10),// Группа 10
                new ChanceTile(36),
                new PropertyTile(37, "Park Place", 350, 35, 8),          // Группа 8 (Синие)
                new TaxTile(38, "Luxury Tax", 100, 0),
                new PropertyTile(39, "Boardwalk", 400, 50, 8)            // Группа 8
        );
    }

    public static Tile getTileByPosition(int position) {
        List<Tile> tiles = configureTiles();
        if (position < 0 || position >= tiles.size()) {
            System.err.println("Ошибка: Позиция " + position + " выходит за пределы игрового поля!");
            return null;
        }
        return tiles.get(position);
    }
}
