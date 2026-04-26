package org.example.Skills.OffensiveSkills;


import org.example.Skills.Scaling.ScalingType;
import org.example.Skills.Targeting.TargetType;
import org.example.Weapons.Weapon;
import org.example.core.character.Character;
import org.example.gameplay.combat.CombatEvent;
import org.example.gameplay.combat.CombatEventType;

import java.util.List;


public class SelfAttack extends OffensiveSkill {

    public SelfAttack() {
        super(
                "Attaque de base de soi",
                TargetType.ANY, List.of(ScalingType.STRENGTH),
                List.of(CombatEventType.SELF_DAMAGE,CombatEventType.DAMAGE_DEALT));
    }


    @Override
    public List<CombatEvent> execute(Character source, Character target, List<CombatEventType> combatEventTypesList) {
        return resolve(source, target, combatEventTypesList);
    }
}

