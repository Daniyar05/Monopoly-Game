package com.monopoly.game.component.model;

import com.monopoly.game.component.card.AbstractCard;
import com.monopoly.game.component.money.Cash;
import com.monopoly.game.component.area.PropertyTile;
import com.monopoly.game.component.voucher.AbstractPromissoryNote;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class Player {
    private boolean hasJailFreeCard;
    private String name;
    private Cash wallet;
    private int position;
    private int countSkipSteps=0;
    private List<PropertyTile> ownedProperties;
    private List<AbstractCard> cardList;
    private boolean isBankrupt = false;

    public Player(String name, Cash cash) {
        this.name = name;
        this.wallet = cash;
        this.position = 0;
        this.ownedProperties = new ArrayList<>();
        this.cardList = new ArrayList<>();
    }

    public void reduceCountSkipSteps(){
        countSkipSteps--;
    }

    public int changePosition(int step){
        position+=step;
        return position;
    }

    public void adjustCash(AbstractPromissoryNote promissoryNote, PropertyTile thisTile) {
        promissoryNote.setPlayerTo(this);
        promissoryNote.setCash(thisTile.getRent());
        promissoryNote.execute();
    }

    public void addOwnedProperties(PropertyTile propertyTile){
        ownedProperties.add(propertyTile);
    }


    public boolean enoughCash(Cash cash) {

        return wallet.compareTo(cash)>=0;
    }

    public void useJailFreeCard() {
        if(hasJailFreeCard) {
            hasJailFreeCard = false;
        }
    }

    public void addJailFreeCard() {
        hasJailFreeCard = true;
    }

    public boolean ownsFullGroup(int group) {
        long ownedInGroup = ownedProperties.stream()
                .filter(p -> p.getGroup() == group)
                .count();

        return switch(group) {
            case 1, 2, 3, 4, 5, 6, 7, 8 -> ownedInGroup == 3; // Для обычных цветных групп
            case 10 -> ownedInGroup == 4;                     // Железные дороги
            case 11 -> ownedInGroup == 2;                     // Коммунальные предприятия
            default -> false;
        };
    }
    public void declareBankruptcy() {
        this.isBankrupt = true;
        ownedProperties.forEach(p -> p.setOwner(null));
    }
}
