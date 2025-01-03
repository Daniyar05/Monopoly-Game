package com.monopoly.game.manager;

import com.monopoly.game.component.model.Player;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
@Getter
public class PlayerManager {
    private List<Player> players;
    private short indexNowPlayer=0;

    public PlayerManager(List<Player> players) {
        this.players = players;
    }

    public void start(List<Player> players) {
        this.players = players;
//        moneyManager.setPlayers(players);
    }


    public void stop() {
    }

    public Player nowPlayer() {
        return players.get(indexNowPlayer % players.size());
    }

    public void nextPlayer(){
        indexNowPlayer++;
    }


    public int move(int step) {
        return nowPlayer().changePosition(step);
    }
    public void addPlayer(Player player){
        players.add(player);
    }
}
