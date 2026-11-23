package org.example.Skills.DefensiveSkills;

import org.example.app.data.GameData;
import org.example.core.character.Character;
import org.example.data.GameTuning;
import org.example.testsupport.FixedRandom;
import org.example.testsupport.TestCharacters;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Esquive relative (option A) : chance = part × Agi défenseur ÷ (Agi défenseur + Agi attaquant)
 * + Chance × bonus par point, bornée entre un minimum et un maximum.
 */
class EvadeTest {

    private static final double EPSILON = 1e-9;
    private static final int RAW_DAMAGE = 50;

    private final GameTuning.Evade tuning = GameData.TUNING.combat().evade();

    @Test
    @DisplayName("À Agilité égale et sans Chance, on esquive 40 %")
    void equalAgilityGivesBaseChance() {
        Evade evade = evadeRolling(0.5);
        Character defender = TestCharacters.create(0, 10, evade);
        Character attacker = TestCharacters.create(0, 10, evade);

        assertEquals(tuning.maxShare() * 0.5, evade.dodgeChance(defender, attacker), EPSILON);
        assertEquals(0.40, evade.dodgeChance(defender, attacker), EPSILON);
    }

    @Test
    @DisplayName("Éclaireur contre Paladin (vraies données) : environ 47 %")
    void scoutAgainstPaladin() {
        Evade evade = evadeRolling(0.5);
        Character scout = GameData.CONTENT.create(GameData.SCOUT, 1);
        Character paladin = GameData.CONTENT.create(GameData.PALADIN, 1);
        int scoutAgi = scout.getAttributes().getAgility();
        int paladinAgi = paladin.getAttributes().getAgility();

        double expected = tuning.maxShare() * scoutAgi / (scoutAgi + paladinAgi)
                + tuning.luckPerPoint() * scout.getAttributes().getLuck();

        assertEquals(expected, evade.dodgeChance(scout, paladin), EPSILON);
        assertEquals(0.47, evade.dodgeChance(scout, paladin), 0.005);
    }

    @Test
    @DisplayName("La chance d'esquive ne change pas avec le niveau (les deux Agilités grandissent ensemble)")
    void chanceIsStableAcrossLevels() {
        Evade evade = evadeRolling(0.5);
        double atLevel1 = evade.dodgeChance(
                GameData.CONTENT.create(GameData.SCOUT, 1), GameData.CONTENT.create(GameData.PALADIN, 1));
        double atLevel40 = evade.dodgeChance(
                GameData.CONTENT.create(GameData.SCOUT, 40), GameData.CONTENT.create(GameData.PALADIN, 40));

        assertEquals(atLevel1, atLevel40, 0.02);
    }

    @Test
    @DisplayName("Jamais plus que le maximum, même face à un attaquant très lent")
    void chanceIsCappedAtMaximum() {
        Evade evade = evadeRolling(0.5);
        Character nimble = TestCharacters.create(0, 100, evade);
        Character clumsy = TestCharacters.create(0, 1, evade);

        assertEquals(tuning.maxChance(), evade.dodgeChance(nimble, clumsy), EPSILON);
    }

    @Test
    @DisplayName("Jamais moins que le minimum, même face à un attaquant très agile")
    void chanceHasAFloor() {
        Evade evade = evadeRolling(0.5);
        Character clumsy = TestCharacters.create(0, 1, evade);
        Character nimble = TestCharacters.create(0, 100, evade);

        assertEquals(tuning.minChance(), evade.dodgeChance(clumsy, nimble), EPSILON);
    }

    @Test
    @DisplayName("Sans attaquant connu, on compte comme à Agilité égale")
    void unknownAttackerCountsAsEqual() {
        Evade evade = evadeRolling(0.5);
        Character defender = TestCharacters.create(0, 10, evade);

        assertEquals(tuning.maxShare() * 0.5, evade.dodgeChance(defender, null), EPSILON);
    }

    @Test
    @DisplayName("Un jet sous la chance annule le coup, un jet au-dessus le laisse passer")
    void rollDecidesTheDodge() {
        Character attacker = TestCharacters.create(0, 10, GameData.guard);

        Evade lucky = evadeRolling(0.0);
        Character dodger = TestCharacters.create(0, 10, lucky);
        assertEquals(0, lucky.onDamageTaken(RAW_DAMAGE, dodger, attacker));

        Evade unlucky = evadeRolling(0.99);
        Character hit = TestCharacters.create(0, 10, unlucky);
        assertEquals(RAW_DAMAGE, unlucky.onDamageTaken(RAW_DAMAGE, hit, attacker));
    }

    private Evade evadeRolling(double roll) {
        return new Evade(tuning, new FixedRandom(roll));
    }
}
