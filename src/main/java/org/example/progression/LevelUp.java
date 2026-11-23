package org.example.progression;

/** Résultat d'un gain d'XP : niveaux avant et après. */
public record LevelUp(int fromLevel, int toLevel, long xpGained) {

    public int levelsGained() {
        return toLevel - fromLevel;
    }
}
