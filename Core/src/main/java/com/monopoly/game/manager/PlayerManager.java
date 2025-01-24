package com.monopoly.game.manager;

import com.monopoly.game.component.area.PropertyTile;
import com.monopoly.game.component.model.Player;
import com.monopoly.game.component.money.Cash;
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

    public Player deleteOwnerTile(String playerName, String tileName) {
        Player targetPlayer = players.stream()
                .filter(player -> player.getName().equals(playerName))
                .findFirst()
                .orElse(null);

        if (targetPlayer == null) {
            return null; // Игрок не найден
        }

        // Находим клетку по имени среди владений игрока
        PropertyTile tileToRemove = targetPlayer.getOwnedProperties().stream()
                .filter(tile -> tile.getName().equals(tileName))
                .findFirst()
                .orElse(null);

        if (tileToRemove != null) {
            // Удаляем владение
            targetPlayer.getOwnedProperties().remove(tileToRemove);
            tileToRemove.setOwner(null); // Сбрасываем владельца клетки

            // Обновляем баланс (возвращаем 50% стоимости)
            int sellPrice = tileToRemove.getCost().getAmount() / 2;
            targetPlayer.getWallet().addCash(new Cash(sellPrice));
        }
        return targetPlayer;

    }
}
