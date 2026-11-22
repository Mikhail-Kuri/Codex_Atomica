package org.example.Skills.DefensiveSkills;

import org.example.Skills.Actions.DefensiveAction;
import org.example.app.data.GameData;
import org.example.core.character.Character;
import org.example.gameplay.combat.CombatEvent;
import org.example.gameplay.combat.CombatEventType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Règle : toutes les défenses émettent DEFENSE_PREPARED. Avant, Guard et Esquive
 * renvoyaient une liste vide, et seul le Contre prévenait l'état mental.
 */
class DefensePreparedEventTest {

    @Test
    @DisplayName("Bug : Guard n'émettait pas DEFENSE_PREPARED")
    void guardEmitsDefensePrepared() {
        assertTrue(emitsDefensePrepared(GameData.guard));
    }

    @Test
    @DisplayName("Bug : Esquive n'émettait pas DEFENSE_PREPARED")
    void evadeEmitsDefensePrepared() {
        assertTrue(emitsDefensePrepared(GameData.evade));
    }

    @Test
    @DisplayName("Le Contre émet toujours DEFENSE_PREPARED")
    void counterEmitsDefensePrepared() {
        assertTrue(emitsDefensePrepared(GameData.counter));
    }

    private static boolean emitsDefensePrepared(DefensiveSkill skill) {
        Character defender = GameData.createPaladin();
        List<CombatEvent> events = new DefensiveAction(defender, defender, skill).execute();
        return events.stream().anyMatch(e -> e.type() == CombatEventType.DEFENSE_PREPARED);
    }
}
