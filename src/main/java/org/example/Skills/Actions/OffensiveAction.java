package org.example.Skills.Actions;

import org.example.Skills.OffensiveSkills.OffensiveSkill;
import org.example.core.character.Character;
import org.example.gameplay.combat.CombatEvent;
import org.example.gameplay.combat.CombatEventType;

import java.util.List;

public class OffensiveAction extends Action {

    private final OffensiveSkill skill;
    protected final int priority = 1;

    public OffensiveAction(Character source, Character target, OffensiveSkill skill) {
        super(source, target);
        this.skill = skill;
    }

    @Override
    public List<CombatEvent> execute() {

        // Aucun événement = action sans effet ; le combat l'annonce dans le journal
        if (source == null || target == null) {
            return List.of();
        }

        source.setCurrentlyUsedOffensiveSkill(skill);
        return skill.execute(source, target);
    }

    @Override
    public List<CombatEventType> getCombatEventTypesList() {
        return skill.getCombatEventTypesList();
    }


    public int getPriority() {
        return priority;
    }

    @Override
    public boolean isDefenseAction(){
        return false;
    }


}





