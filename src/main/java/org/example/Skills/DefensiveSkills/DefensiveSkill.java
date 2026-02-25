package org.example.Skills.DefensiveSkills;

import org.example.Character;

public abstract class DefensiveSkill {
    protected String name;

    public DefensiveSkill(String name) {
        this.name = name;
    }

    public abstract int reduceDamage(int rawDamage, Character owner);

    public String getName() {
        return name;
    }


}