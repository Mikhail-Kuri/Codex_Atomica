package org.example.data;

import org.example.Skills.Scaling.DamageType;
import org.example.core.character.Character;
import org.example.gameplay.mental.RageMentalState;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/** Chargement des données de jeu depuis le JSON, et refus clair des données invalides. */
class GameContentLoaderTest {

    private static final String ITEMS = """
            [ { "id": "test_blade", "name": "Test Blade", "slot": "MAIN_HAND",
                "itemLevel": 1, "requiredLevel": 1, "rarity": "COMMON",
                "weapon": { "basePower": 40, "damageTypes": ["SLASHING"], "range": "MELEE" } } ]
            """;

    private static final String STATS = """
            "baseStats": { "VITALITY": 90, "VIGOR": 4, "AGILITY": 6, "STRENGTH": 7,
                           "INTELLIGENCE": 3, "WILLPOWER": 2, "RESONANCE": 1.25, "LUCK": 1,
                           "MIN_SPEED": 2, "MAX_SPEED": 6 }
            """;

    private static final String CLASSES = """
            [ { "id": "test_hero", "name": "Test Hero", "mentalState": "RAGE", %s,
                "growth": { "VITALITY": 5 },
                "resistances": { "FIRE": 0.5 },
                "skills": ["attack", "radiation_strike"], "defense": "counter",
                "weapon": "test_blade" } ]
            """.formatted(STATS);

    private static final String ENEMIES = """
            [ { "id": "test_beast", "name": "Test Beast", "rank": "ELITE", "mentalState": "CALM", %s,
                "growth": {}, "resistances": {},
                "skills": ["attack"], "defense": "evade", "weapon": "test_blade" } ]
            """.formatted(STATS);

    @Test
    @DisplayName("Les vraies données du jeu se chargent et chaque personnage se crée")
    void defaultDataBuildsEveryFighter() {
        GameContent content = GameContentLoader.loadDefault();
        List<String> ids = new ArrayList<>(content.classIds());
        ids.addAll(content.enemyIds());

        assertFalse(ids.isEmpty());
        for (String id : ids) {
            Character fighter = content.create(id);
            assertTrue(fighter.isAlive(), id);
            assertFalse(fighter.getName().isBlank(), id);
            assertNotNull(fighter.getEquippedWeapon(), id);
            assertNotNull(fighter.getCurrentDefense(), id);
        }
    }

    @Test
    @DisplayName("Les valeurs du JSON arrivent telles quelles dans le personnage")
    void loadedValuesMatchJson() {
        GameContent content = GameContentLoader.fromJson(ITEMS, CLASSES, ENEMIES);

        Character hero = content.create("test_hero");

        assertEquals("Test Hero", hero.getName());
        assertEquals(90, hero.getCurrentHP());
        assertEquals(4, hero.getAttributes().vigor);
        assertEquals(1.25f, hero.getAttributes().resonance);
        assertEquals(0.5f, hero.getAttributes().getResistance(DamageType.FIRE));
        assertEquals(1.0f, hero.getAttributes().getResistance(DamageType.HOLY));
        assertInstanceOf(RageMentalState.class, hero.getMentalState());
        assertEquals(2, hero.getOffensiveSkills().size());
        assertEquals("Counter", hero.getCurrentDefense().getName());
        assertEquals(40, hero.getEquippedWeapon().getBasePower());
        assertEquals("Test Blade", hero.getEquippedWeapon().getName());
    }

    @Test
    @DisplayName("Chaque personnage créé a ses propres statistiques")
    void eachCharacterGetsItsOwnAttributes() {
        GameContent content = GameContentLoader.fromJson(ITEMS, CLASSES, ENEMIES);

        assertNotSame(content.create("test_hero").getAttributes(),
                content.create("test_hero").getAttributes());
    }

    @Test
    @DisplayName("Une compétence inconnue est refusée avec un message clair")
    void unknownSkillIsRejected() {
        String classes = CLASSES.replace("\"radiation_strike\"", "\"radiation_strik\"");

        DataException error = assertThrows(DataException.class,
                () -> GameContentLoader.fromJson(ITEMS, classes, ENEMIES));

        assertTrue(error.getMessage().contains("classes.json"), error.getMessage());
        assertTrue(error.getMessage().contains("radiation_strik"), error.getMessage());
    }

    @Test
    @DisplayName("Une arme inconnue est refusée")
    void unknownWeaponIsRejected() {
        String enemies = ENEMIES.replace("\"weapon\": \"test_blade\"", "\"weapon\": \"no_such_item\"");

        DataException error = assertThrows(DataException.class,
                () -> GameContentLoader.fromJson(ITEMS, CLASSES, enemies));

        assertTrue(error.getMessage().contains("no_such_item"), error.getMessage());
    }

    @Test
    @DisplayName("Un champ mal écrit est refusé au lieu d'être ignoré")
    void misspelledFieldIsRejected() {
        String classes = CLASSES.replace("\"defense\"", "\"defence\"");

        DataException error = assertThrows(DataException.class,
                () -> GameContentLoader.fromJson(ITEMS, classes, ENEMIES));

        assertTrue(error.getMessage().contains("classes.json"), error.getMessage());
    }

    @Test
    @DisplayName("Une statistique de base manquante est refusée")
    void missingBaseStatIsRejected() {
        String classes = CLASSES.replace("\"LUCK\": 1,", "");

        DataException error = assertThrows(DataException.class,
                () -> GameContentLoader.fromJson(ITEMS, classes, ENEMIES));

        assertTrue(error.getMessage().contains("LUCK"), error.getMessage());
    }

    @Test
    @DisplayName("Un identifiant en double est refusé")
    void duplicateIdIsRejected() {
        String enemies = ENEMIES.replace("\"test_beast\"", "\"test_hero\"");

        DataException error = assertThrows(DataException.class,
                () -> GameContentLoader.fromJson(ITEMS, CLASSES, enemies));

        assertTrue(error.getMessage().contains("test_hero"), error.getMessage());
    }
}
