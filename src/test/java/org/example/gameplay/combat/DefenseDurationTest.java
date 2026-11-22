package org.example.gameplay.combat;

import org.example.Skills.Actions.DefensiveAction;
import org.example.app.data.GameData;
import org.example.core.character.Character;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Règle : une défense dure un seul tour. Avant, elle restait active tant que
 * le personnage n'était pas touché, et bloquait toute nouvelle défense.
 */
class DefenseDurationTest {

    @Test
    @DisplayName("Bug : une défense non utilisée restait active au tour suivant")
    void unusedDefenseEndsWithTheTurn() {
        Character paladin = GameData.createPaladin();
        Character enemy = GameData.createEnemy();
        TurnManager turnManager = new TurnManager();

        turnManager.addAction(new DefensiveAction(paladin, paladin, paladin.getCurrentDefense()));
        turnManager.resolveCurrentTurn();
        turnManager.endTurn(List.of(paladin, enemy));

        assertFalse(paladin.isDefending());
    }

    @Test
    @DisplayName("Bug : on ne pouvait plus défendre (« défend déjà ! ») au tour suivant")
    void canDefendAgainNextTurn() {
        Character paladin = GameData.createPaladin();
        Character enemy = GameData.createEnemy();
        TurnManager turnManager = new TurnManager();

        turnManager.addAction(new DefensiveAction(paladin, paladin, paladin.getCurrentDefense()));
        turnManager.resolveCurrentTurn();
        turnManager.endTurn(List.of(paladin, enemy));

        assertTrue(turnManager.addAction(
                new DefensiveAction(paladin, paladin, paladin.getCurrentDefense())));
    }
}
