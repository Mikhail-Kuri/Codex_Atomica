package org.example.progression;

import org.example.data.EnemyRank;

import java.util.Map;

/**
 * Réglages de progression lus dans tuning.json.
 * Chaque valeur renvoie à une formule de design/gdd/progression.md.
 */
public record ProgressionTuning(Levels levels, Xp xp) {

    /** Niveau maximum, courbe d'XP (X1) et unité de récompense (X2). */
    public record Levels(int max, Curve xpCurve, Curve rewardUnit) {}

    /** {@code round(base × niveau^exponent)}. */
    public record Curve(double base, double exponent) {

        public long at(int level) {
            return Math.round(base * Math.pow(level, exponent));
        }
    }

    /** XP de combat : part d'unité par rang (X6) et modificateur d'écart de niveau (X4). */
    public record Xp(Map<EnemyRank, Double> enemyRank, LevelGap levelGap) {}

    /**
     * Formule X4.
     *
     * @param grayOffset     seuil gris = niveau − (grayOffset + niveau / grayPerLevels)
     * @param fullRange      écart négatif encore payé à 100 %
     * @param lossPerLevel   perte par niveau au-delà de fullRange
     * @param minBeforeGray  plancher avant le seuil gris
     * @param aboveBonusFrom écart positif à partir duquel le bonus s'applique
     * @param aboveBonus     multiplicateur maximum contre un ennemi plus fort
     */
    public record LevelGap(int grayOffset,
                           int grayPerLevels,
                           int fullRange,
                           double lossPerLevel,
                           double minBeforeGray,
                           int aboveBonusFrom,
                           double aboveBonus) {}
}
