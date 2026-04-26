package org.example.gameplay.combat;

import org.example.Skills.Scaling.DamageType;
import org.example.core.character.Character;

import java.util.Set;

public class CombatEventFactory {

    public static CombatEvent create(CombatEventType type,
                                     Character source,
                                     Character target,
                                     Set<DamageType> damageTypes,
                                     int value) {

        return switch (type) {

            case DAMAGE_DEALT -> new CombatEvent(type, source, target, damageTypes, value);

            case DAMAGE_RECEIVED -> new CombatEvent(type, target, source, damageTypes, value);

            case SELF_DAMAGE -> new CombatEvent(type, source, source, damageTypes, value);

            case DEFENSE_PREPARED, TURN_STARTED, TURN_ENDED -> new CombatEvent(type, source, source, damageTypes, 0);

            case ATTACK_MISSED, ENEMY_DEFEATED, ALLY_DEFEATED -> new CombatEvent(type, source, target, damageTypes, 0);

            case ATTACK_BLOCKED -> new CombatEvent(type, target, source, damageTypes, 0);
        };
    }
}
