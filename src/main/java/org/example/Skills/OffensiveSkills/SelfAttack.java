package org.example.Skills.OffensiveSkills;


import org.example.Skills.Scaling.DamageType;
import org.example.Skills.Scaling.ScalingType;
import org.example.Skills.Targeting.TargetType;
import org.example.Weapons.Weapon;
import org.example.core.character.Character;
import org.example.gameplay.combat.CombatEvent;
import org.example.gameplay.combat.CombatEventType;

import java.util.List;
import java.util.Set;


public class SelfAttack extends OffensiveSkill {

    private int basePower = 0;

    public SelfAttack() {
        super(
                "3",
                TargetType.ANY, List.of(ScalingType.STRENGTH),
                List.of(CombatEventType.SELF_DAMAGE, CombatEventType.DAMAGE_DEALT),
                Set.of(DamageType.PHYSICAL));
    }


    @Override
    public List<CombatEvent> execute(Character source, Character target) {
        return resolve(source, target);
    }

    @Override
    public int getBasePower() {
        return basePower;
    }

    public void setBasePower(int basePower) {
        this.basePower = basePower;
    }
}

