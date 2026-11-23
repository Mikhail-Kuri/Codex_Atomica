package org.example.progression;

import org.example.data.GameTuning;

import org.example.data.EnemyRank;
import org.example.data.GameContentLoader;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/** XP de combat : formules X4 (écart de niveau, seuil gris) et X6 (sans bonus de style). */
class CombatXpTest {

    private static final int PLAYER_LEVEL = 20;
    private static final double EPSILON = 1e-9;

    private final GameTuning tuning = GameContentLoader.loadTuning();
    private final CombatXp xp = new CombatXp(tuning);
    private final LevelCurve curve = new LevelCurve(tuning);

    @Test
    @DisplayName("Seuil gris au niveau 20 : niveau 15 (tableau du document)")
    void grayThresholdMatchesDesignTable() {
        assertEquals(2, xp.grayThreshold(5));
        assertEquals(15, xp.grayThreshold(20));
        assertEquals(42, xp.grayThreshold(50));
    }

    @Test
    @DisplayName("Modificateur d'écart : 100 % proche, 60 % à −4, 40 % à −5, 0 sous le gris, 120 % au-dessus")
    void levelGapModifierFollowsFormula() {
        assertEquals(1.0, xp.levelGapModifier(PLAYER_LEVEL, 20), EPSILON);
        assertEquals(1.0, xp.levelGapModifier(PLAYER_LEVEL, 18), EPSILON);
        assertEquals(0.6, xp.levelGapModifier(PLAYER_LEVEL, 16), EPSILON);
        assertEquals(0.4, xp.levelGapModifier(PLAYER_LEVEL, 15), EPSILON);
        assertEquals(0.0, xp.levelGapModifier(PLAYER_LEVEL, 14), EPSILON);
        assertEquals(1.0, xp.levelGapModifier(PLAYER_LEVEL, 22), EPSILON);
        assertEquals(1.2, xp.levelGapModifier(PLAYER_LEVEL, 25), EPSILON);
    }

    @Test
    @DisplayName("Un ennemi normal de même niveau donne 5 % d'une unité")
    void normalEnemyAtSameLevel() {
        long expected = Math.round(curve.rewardUnit(PLAYER_LEVEL) * 0.05);

        assertEquals(expected, xp.forDefeating(PLAYER_LEVEL, PLAYER_LEVEL, EnemyRank.NORMAL));
        assertEquals(88, expected); // 1 754 × 0,05
    }

    @Test
    @DisplayName("Élite ×3 et boss ×10 par rapport à un ennemi normal")
    void eliteAndBossAreWorthMore() {
        double unit = curve.rewardUnit(PLAYER_LEVEL);

        assertEquals(Math.round(unit * 0.15), xp.forDefeating(PLAYER_LEVEL, PLAYER_LEVEL, EnemyRank.ELITE));
        assertEquals(Math.round(unit * 0.5), xp.forDefeating(PLAYER_LEVEL, PLAYER_LEVEL, EnemyRank.BOSS));
    }

    @Test
    @DisplayName("Un ennemi gris ne donne aucune XP, même un boss")
    void grayEnemyGivesNothing() {
        assertEquals(0, xp.forDefeating(PLAYER_LEVEL, 14, EnemyRank.BOSS));
    }
}
