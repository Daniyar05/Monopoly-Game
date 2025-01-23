package com.monopoly.game.component.area;

import com.monopoly.game.component.card.ChanceCard;
import com.monopoly.game.component.card.chance.GetOutOfJailCard;
import com.monopoly.game.component.card.chance.MoveToTileCard;
import com.monopoly.game.component.card.chance.PayTaxCard;
import com.monopoly.game.component.model.Player;
import com.monopoly.game.manager.EventManager;

import java.util.*;

public class ChanceTile extends Tile {
    private final List<ChanceCard> chanceCards;
    private final Random random = new Random();

    public ChanceTile(int position) {
        super(position, "Chance");
        this.chanceCards = initializeCards();
        Collections.shuffle(chanceCards); // Перемешиваем колоду
    }

    private List<ChanceCard> initializeCards() {
        List<ChanceCard> cards = new ArrayList<>();
        cards.add(new MoveToTileCard("Переместитесь на поле Вперёд (Go)", 0));
        cards.add(new MoveToTileCard("Отправляйтесь в тюрьму", 10));
        cards.add(new PayTaxCard("Оплатите ремонт дорог: $150", 150));
        cards.add(new GetOutOfJailCard("Освобождение из тюрьмы. Сохраните эту карту."));
        cards.add(new MoveToTileCard("Переместитесь на Парковку", 20));
        cards.add(new PayTaxCard("Штраф за превышение скорости: $50", 50));
        return cards;
    }

    @Override
    public void execute(Player player, EventManager eventManager) {
        if (!chanceCards.isEmpty()) {
            ChanceCard card = chanceCards.remove(0); // Берем верхнюю карту
            System.out.println("Шанс: " + card.getDescription());
            card.executeEffect(player, eventManager);
            chanceCards.add(card); // Возвращаем карту в конец колоды
        }
    }
}