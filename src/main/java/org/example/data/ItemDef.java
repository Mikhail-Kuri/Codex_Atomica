package org.example.data;

import org.example.Skills.Scaling.DamageType;
import org.example.Skills.Scaling.RangeScaling;

import java.util.Set;

/**
 * Un objet tel que décrit dans items.json.
 *
 * @param weapon présent seulement si l'objet est une arme
 */
public record ItemDef(
        String id,
        String name,
        ItemSlot slot,
        int itemLevel,
        int requiredLevel,
        Rarity rarity,
        WeaponDef weapon
) {

    /** Partie « arme » d'un objet. */
    public record WeaponDef(
            int basePower,
            Set<DamageType> damageTypes,
            RangeScaling range
    ) {}
}
