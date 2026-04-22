

package org.example.Skills.DefensiveSkills;

import org.example.Skills.Scaling.ScalingType;
import org.example.Skills.Targeting.TargetType;
import org.example.core.character.Character;
import org.example.gameplay.combat.CombatEvent;
import org.example.gameplay.combat.CombatEventType;

import java.util.List;

public class Counter extends DefensiveSkill {

    public Counter() {
        super("Contre",
                TargetType.SELF,
                List.of(ScalingType.VIGOR),
                List.of(CombatEventType.DEFENSE_PREPARED)
        );

    }

    @Override
    public int onDamageTaken(int rawDamage, Character owner, Character attacker) {

        int weaponPower = (owner.getEquippedWeapon() != null)
                ? owner.getEquippedWeapon().getBasePower()
                : 5;

        int counterDamage = (int) (
                owner.getAttributes().vigor +
                        (weaponPower * owner.getAttributes().resonance)
        );


        owner.getState().setPendingCounterDamage(counterDamage);

        return Math.max(0, rawDamage - (owner.getAttributes().vigor / 2));
    }

    @Override
    public List<CombatEvent> execute(Character source,List<CombatEventType> combatEventTypesList) {
        return resolve(source, combatEventTypesList);
    }

}