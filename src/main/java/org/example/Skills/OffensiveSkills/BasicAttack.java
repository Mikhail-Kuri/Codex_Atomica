package org.example.Skills.OffensiveSkills;

import org.example.Skills.Scaling.DamageType;
import org.example.Skills.Scaling.ScalingType;
import org.example.Skills.Targeting.TargetType;
import org.example.core.character.Character;
import org.example.gameplay.combat.CombatEvent;
import org.example.gameplay.combat.CombatEventType;

import java.util.List;
import java.util.Set;


public class BasicAttack extends OffensiveSkill {

    private int basePower = 0;

    public BasicAttack() {
        super(
                "1",
                TargetType.ENEMY,
                List.of(ScalingType.STRENGTH, ScalingType.WEAPON),
                List.of(CombatEventType.DAMAGE_DEALT),
                Set.of(DamageType.PHYSICAL)
        );
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

