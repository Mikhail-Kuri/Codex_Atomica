package org.example.gameplay.combat;
import org.example.core.character.Character;

public class CombatEventFactory {

    public static CombatEvent create(CombatEventType type,
                                     Character source,
                                     Character target,
                                     int value) {

        return switch (type) {

            case DAMAGE_DEALT ->
                    new CombatEvent(type, source, target, value);

            case DAMAGE_RECEIVED ->
                    new CombatEvent(type, target, source, value);

            case SELF_DAMAGE ->
                    new CombatEvent(type, source, source, value);

            case DEFENSE_PREPARED ->
                    new CombatEvent(type, source, source, 0);

            case ATTACK_MISSED ->
                    new CombatEvent(type, source, target, 0);

            case ATTACK_BLOCKED ->
                    new CombatEvent(type, target, source, 0);

            case ENEMY_DEFEATED ->
                    new CombatEvent(type, source, target, 0);

            case ALLY_DEFEATED ->
                    new CombatEvent(type, source, target, 0);

            case TURN_STARTED ->
                    new CombatEvent(type, source, source, 0);

            case TURN_ENDED ->
                    new CombatEvent(type, source, source, 0);
        };
    }
}
