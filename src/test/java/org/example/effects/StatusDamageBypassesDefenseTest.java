package org.example.effects;

import org.example.app.data.GameData;
import org.example.core.character.Character;
import org.example.effects.types.BleedEffect;
import org.example.effects.types.BurnEffect;
import org.example.testsupport.TestCharacters;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Règle : les dégâts de fin de tour d'un effet ne passent pas par la défense.
 * Avant, une Esquive pouvait esquiver sa propre brûlure, et la brûlure
 * consommait la défense à la place du vrai coup.
 */
class StatusDamageBypassesDefenseTest {

    private static final int DAMAGE_PER_TURN = 4;
    private static final int DURATION = 2;

    @Test
    @DisplayName("Bug : une Esquive pouvait esquiver sa propre brûlure")
    void burnIsNotDodged() {
        Character dodger = TestCharacters.create(0, TestCharacters.ALWAYS_DODGE_AGILITY, GameData.evade);
        dodger.prepareDefense();
        int hpBefore = dodger.getCurrentHP();

        new BurnEffect(DAMAGE_PER_TURN, DURATION).onTurnEnd(dodger);

        assertEquals(hpBefore - DAMAGE_PER_TURN, dodger.getCurrentHP());
    }

    @Test
    @DisplayName("Bug : le saignement consommait la défense active")
    void bleedDoesNotConsumeDefense() {
        Character guard = TestCharacters.create(0, 0, GameData.guard);
        guard.prepareDefense();

        new BleedEffect(DAMAGE_PER_TURN, DURATION).onTurnEnd(guard);

        assertTrue(guard.isDefending());
    }
}
