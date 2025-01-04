package com.monopoly.game.component.model;


public class Dice {
    public static int roll(){
        return (int) (Math.random() * 6) + 1;
    }
}
