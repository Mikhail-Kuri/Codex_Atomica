package org.example.progression;

/** Courbes de niveau : XP demandée (X1) et unité de récompense (X2). */
public class LevelCurve {

    private final ProgressionTuning.Levels levels;

    public LevelCurve(ProgressionTuning tuning) {
        this.levels = tuning.levels();
    }

    /** Niveau maximum (60 par défaut). */
    public int maxLevel() {
        return levels.max();
    }

    /** X1 : XP nécessaire pour passer du niveau {@code level} au suivant. */
    public long xpToNext(int level) {
        return levels.xpCurve().at(level);
    }

    /** X2 : valeur d'une unité de récompense au niveau {@code level}. */
    public long rewardUnit(int level) {
        return levels.rewardUnit().at(level);
    }
}
