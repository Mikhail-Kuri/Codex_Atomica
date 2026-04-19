package org.example.Skills.Actions;

import org.example.Skills.DefensiveSkills.DefensiveSkill;
import org.example.Skills.OffensiveSkills.OffensiveSkill;
import org.example.core.character.Character;
import org.example.gameplay.combat.CombatEvent;

import java.util.List;

public class DefensiveAction extends Action {

    protected final int priority = 100;
    private final DefensiveSkill skill;


    public DefensiveAction(Character source, Character target,DefensiveSkill skill1) {
        super(source, target);
        this.skill = skill1;
    }

    @Override
    public List<CombatEvent> execute() {

        source.prepareDefense();
        return skill.execute(source, target);
    }

    public int getPriority() {
        return priority;
    }
}
