package org.example.Skills.OffensiveSkills;


import org.example.Skills.Scaling.ScalingType;
import org.example.Skills.Targeting.TargetType;
import org.example.Weapons.Weapon;
import org.example.core.character.Character;

import java.util.List;


public class SelfAttack extends OffensiveSkill {

    public SelfAttack() {
        super("Attaque de base de soi", TargetType.SELF, List.of(ScalingType.STRENGTH));
    }



    @Override
    public void execute(Character source, Character target) {
        resolve(source, target);
    }

}

