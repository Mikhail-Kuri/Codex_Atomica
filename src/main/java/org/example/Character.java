package org.example;

public class Character {
    private String name;
    private String heroClass;
    private Attributes baseAttributes ;
    private int currentHP;

    public Character(String name, Attributes stats) {
        this.name = name;
        this.baseAttributes = stats;
        this.currentHP = stats.vitality;
    }
}
