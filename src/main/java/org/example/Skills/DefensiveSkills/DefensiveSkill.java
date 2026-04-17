package org.example.Skills.DefensiveSkills;

import org.example.core.character.Character;

public abstract class DefensiveSkill {
    protected String name;

    public DefensiveSkill(String name) {
        this.name = name;
    }

    public abstract int onDamageTaken(int rawDamage, Character owner, Character attacker);

    public String getName() {
        return name;
    }


}