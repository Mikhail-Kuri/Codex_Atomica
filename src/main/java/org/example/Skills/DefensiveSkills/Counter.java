

package org.example.Skills.DefensiveSkills;

import org.example.core.character.Character;
import org.example.gameplay.combat.CombatEvent;

import java.util.List;

public class Counter extends DefensiveSkill {

    public Counter() {
        super("Contre");
    }

    public Counter(String name) {
        super(name);
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

        // 🔥 appliquer le vrai contre
        if (attacker != null && attacker.isAlive()) {
            attacker.takeDamage(counterDamage, owner);
        }

        return Math.max(0, rawDamage - (owner.getAttributes().vigor / 2));
    }

    @Override
    public List<CombatEvent> execute(Character source, Character target) {
        return resolve(source, target);
    }
}