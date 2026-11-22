package org.example.gameplay.combat;

import org.example.Skills.Actions.DefensiveAction;
import org.example.Skills.Actions.OffensiveAction;
import org.example.app.data.GameData;
import org.example.core.character.Character;
import org.example.testsupport.TestCharacters;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/** Règle : chaque personnage a un budget d'actions par tour (1 par défaut). */
class ActionBudgetTest {

    @Test
    @DisplayName("Avec le budget par défaut, une 2e action du même personnage est refusée")
    void secondActionIsRefusedWithDefaultBudget() {
        Character paladin = GameData.createPaladin();
        Character enemy = GameData.createEnemy();
        TurnManager turnManager = new TurnManager();

        assertTrue(turnManager.addAction(attack(paladin, enemy)));
        assertFalse(turnManager.addAction(attack(paladin, enemy)));
    }

    @Test
    @DisplayName("Attaquer et défendre le même tour est refusé avec 1 action")
    void attackAndDefendSameTurnIsRefused() {
        Character paladin = GameData.createPaladin();
        Character enemy = GameData.createEnemy();
        TurnManager turnManager = new TurnManager();

        assertTrue(turnManager.addAction(attack(paladin, enemy)));
        assertFalse(turnManager.addAction(new DefensiveAction(paladin, paladin, paladin.getCurrentDefense())));
    }

    @Test
    @DisplayName("Un personnage avec 2 actions peut attaquer 2 fois, pas 3")
    void characterWithTwoActionsCanAttackTwice() {
        Character rogue = TestCharacters.withActionsPerTurn(2, GameData.evade);
        Character enemy = GameData.createEnemy();
        TurnManager turnManager = new TurnManager();

        assertTrue(turnManager.addAction(attack(rogue, enemy)));
        assertTrue(turnManager.addAction(attack(rogue, enemy)));
        assertFalse(turnManager.addAction(attack(rogue, enemy)));
    }

    @Test
    @DisplayName("Même avec 2 actions, une seule défense par tour")
    void onlyOneDefensePerTurnEvenWithExtraActions() {
        Character rogue = TestCharacters.withActionsPerTurn(2, GameData.evade);
        TurnManager turnManager = new TurnManager();

        assertTrue(turnManager.addAction(new DefensiveAction(rogue, rogue, rogue.getCurrentDefense())));
        assertFalse(turnManager.addAction(new DefensiveAction(rogue, rogue, rogue.getCurrentDefense())));
    }

    private static OffensiveAction attack(Character source, Character target) {
        return new OffensiveAction(source, target, source.getDefaultOffensiveSkill());
    }
}
