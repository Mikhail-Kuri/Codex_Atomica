package org.example.Skills.DefensiveSkills;

import org.example.Skills.Scaling.ScalingType;
import org.example.Skills.Targeting.TargetType;
import org.example.core.character.Character;
import org.example.gameplay.combat.CombatEvent;
import org.example.gameplay.combat.CombatEventType;

import java.util.List;

public class Guard extends DefensiveSkill {

    public Guard() {
        super(
                "Guard",
                TargetType.SELF,
                List.of(ScalingType.VIGOR),
                List.of(CombatEventType.DEFENSE_PREPARED));
    }

    @Override
    public int onDamageTaken(int rawDamage, Character owner, Character attacker) {

        int effectiveVigor = owner.getAttributes().vigor * 2;

        int finalDamage = Math.max(0, rawDamage - effectiveVigor);

        System.out.println("[ACTION: GARDE] " + owner.getName() + " s'ancre au sol !");
        System.out.println("Vigueur effective : " + effectiveVigor +
                " | Dégâts absorbés : " + (rawDamage - finalDamage));

        return finalDamage;
    }

    @Override
    public List<CombatEvent> execute(Character source, List<CombatEventType> combatEventTypesList) {
        return resolve(source, combatEventTypesList);
    }

    @Override
    public List<CombatEventType> getCombatEventTypesList() {
        return List.of();
    }

}
