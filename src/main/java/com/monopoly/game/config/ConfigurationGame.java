package com.monopoly.game.config;

import com.monopoly.game.component.area.Tile;
import com.monopoly.game.component.model.Player;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class ConfigurationGame {
    private List<Player> players;
    private List<Tile> tiles;
}
