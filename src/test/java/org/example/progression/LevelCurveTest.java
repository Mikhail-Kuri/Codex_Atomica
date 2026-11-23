package org.example.progression;

import org.example.data.GameContentLoader;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/** Courbes de design/gdd/progression.md : formules X1 et X2, valeurs des tableaux. */
class LevelCurveTest {

    private final LevelCurve curve = new LevelCurve(GameContentLoader.loadTuning());

    @Test
    @DisplayName("X1 : XP pour passer au niveau suivant (tableau du document)")
    void xpToNextLevelMatchesDesignTable() {
        assertEquals(100, curve.xpToNext(1));
        assertEquals(1_118, curve.xpToNext(5));
        assertEquals(3_162, curve.xpToNext(10));
        assertEquals(8_944, curve.xpToNext(20));
        assertEquals(45_319, curve.xpToNext(59));
    }

    @Test
    @DisplayName("X2 : unité de récompense (tableau du document)")
    void rewardUnitMatchesDesignTable() {
        assertEquals(65, curve.rewardUnit(1));
        assertEquals(818, curve.rewardUnit(10));
        assertEquals(1_754, curve.rewardUnit(20));
        assertEquals(5_766, curve.rewardUnit(59));
    }

    @Test
    @DisplayName("Total de 1 à 60 : 1 092 274 XP")
    void totalXpToMaxLevel() {
        long total = 0;
        for (int level = 1; level < curve.maxLevel(); level++) {
            total += curve.xpToNext(level);
        }

        assertEquals(60, curve.maxLevel());
        assertEquals(1_092_274, total);
    }
}
