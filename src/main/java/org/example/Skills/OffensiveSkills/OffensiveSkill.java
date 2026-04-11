package org.example.Skills.OffensiveSkills;

import org.example.Skills.Targeting.TargetType;
import org.example.core.Character;


public abstract class OffensiveSkill {

    protected String name;
    protected TargetType targetType;


    public OffensiveSkill(String name, TargetType targetType) {
        this.name = name;
        this.targetType = targetType;
    }

    public abstract void execute(Character source, Character target);

    public String getName() {
        return name;
    }
}
