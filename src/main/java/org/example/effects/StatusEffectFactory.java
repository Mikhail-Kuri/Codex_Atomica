package org.example.effects;

import org.example.Skills.Scaling.DamageType;
import org.example.effects.types.BleedEffect;
import org.example.effects.types.BurnEffect;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class StatusEffectFactory {

    public static List<StatusEffect> fromDamageTypes(Set<DamageType> types) {

        List<StatusEffect> effects = new ArrayList<>();

        for (DamageType type : types) {

            switch (type) {

                case BLEEDING ->
                        effects.add(new BleedEffect(5, 3));
                case FIRE ->
                        effects.add(new BurnEffect(4, 2));
                default -> {
                    // pas d’effet pour ce type
                }
            }
        }

        return effects;
    }
}