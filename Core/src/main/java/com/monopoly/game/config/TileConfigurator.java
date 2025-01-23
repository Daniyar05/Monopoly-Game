package com.monopoly.game.config;

import com.monopoly.game.component.area.*;

import java.util.List;

public class TileConfigurator {

    public static List<Tile> configureTiles() {
        return List.of(
                // Верхняя сторона (слева направо)
                new StartTile(0), // Вперёд (Go)
                new PropertyTile(1, "Mediterranean Avenue", 60, 2), // Собственность
                new CommunityChestTile(2), // Общественная казна
                new PropertyTile(3, "Baltic Avenue", 60, 4), // Собственность
                new TaxTile(4, "Income Tax", 200, 0.1), // Подоходный налог
                new PropertyTile(5, "Reading Railroad", 200, 25), // Железная дорога
                new PropertyTile(6, "Oriental Avenue", 100, 6), // Собственность
                new ChanceTile(7), // Шанс
                new PropertyTile(8, "Vermont Avenue", 100, 6), // Собственность
                new PropertyTile(9, "Connecticut Avenue", 120, 8), // Собственность
                new JailTile(10), // Тюрьма (Просто посещение)

                // Правая сторона (сверху вниз)
                new PropertyTile(11, "St. Charles Place", 140, 10), // Собственность
                new UtilityTile(12, "Electric Company", 150, 10), // Электрическая компания
                new PropertyTile(13, "States Avenue", 140, 10), // Собственность
                new PropertyTile(14, "Virginia Avenue", 160, 12), // Собственность
                new PropertyTile(15, "Pennsylvania Railroad", 200, 25), // Железная дорога
                new PropertyTile(16, "St. James Place", 180, 14), // Собственность
                new CommunityChestTile(17), // Общественная казна
                new PropertyTile(18, "Tennessee Avenue", 180, 14), // Собственность
                new PropertyTile(19, "New York Avenue", 200, 16), // Собственность
                new FreeParkingTile(20, "Free Parking"), // Бесплатная стоянка

                // Нижняя сторона (справа налево)
                new PropertyTile(21, "Kentucky Avenue", 220, 18), // Собственность
                new ChanceTile(22), // Шанс
                new PropertyTile(23, "Indiana Avenue", 220, 18), // Собственность
                new PropertyTile(24, "Illinois Avenue", 240, 20), // Собственность
                new PropertyTile(25, "B&O Railroad", 200, 25), // Железная дорога
                new PropertyTile(26, "Atlantic Avenue", 260, 22), // Собственность
                new PropertyTile(27, "Ventnor Avenue", 260, 22), // Собственность
                new PropertyTile(28, "Water Works", 150, 10), // Водопровод //TODO добавить UTILITY Tile !!
                new PropertyTile(29, "Marvin Gardens", 280, 24), // Собственность
                new GoToJailTile(30), // Отправляйтесь в тюрьму

                // Левая сторона (снизу вверх)
                new PropertyTile(31, "Pacific Avenue", 300, 26), // Собственность
                new PropertyTile(32, "North Carolina Avenue", 300, 26), // Собственность
                new CommunityChestTile(33), // Общественная казна
                new PropertyTile(34, "Pennsylvania Avenue", 320, 28), // Собственность
                new PropertyTile(35, "Short Line Railroad", 200, 25), // Железная дорога
                new ChanceTile(36), // Шанс
                new PropertyTile(37, "Park Place", 350, 35), // Собственность
                new TaxTile(38, "Luxury Tax", 100, 0), // Налог на роскошь
                new PropertyTile(39, "Boardwalk", 400, 50) // Собственность
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
