package com.monopoly.game.component.area;

import com.monopoly.game.component.card.CommunityChestCard;
import com.monopoly.game.component.card.communityChest.AddMoneyCard;
import com.monopoly.game.component.card.communityChest.MoveToTileCard;
import com.monopoly.game.component.card.communityChest.PayCard;
import com.monopoly.game.component.model.Player;
import com.monopoly.game.manager.EventManager;

import java.util.*;

public class CommunityChestTile extends Tile {
    private final List<CommunityChestCard> communityChestCards;

    public CommunityChestTile(int position) {
        super(position, "Community Chest");
        this.communityChestCards = initializeCards();
        Collections.shuffle(communityChestCards);
    }

    private List<CommunityChestCard> initializeCards() {
        List<CommunityChestCard> cards = new ArrayList<>();
        cards.add(new AddMoneyCard("Найденная банковская ошибка. Получите $200.", 200));
        cards.add(new AddMoneyCard("Верните долг за страховку. Получите $100.", 100));
        cards.add(new AddMoneyCard("Вы получили возврат налога на доход. Получите $20.", 20));
        cards.add(new AddMoneyCard("Вы выиграли второй приз на конкурсе красоты. Получите $10.", 10));
        cards.add(new AddMoneyCard("Соберите $50 за услуги.", 50));
        cards.add(new AddMoneyCard("Ваше наследство. Получите $100.", 100));
        cards.add(new PayCard("Оплатите медицинские счета. Заплатите $100.", 100));
        cards.add(new PayCard("Оплатите услуги больницы. Заплатите $100.", 100));
        cards.add(new PayCard("Оплатите услуги доктора. Заплатите $50.", 50));
        cards.add(new PayCard("Оплатите обучение ребенка. Заплатите $150.", 150));
        cards.add(new MoveToTileCard("Переместитесь на поле Вперёд (Go). Получите $200.", 0));
        cards.add(new MoveToTileCard("Отправляйтесь в тюрьму.", 10));
        cards.add(new AddMoneyCard("Вам выплачены дивиденды по акции. Получите $50.", 50));
        cards.add(new AddMoneyCard("Вы получили возврат кредита в $25.", 25));
        return cards;
    }

    @Override
    public void execute(Player player, EventManager eventManager) {
        if (!communityChestCards.isEmpty()) {
            CommunityChestCard card = communityChestCards.remove(0);
            System.out.println("Community Chest: " + card.getDescription());
            card.executeEffect(player, eventManager);
            communityChestCards.add(card);
        }
    }
}
