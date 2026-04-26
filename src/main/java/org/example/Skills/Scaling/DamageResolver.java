package org.example.Skills.Scaling;

import org.example.core.character.Character;

import java.util.Set;


public class DamageResolver {

    public static int resolve(
            int baseDamage,
            Character attacker,
            Character defender,
            Set<DamageType> damageTypes
    ) {

        System.out.println("\n⚔️ === DAMAGE RESOLUTION ===");
        System.out.println("Attacker: " + attacker.getName());
        System.out.println("Defender: " + defender.getName());
        System.out.println("Base damage: " + baseDamage);
        System.out.println("Damage types: " + damageTypes);

        float multiplier = 1.0f;

        for (DamageType type : damageTypes) {
            float res = defender.getAttributes().getResistance(type);

            System.out.println("→ " + type + " resistance: " + res);

            multiplier *= res;
        }

        System.out.println("Final multiplier: " + multiplier);

        int finalDamage = Math.max(0, Math.round(baseDamage * multiplier));

        System.out.println("FINAL DAMAGE: " + finalDamage);
        System.out.println("================================\n");

        return finalDamage;
    }
}
