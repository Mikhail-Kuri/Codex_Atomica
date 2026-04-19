package org.example.Skills.OffensiveSkills;


import org.example.Skills.Scaling.ScalingType;
import org.example.Skills.Targeting.TargetType;
import org.example.Weapons.Weapon;
import org.example.core.character.Character;
import org.example.gameplay.combat.CombatEvent;

import java.util.List;


public class SelfAttack extends OffensiveSkill {

    public SelfAttack() {
        super("Attaque de base de soi", TargetType.SELF, List.of(ScalingType.STRENGTH));
    }


    @Override
    public List<CombatEvent> execute(Character source, Character target) {
        return resolve(source, target);
    }

}

