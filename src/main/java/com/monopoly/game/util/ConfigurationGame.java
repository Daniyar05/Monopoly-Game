package com.monopoly.game.util;

import com.monopoly.game.component.model.Player;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class ConfigurationGame {
    private List<Player> players;


}
