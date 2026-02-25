package org.example;


import org.example.Skills.BasicAttack;

public class Main {
    public static void main(String[] args) {
        Attributes statsHeros = new Attributes(100, 10, 5, 5, 10, 1.2f, 0, 0);
        Character hero = new Character("Arthur", statsHeros);

        Attributes statsRat = new Attributes(30, 2, 5, 1, 1, 1.0f, 0, 0);
        Character monster = new Character("Rat Géant", statsRat);

        hero.getActions().add(new BasicAttack("Attaque de base", 15));

        hero.performAction(0, monster);
    }
}