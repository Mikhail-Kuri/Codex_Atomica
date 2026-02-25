package org.example;

import org.example.Attributes;
import org.example.Skills.Action;
import org.example.Skills.BasicAttack;

import java.util.Collection;
import java.util.List;

public class Character {
    private final String name;
    private Attributes attributes;
    private int currentHP;
    private List<Action> actions;

    public Character(String name, Attributes attributes, int currentHP, List<Action> actions) {
        this.name = name;
        this.attributes = attributes;
        this.currentHP = currentHP;
        this.actions = actions;
    }

    public Character(String arthur, Attributes statsHeros) {
        this.name = arthur;
        this.attributes = statsHeros;
        this.currentHP = statsHeros.vitality;
        this.actions = new java.util.ArrayList<>();
    }


    public void performAction(int actionIndex, Character target) {
        if (actionIndex >= 0 && actionIndex < actions.size()) {
            Action selectedAction = actions.get(actionIndex);

            selectedAction.execute(this, target);
        } else {
            System.out.println(this.name + " ne sait pas quoi faire !");
        }
    }

    public void takeDamage(int amount) {
        this.currentHP -= amount;
        if (this.currentHP < 0) this.currentHP = 0;
        System.out.println("HP restant : de "+this.name +  " :" + this.currentHP );
    }

    public Attributes getAttributes() { return attributes; }
    public String getName() { return name; }
    public int getCurrentHP() { return currentHP; }

    public Collection<Action> getActions() {
        return actions;
    }
}