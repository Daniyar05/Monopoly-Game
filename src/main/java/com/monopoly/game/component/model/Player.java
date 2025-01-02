package com.monopoly.game.component.model;

import com.monopoly.game.component.area.PropertyTile;
import com.monopoly.game.component.area.StartTile;
import com.monopoly.game.component.area.Tile;
import com.monopoly.game.component.money.Cash;
import com.monopoly.game.component.voucher.AbstractPromissoryNote;
import com.monopoly.game.component.voucher.PayingRent;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class Player {
    private String name;
    private Cash cash;
    private int position;
    private List<PropertyTile> ownedProperties;

    public Player(String name, Cash cash) {
        this.name = name;
        this.cash = cash;
        this.position = 0;
        this.ownedProperties = new ArrayList<>();
    }

//    public void move(int steps, Board board) {
//        position = (position + steps) % board.getTiles().size();
//        Tile thisTile = board.getTile(position);
//        if (thisTile instanceof PropertyTile){
//            if (this.equals(((PropertyTile) thisTile).getOwner())){
//
//            } else {
//                PayingRent payingRent = new PayingRent(((PropertyTile) thisTile).getOwner());
//                ((PropertyTile) thisTile).getOwner().adjustCash(payingRent, ((PropertyTile) thisTile));
//            }
//        } else if (thisTile instanceof StartTile) {
//
//        }
//    }
//


    public int changePosition(int step){
        position+=step;
        return position;
    }

    public void adjustCash(AbstractPromissoryNote promissoryNote, PropertyTile thisTile) {
        promissoryNote.setPlayerTo(this);
        promissoryNote.setCash((thisTile).getRentAmount());
        promissoryNote.execute();
    }



}
