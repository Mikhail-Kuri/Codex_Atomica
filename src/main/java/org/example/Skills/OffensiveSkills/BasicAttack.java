package org.example.Skills.OffensiveSkills;

import org.example.Skills.Scaling.ScalingType;
import org.example.Skills.Targeting.TargetType;
import org.example.core.character.Character;
import org.example.gameplay.combat.CombatEvent;

import java.util.List;


public class BasicAttack extends OffensiveSkill {

    public BasicAttack() {
        super("Attaque de base", TargetType.ENEMY, List.of(ScalingType.STRENGTH, ScalingType.WEAPON));
    }


    @Override
    public List<CombatEvent> execute(Character source, Character target) {
        return resolve(source, target);
    }


}

