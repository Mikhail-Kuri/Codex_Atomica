package org.example.gameplay.combat;

import org.example.Skills.Actions.DefensiveAction;
import org.example.Skills.Actions.OffensiveAction;
import org.example.app.data.GameData;
import org.example.core.character.Character;
import org.example.testsupport.TestCharacters;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Règle : l'attaquant ne gagne sa ressource mentale que si le coup passe.
 * Gain complet sans défense, moitié si une défense a réduit le coup, rien s'il est annulé.
 * Avant, le gain était donné avant même de calculer les dégâts.
 */
class AttackerMentalGainTest {

    /** Vigueur de la cible : assez pour que Guard (2 × Vigueur) réduise vraiment le coup. */
    private static final int TARGET_VIGOR = 10;

    @Test
    @DisplayName("Un coup qui passe sans défense donne le gain complet")
    void undefendedHitGivesFullGain() {
        assertTrue(gainAgainst(TestCharacters.create(TARGET_VIGOR, 0, GameData.guard), false) > 0);
    }

    @Test
    @DisplayName("Un coup réduit par Guard donne la moitié du gain")
    void guardedHitGivesHalfGain() {
        int full = gainAgainst(TestCharacters.create(TARGET_VIGOR, 0, GameData.guard), false);
        int half = gainAgainst(TestCharacters.create(TARGET_VIGOR, 0, GameData.guard), true);

        assertEquals(full / 2, half);
    }

    @Test
    @DisplayName("Bug : un coup esquivé rapportait quand même sa ressource à l'attaquant")
    void dodgedHitGivesNoGain() {
        Character dodger = TestCharacters.create(0, TestCharacters.ALWAYS_DODGE_AGILITY, GameData.evade);

        assertEquals(0, gainAgainst(dodger, true));
    }

    /** Fait attaquer un Paladin neuf sur la cible ; renvoie le gain mental de l'attaquant. */
    private static int gainAgainst(Character target, boolean targetDefends) {
        Character attacker = GameData.createPaladin();
        int before = attacker.getMentalState().getValue();
        TurnManager turnManager = new TurnManager();

        if (targetDefends) {
            turnManager.addAction(new DefensiveAction(target, target, target.getCurrentDefense()));
        }
        turnManager.addAction(new OffensiveAction(attacker, target, attacker.getDefaultOffensiveSkill()));
        turnManager.resolveCurrentTurn();

        return attacker.getMentalState().getValue() - before;
    }
}
