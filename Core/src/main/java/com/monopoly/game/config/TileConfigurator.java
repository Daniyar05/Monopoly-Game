package com.monopoly.game.config;

import com.monopoly.game.component.area.*;

import java.util.List;

public class TileConfigurator {

    public static List<Tile> configureTiles() {
        return List.of(
                new StartTile(0),
                new PropertyTile(1,"Mediterranean Avenue", 60, 2),
                new CommunityChestTile(2),
                new PropertyTile(3,"Baltic Avenue2", 60,4),
                new CommunityChestTile(4),
                new PropertyTile(5,"Baltic Avenue", 60,4),
                new TaxTile(6,"Income Tax", 200, 0.1),
                new PropertyTile(7, "Reading Railroad", 200, 25),
                new PropertyTile(8,"Oriental Avenue", 100, 6),
                new ChanceTile(9),
                new PropertyTile(10,"Vermont Avenue", 100, 6),
                new PropertyTile(11,"Connecticut Avenue", 120, 8),
                new JailTile(12),
                new PropertyTile(13, "St. Charles Place", 140,10),
                new UtilityTile(14, "Electric Company", 150, 10),
                new PropertyTile(15,"States Avenue", 140,10),
                new PropertyTile(16, "Virginia Avenue", 160,12),
                new PropertyTile(17, "St. James Place", 180,14),
                new FreeParkingTile(18, "Free Parking"),
                new PropertyTile(19, "Tennessee Avenue", 180,14),
                new PropertyTile(20, "New York Avenue", 200,16),
                new ChanceTile(21),
                new PropertyTile(22,"Kentucky Avenue", 220,18),
                new PropertyTile(23,"Indiana Avenue", 220,18),
                new GoToJailTile(24),
                new PropertyTile(25,"Pacific Avenue", 300,26),
                new PropertyTile(26, "North Carolina Avenue", 300, 26),
                new PropertyTile(27, "Park Place", 350,35),
                new PropertyTile(28, "Boardwalk", 400,50)
        );
    }
}
