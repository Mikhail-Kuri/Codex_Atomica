package org.example.progression;

import org.example.core.character.Character;

/**
 * Donne de l'XP à un personnage et le fait monter de niveau (règles 1 à 4 de la progression).
 * Plusieurs niveaux d'un coup sont possibles ; le reste d'XP est conservé ;
 * au niveau maximum, l'XP n'est plus comptée.
 */
public class Progression {

    private final LevelCurve curve;

    public Progression(ProgressionTuning tuning) {
        this.curve = new LevelCurve(tuning);
    }

    public LevelUp grantXp(Character character, long amount) {
        int from = character.getLevel();
        int level = from;

        if (level >= curve.maxLevel() || amount <= 0) {
            return new LevelUp(from, from, 0);
        }

        long xp = character.getExperience() + amount;
        while (level < curve.maxLevel() && xp >= curve.xpToNext(level)) {
            xp -= curve.xpToNext(level);
            level++;
        }
        if (level >= curve.maxLevel()) {
            xp = 0;
        }

        character.setExperience(xp);
        if (level != from) {
            character.applyLevel(level);
        }
        return new LevelUp(from, level, amount);
    }

    /** XP encore nécessaire pour atteindre le niveau suivant (0 au niveau maximum). */
    public long xpToNextLevel(Character character) {
        if (character.getLevel() >= curve.maxLevel()) return 0;
        return curve.xpToNext(character.getLevel()) - character.getExperience();
    }
}
