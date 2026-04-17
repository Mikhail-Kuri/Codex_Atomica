package org.example.Skills.Actions;

import org.example.Skills.OffensiveSkills.OffensiveSkill;
import org.example.core.character.Character;

public class OffensiveAction extends Action {

    private final OffensiveSkill skill;

    public OffensiveAction(Character source, Character target, OffensiveSkill skill) {
        super(source, target);
        this.skill = skill;
    }

    @Override
    public void execute() {
        if (source == null || target == null){
            System.out.println("Source ou cible est null. Action annulée.");
            return;
        }
        skill.execute(source, target);

    }
}





