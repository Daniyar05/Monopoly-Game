package com.monopoly.game.component.model;

import java.util.Scanner;

public class Event {



    public boolean createAndListenerYesNoChoice(String question) {
        System.out.println(question);
        Scanner scanner = new Scanner(System.in);
        return "yes".equals(scanner.next());
    }
}
