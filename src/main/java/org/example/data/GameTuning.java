package org.example.data;

import java.util.Map;

/**
 * Réglages globaux du jeu, lus dans tuning.json : progression (niveaux, XP)
 * et formules de combat. Les valeurs de progression renvoient aux formules
 * de design/gdd/progression.md.
 */
public record GameTuning(Levels levels, Xp xp, Combat combat) {

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

    /** Formules de combat. */
    public record Combat(Evade evade) {}

    /**
     * Esquive relative :
     * {@code chance = maxShare × agiDéfenseur ÷ (agiDéfenseur + agiAttaquant) + luckPerPoint × chance},
     * bornée entre minChance et maxChance.
     *
     * @param maxShare     chance d'esquive quand l'attaquant n'a aucune Agilité (avant bornes)
     * @param luckPerPoint bonus par point de Chance du défenseur
     * @param minChance    plancher : on n'est jamais certain de toucher
     * @param maxChance    plafond : on n'est jamais intouchable
     */
    public record Evade(double maxShare, double luckPerPoint, double minChance, double maxChance) {}
}
