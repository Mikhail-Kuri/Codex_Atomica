package org.example.Skills.Scaling;

import org.example.core.character.Character;
import org.example.gameplay.log.CombatLog;

import java.util.Set;


public class DamageResolver {

    /** Applique les résistances du défenseur, sans rien journaliser. */
    public static int resolve(
            int baseDamage,
            Character attacker,
            Character defender,
            Set<DamageType> damageTypes
    ) {
        return resolve(baseDamage, attacker, defender, damageTypes, CombatLog.SILENT);
    }

    /**
     * Applique les résistances du défenseur à chaque type de dégât.
     * Le détail du calcul est écrit dans le journal en debug.
     */
    public static int resolve(
            int baseDamage,
            Character attacker,
            Character defender,
            Set<DamageType> damageTypes,
            CombatLog log
    ) {

        float multiplier = 1.0f;

        for (DamageType type : damageTypes) {
            float res = defender.getAttributes().getResistance(type);
            log.debug(defender.getName() + " : résistance " + type + " × " + res);
            multiplier *= res;
        }

        int finalDamage = Math.max(0, Math.round(baseDamage * multiplier));

        log.debug(attacker.getName() + " → " + defender.getName() + " : "
                + baseDamage + " de base × " + multiplier + " = " + finalDamage);

        return finalDamage;
    }
}
