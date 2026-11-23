package org.example.progression;

import org.example.core.character.Character;
import org.example.data.GameContent;
import org.example.data.GameContentLoader;
import org.example.data.Stat;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

/** Gain d'XP et montée de niveau : règles 1 à 4 et formules S1, S2, X1. */
class ProgressionTest {

    private static final String ITEMS = """
            [ { "id": "blade", "name": "Blade", "slot": "MAIN_HAND", "itemLevel": 1,
                "requiredLevel": 1, "rarity": "COMMON",
                "weapon": { "basePower": 10, "damageTypes": ["PHYSICAL"], "range": "MELEE" } } ]
            """;

    /** Croissance volontairement non nulle : c'est ce qui est testé. */
    private static final String CLASSES = """
            [ { "id": "grower", "name": "Grower", "mentalState": "CALM",
                "baseStats": { "VITALITY": 100, "VIGOR": 4, "AGILITY": 5, "STRENGTH": 5,
                               "INTELLIGENCE": 5, "WILLPOWER": 5, "RESONANCE": 1.0, "LUCK": 5,
                               "MIN_SPEED": 3, "MAX_SPEED": 6 },
                "growth": { "VITALITY": 10, "VIGOR": 0.5, "RESONANCE": 0.02 },
                "resistances": {}, "skills": ["attack"], "defense": "guard", "weapon": "blade" },
              { "id": "static", "name": "Static", "mentalState": "CALM",
                "baseStats": { "VITALITY": 100, "VIGOR": 4, "AGILITY": 5, "STRENGTH": 5,
                               "INTELLIGENCE": 5, "WILLPOWER": 5, "RESONANCE": 1.0, "LUCK": 5,
                               "MIN_SPEED": 3, "MAX_SPEED": 6 },
                "growth": {},
                "resistances": {}, "skills": ["attack"], "defense": "guard", "weapon": "blade" } ]
            """;

    private final ProgressionTuning tuning = GameContentLoader.loadTuning();
    private final LevelCurve curve = new LevelCurve(tuning);
    private final Progression progression = new Progression(tuning);
    private final GameContent content = GameContentLoader.fromJson(ITEMS, CLASSES, "[]");

    @Test
    @DisplayName("S1 : stats = base + croissance × (niveau − 1), arrondi vers le bas pour les entiers")
    void statsFollowLinearGrowth() {
        Character level10 = content.create("grower", 10);

        assertEquals(10, level10.getLevel());
        assertEquals(190, level10.getAttributes().getVitality()); // 100 + 10 × 9
        assertEquals(8, level10.getAttributes().getVigor());      // 4 + floor(0,5 × 9)
        assertEquals(1.18f, level10.getAttributes().getResonance(), 1e-5f); // 1 + 0,02 × 9
        assertEquals(190, level10.getCurrentHP());
    }

    @Test
    @DisplayName("Assez d'XP fait monter d'un niveau et recalcule les stats")
    void enoughXpLevelsUp() {
        Character hero = content.create("grower", 1);

        LevelUp result = progression.grantXp(hero, curve.xpToNext(1));

        assertEquals(2, hero.getLevel());
        assertEquals(0, hero.getExperience());
        assertEquals(1, result.levelsGained());
        assertEquals(110, hero.getAttributes().getVitality());
    }

    @Test
    @DisplayName("Une grosse récompense fait passer plusieurs niveaux et garde le reste")
    void bigRewardGivesSeveralLevelsAndKeepsRemainder() {
        Character hero = content.create("grower", 1);
        long leftover = 7;
        long reward = curve.xpToNext(1) + curve.xpToNext(2) + curve.xpToNext(3) + leftover;

        LevelUp result = progression.grantXp(hero, reward);

        assertEquals(4, hero.getLevel());
        assertEquals(leftover, hero.getExperience());
        assertEquals(3, result.levelsGained());
    }

    @Test
    @DisplayName("S2 : monter de niveau garde la même proportion de PV")
    void levelUpKeepsHpProportion() {
        Character hero = content.create("grower", 1);   // 100 PV max
        hero.getState().setCurrentHP(50);                // 50 %

        progression.grantXp(hero, curve.xpToNext(1));    // 110 PV max

        assertEquals(55, hero.getCurrentHP());
    }

    @Test
    @DisplayName("Au niveau 60, l'XP gagnée ne change plus rien")
    void maxLevelIgnoresXp() {
        Character hero = content.create("grower", curve.maxLevel());

        LevelUp result = progression.grantXp(hero, 1_000_000);

        assertEquals(curve.maxLevel(), hero.getLevel());
        assertEquals(0, hero.getExperience());
        assertEquals(0, result.levelsGained());
    }

    @Test
    @DisplayName("Arriver au niveau 60 remet l'XP à 0 (pas de surplus stocké)")
    void reachingMaxLevelDropsSurplus() {
        Character hero = content.create("grower", curve.maxLevel() - 1);

        progression.grantXp(hero, curve.xpToNext(curve.maxLevel() - 1) + 500);

        assertEquals(curve.maxLevel(), hero.getLevel());
        assertEquals(0, hero.getExperience());
    }

    @Test
    @DisplayName("Sans croissance dans les données, monter de niveau ne change pas les stats")
    void noGrowthMeansSameStats() {
        Character fixed = content.create("static", 1);
        int vitality = fixed.getAttributes().getVitality();

        progression.grantXp(fixed, curve.xpToNext(1));

        assertEquals(2, fixed.getLevel());
        assertEquals(vitality, fixed.getAttributes().getVitality());
    }

    @Test
    @DisplayName("StatProfile : une statistique absente de la croissance ne grandit pas")
    void missingGrowthMeansZero() {
        StatProfile profile = new StatProfile(
                Map.of(Stat.LUCK, 5.0), Map.of());

        assertEquals(5.0, profile.valueAt(Stat.LUCK, 30), 1e-9);
    }
}
