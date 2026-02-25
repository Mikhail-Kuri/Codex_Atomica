package org.example.Skills;

import org.example.Character;

public abstract class Action {

    protected String name;
    protected int basePower;

    public Action(String name, int basePower) {
        this.name = name;
        this.basePower = basePower;
    }

    public Action() {

    }

    public Action(String attaqueDeBase) {

    }

    public abstract void execute(org.example.Character attacker, Character target);
}
