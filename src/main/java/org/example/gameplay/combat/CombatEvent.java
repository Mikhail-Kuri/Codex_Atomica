package org.example.gameplay.combat;

import org.example.Skills.Scaling.DamageType;
import org.example.core.character.Character;

import java.util.Set;


/**
 * @param value dégâts, heal, sanity, etc.
 */
public record CombatEvent(CombatEventType type, Character source, Character target,Set<DamageType> damageTypes,int value) {
}