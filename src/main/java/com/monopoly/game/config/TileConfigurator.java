package com.monopoly.game.config;

import com.monopoly.game.component.area.*;
import java.util.List;

public class TileConfigurator {

    public static List<Tile> configureTiles() {
        return List.of(
                new StartTile(0, "Go"),
                new PropertyTile(1,"Mediterranean Avenue", 60, 2),
                new CommunityChestTile(2,"Community Chest"),
                new PropertyTile(3,"Baltic Avenue", 60,4),
                new TaxTile(4,"Income Tax", 200, 0.1),
                new PropertyTile(5, "Reading Railroad", 200, 25),
                new PropertyTile(6,"Oriental Avenue", 100, 6),
                new ChanceTile(7,"Chance"),
                new PropertyTile(8,"Vermont Avenue", 100, 6),
                new PropertyTile(9,"Connecticut Avenue", 120, 8),
                new JailTile(10, "Jail"),
                new PropertyTile(11, "St. Charles Place", 140,10),
                new UtilityTile(12, "Electric Company", 150, 10),
                new PropertyTile(13,"States Avenue", 140,10),
                new PropertyTile(14, "Virginia Avenue", 160,12),
                new PropertyTile(15, "St. James Place", 180,14),
                new FreeParkingTile(16, "Free Parking"),
                new PropertyTile(17, "Tennessee Avenue", 180,14),
                new PropertyTile(18, "New York Avenue", 200,16),
                new ChanceTile(19,"Chance"),
                new PropertyTile(20,"Kentucky Avenue", 220,18),
                new PropertyTile(21,"Indiana Avenue", 220,18),
                new GoToJailTile(22, "Go to Jail"),
                new PropertyTile(23,"Pacific Avenue", 300,26),
                new PropertyTile(24, "North Carolina Avenue", 300, 26),
                new PropertyTile(25, "Park Place", 350,35),
                new PropertyTile(26, "Boardwalk", 400,50)
        );
    }
}
