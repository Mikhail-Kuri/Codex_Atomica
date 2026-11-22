package org.example.gameplay.ai;

import org.example.Skills.Actions.Action;
import org.example.Skills.Actions.OffensiveAction;
import org.example.app.data.GameData;
import org.example.core.character.Character;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/** IA simple : défend avec une probabilité donnée, sinon utilise une de ses compétences. */
class SimpleAiChooserTest {

    private static final long SEED = 42L;
    private static final int SAMPLES = 50;

    @Test
    @DisplayName("Avec 0 % de défense, l'IA attaque toujours")
    void neverDefendsWithZeroChance() {
        Character mutant = GameData.createMutant();
        Character paladin = GameData.createPaladin();
        SimpleAiChooser ai = new SimpleAiChooser(0.0, new Random(SEED));

        for (int i = 0; i < SAMPLES; i++) {
            assertFalse(ai.choose(mutant, paladin).isDefenseAction());
        }
    }

    @Test
    @DisplayName("Avec 100 % de défense, l'IA défend toujours")
    void alwaysDefendsWithFullChance() {
        Character mutant = GameData.createMutant();
        Character paladin = GameData.createPaladin();
        SimpleAiChooser ai = new SimpleAiChooser(1.0, new Random(SEED));

        for (int i = 0; i < SAMPLES; i++) {
            assertTrue(ai.choose(mutant, paladin).isDefenseAction());
        }
    }

    @Test
    @DisplayName("L'IA n'utilise que ses propres compétences, contre son adversaire")
    void usesOnlyItsOwnSkillsAgainstOpponent() {
        Character mutant = GameData.createMutant();
        Character paladin = GameData.createPaladin();
        SimpleAiChooser ai = new SimpleAiChooser(0.0, new Random(SEED));

        for (int i = 0; i < SAMPLES; i++) {
            Action action = ai.choose(mutant, paladin);
            OffensiveAction attack = (OffensiveAction) action;
            assertTrue(mutant.getOffensiveSkills().contains(attack.getSkill()));
            assertTrue(attack.getTarget() == paladin);
        }
    }
}
