package org.example.progression;

import org.example.data.EnemyRank;

/**
 * XP rapportée par un ennemi vaincu : formules X4 et X6 de design/gdd/progression.md
 * (le bonus de style viendra plus tard).
 */
public class CombatXp {

    private final ProgressionTuning tuning;
    private final LevelCurve curve;

    public CombatXp(ProgressionTuning tuning) {
        this.tuning = tuning;
        this.curve = new LevelCurve(tuning);
    }

    /** Niveau sous lequel un ennemi est « gris » et ne rapporte plus rien. */
    public int grayThreshold(int playerLevel) {
        ProgressionTuning.LevelGap gap = tuning.xp().levelGap();
        return playerLevel - (gap.grayOffset() + playerLevel / gap.grayPerLevels());
    }

    /** X4 : multiplicateur selon l'écart entre l'ennemi et le joueur. */
    public double levelGapModifier(int playerLevel, int enemyLevel) {
        ProgressionTuning.LevelGap gap = tuning.xp().levelGap();

        if (enemyLevel < grayThreshold(playerLevel)) return 0.0;

        int difference = enemyLevel - playerLevel;
        if (difference >= gap.aboveBonusFrom()) return gap.aboveBonus();
        if (difference >= -gap.fullRange()) return 1.0;

        double reduced = 1.0 - gap.lossPerLevel() * (-gap.fullRange() - difference);
        return Math.max(gap.minBeforeGray(), reduced);
    }

    /** X6 : XP pour avoir vaincu un ennemi de ce niveau et de ce rang. */
    public long forDefeating(int playerLevel, int enemyLevel, EnemyRank rank) {
        double share = tuning.xp().enemyRank().get(rank);
        double modifier = levelGapModifier(playerLevel, enemyLevel);
        return Math.round(curve.rewardUnit(enemyLevel) * share * modifier);
    }
}
