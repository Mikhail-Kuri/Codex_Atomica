package org.example.gameplay.combat;

import org.example.Skills.Actions.DefensiveAction;
import org.example.Skills.Actions.OffensiveAction;
import org.example.Skills.DefensiveSkills.Counter;
import org.example.Skills.Scaling.DamageResolver;
import org.example.app.data.GameData;
import org.example.core.character.Character;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Régression : triggerCounter ajoutait un DAMAGE_DEALT (qui crée déjà son
 * DAMAGE_RECEIVED) ET un DAMAGE_RECEIVED de plus, donc le Contre frappait deux fois.
 */
class CounterTest {

    @Test
    @DisplayName("Bug : le Contre infligeait ses dégâts deux fois")
    void counterHitsAttackerExactlyOnce() {
        Character paladin = GameData.createPaladin();
        Character mutant = GameData.createMutant(); // défense : Contre
        int paladinStartHp = paladin.getCurrentHP();

        TurnManager turnManager = new TurnManager();
        // La défense a la priorité 100 : elle passe toujours avant l'attaque
        turnManager.addAction(new DefensiveAction(mutant, mutant, mutant.getCurrentDefense()));
        turnManager.addAction(new OffensiveAction(paladin, mutant, paladin.getDefaultOffensiveSkill()));
        turnManager.resolveCurrentTurn();

        int actualLoss = paladinStartHp - paladin.getCurrentHP();

        assertEquals(expectedSingleCounterHit(paladin), actualLoss);
    }

    /** Dégâts d'UN coup de Contre, calculés avec les formules du jeu sur des personnages neufs. */
    private static int expectedSingleCounterHit(Character usedAttacker) {
        Character freshMutant = GameData.createMutant();
        Character freshPaladin = GameData.createPaladin();

        new Counter().onDamageTaken(0, freshMutant, null);
        int counterDamage = freshMutant.getState().getPendingCounterDamage();

        int afterResistances = DamageResolver.resolve(
                counterDamage,
                freshMutant,
                freshPaladin,
                usedAttacker.getDefaultOffensiveSkill().getDamageTypes());

        int hpBefore = freshPaladin.getCurrentHP();
        freshPaladin.takeDamage(afterResistances, freshMutant);
        return hpBefore - freshPaladin.getCurrentHP();
    }
}
