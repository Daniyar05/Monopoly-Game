package com.monopoly.game.manager;

import com.monopoly.game.component.card.ChanceCard;
import com.monopoly.game.component.card.CommunityChestCard;
import com.monopoly.game.component.card.TypeCard;
import com.monopoly.game.component.model.Player;
import lombok.Getter;

import java.util.Queue;

@Getter
public class CardManager{
    private final Queue<ChanceCard> chanceCardList;
    private final Queue<CommunityChestCard> communityChestCardList;

    public CardManager(Queue<ChanceCard> chanceCardList, Queue<CommunityChestCard> communityChestCardList) {
        this.chanceCardList = chanceCardList;
        this.communityChestCardList = communityChestCardList;
    }

    public void execute(EventManagerConsole eventManager, BoardManager boardManager, PlayerManager playerManager, Player targetPlayer, TypeCard typeCard){

        if (TypeCard.CHANCE_CARD.equals(typeCard)){
            ChanceCard card = chanceCardList.poll();
            //TODO добавить логику и реализацию
            chanceCardList.add(card);
        } else if(TypeCard.COMMUNITY_CHEST.equals(typeCard)){
            CommunityChestCard card = communityChestCardList.poll();
            //TODO добавить логику и реализацию
            communityChestCardList.add(card);
        } else {
            eventManager.notifyAboutAction("The Type Card does not match base types", targetPlayer.getName());
        }
    }
}
