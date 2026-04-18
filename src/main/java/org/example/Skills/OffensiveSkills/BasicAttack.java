package org.example.Skills.OffensiveSkills;

import org.example.Skills.Scaling.ScalingType;
import org.example.Skills.Targeting.TargetType;
import org.example.core.character.Character;

import java.util.List;


public class BasicAttack extends OffensiveSkill {

    public BasicAttack() {
        super("Attaque de base", TargetType.ENEMY, List.of(ScalingType.STRENGTH, ScalingType.WEAPON));
    }


    @Override
    public void execute(Character source, Character target) {
        resolve(source, target);
    }


}

