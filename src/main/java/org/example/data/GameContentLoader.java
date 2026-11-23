package org.example.data;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.data.GameTuning;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Lit les fichiers de données (src/main/resources/data/) et vérifie tout avant de
 * construire le {@link GameContent}. Une erreur arrête le chargement avec un message
 * qui nomme le fichier et le problème, au lieu d'un bug silencieux en combat.
 */
public final class GameContentLoader {

    static final String ITEMS_FILE = "items.json";
    static final String CLASSES_FILE = "classes.json";
    static final String ENEMIES_FILE = "enemies.json";
    static final String TUNING_FILE = "tuning.json";

    private static final ObjectMapper MAPPER = new ObjectMapper()
            .enable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
            .enable(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES);

    private GameContentLoader() {}

    /** Charge les données du jeu depuis le dossier de ressources /data/. */
    public static GameContent loadDefault() {
        return fromJson(readResource(ITEMS_FILE), readResource(CLASSES_FILE),
                readResource(ENEMIES_FILE), loadTuning());
    }

    /** Charge et vérifie des données fournies directement, avec les réglages du jeu. */
    public static GameContent fromJson(String itemsJson, String classesJson, String enemiesJson) {
        return fromJson(itemsJson, classesJson, enemiesJson, loadTuning());
    }

    /** Charge et vérifie des données fournies directement (utile pour les tests). */
    public static GameContent fromJson(String itemsJson, String classesJson, String enemiesJson,
                                       GameTuning tuning) {
        SkillRegistry skills = new SkillRegistry(tuning);

        List<ItemDef> itemList = parse(ITEMS_FILE, itemsJson, new TypeReference<>() {});
        List<FighterDef> classList = parse(CLASSES_FILE, classesJson, new TypeReference<>() {});
        List<FighterDef> enemyList = parse(ENEMIES_FILE, enemiesJson, new TypeReference<>() {});

        Map<String, ItemDef> items = new LinkedHashMap<>();
        for (ItemDef item : itemList) {
            validateItem(item);
            putUnique(ITEMS_FILE, items, item.id(), item);
        }

        Map<String, FighterDef> classes = new LinkedHashMap<>();
        Map<String, FighterDef> enemies = new LinkedHashMap<>();
        Map<String, FighterDef> allFighters = new LinkedHashMap<>();

        for (FighterDef def : classList) {
            validateFighter(CLASSES_FILE, def, items, skills);
            putUnique(CLASSES_FILE, allFighters, def.id(), def);
            classes.put(def.id(), def);
        }
        for (FighterDef def : enemyList) {
            validateFighter(ENEMIES_FILE, def, items, skills);
            if (def.rank() == null) {
                throw new DataException(ENEMIES_FILE, "enemy '" + def.id() + "' has no rank");
            }
            putUnique(ENEMIES_FILE, allFighters, def.id(), def);
            enemies.put(def.id(), def);
        }

        return new GameContent(items, classes, enemies, skills);
    }

    /** Charge les réglages du jeu depuis /data/tuning.json. */
    public static GameTuning loadTuning() {
        return tuningFromJson(readResource(TUNING_FILE));
    }

    /** Lit et vérifie des réglages du jeu fournis directement. */
    public static GameTuning tuningFromJson(String json) {
        GameTuning t = parse(TUNING_FILE, json, new TypeReference<>() {});

        if (t.levels() == null || t.xp() == null) {
            throw new DataException(TUNING_FILE, "needs both 'levels' and 'xp'");
        }
        GameTuning.Levels levels = t.levels();
        if (levels.max() < 1) {
            throw new DataException(TUNING_FILE, "levels.max must be at least 1");
        }
        requireCurve("levels.xpCurve", levels.xpCurve());
        requireCurve("levels.rewardUnit", levels.rewardUnit());

        if (t.xp().enemyRank() == null) {
            throw new DataException(TUNING_FILE, "xp.enemyRank is missing");
        }
        for (EnemyRank rank : EnemyRank.values()) {
            Double share = t.xp().enemyRank().get(rank);
            if (share == null || share < 0) {
                throw new DataException(TUNING_FILE, "xp.enemyRank." + rank + " must be 0 or more");
            }
        }

        GameTuning.LevelGap gap = t.xp().levelGap();
        if (gap == null) {
            throw new DataException(TUNING_FILE, "xp.levelGap is missing");
        }
        if (gap.grayPerLevels() < 1) {
            throw new DataException(TUNING_FILE, "xp.levelGap.grayPerLevels must be at least 1");
        }
        if (gap.minBeforeGray() < 0 || gap.minBeforeGray() > 1 || gap.lossPerLevel() < 0) {
            throw new DataException(TUNING_FILE, "xp.levelGap loss values must be between 0 and 1");
        }
        if (gap.aboveBonus() < 1) {
            throw new DataException(TUNING_FILE, "xp.levelGap.aboveBonus must be at least 1");
        }

        if (t.combat() == null || t.combat().evade() == null) {
            throw new DataException(TUNING_FILE, "combat.evade is missing");
        }
        GameTuning.Evade evade = t.combat().evade();
        if (evade.maxShare() <= 0 || evade.maxShare() > 1 || evade.luckPerPoint() < 0) {
            throw new DataException(TUNING_FILE, "combat.evade.maxShare must be in (0, 1] and luckPerPoint 0 or more");
        }
        if (evade.minChance() < 0 || evade.minChance() > evade.maxChance() || evade.maxChance() > 1) {
            throw new DataException(TUNING_FILE, "combat.evade needs 0 <= minChance <= maxChance <= 1");
        }
        return t;
    }

    private static void requireCurve(String name, GameTuning.Curve curve) {
        if (curve == null || curve.base() <= 0 || curve.exponent() <= 0) {
            throw new DataException(TUNING_FILE, name + " needs a positive base and exponent");
        }
    }

    // ---------------- VALIDATION ----------------

    private static void validateItem(ItemDef item) {
        requireText(ITEMS_FILE, item.id(), "an item has no id");
        requireText(ITEMS_FILE, item.name(), "item '" + item.id() + "' has no name");
        if (item.slot() == null) {
            throw new DataException(ITEMS_FILE, "item '" + item.id() + "' has no slot");
        }
        if (item.itemLevel() < 1 || item.requiredLevel() < 1) {
            throw new DataException(ITEMS_FILE, "item '" + item.id() + "' must have levels of at least 1");
        }
        if (item.weapon() != null) {
            if (item.weapon().damageTypes() == null || item.weapon().damageTypes().isEmpty()) {
                throw new DataException(ITEMS_FILE, "weapon '" + item.id() + "' has no damage type");
            }
            if (item.weapon().range() == null) {
                throw new DataException(ITEMS_FILE, "weapon '" + item.id() + "' has no range");
            }
        }
    }

    private static void validateFighter(String file,
                                        FighterDef def,
                                        Map<String, ItemDef> items,
                                        SkillRegistry skills) {
        requireText(file, def.id(), "an entry has no id");
        String who = "'" + def.id() + "'";
        requireText(file, def.name(), who + " has no name");

        if (def.mentalState() == null) {
            throw new DataException(file, who + " has no mentalState");
        }

        if (def.baseStats() == null) {
            throw new DataException(file, who + " has no baseStats");
        }
        for (Stat stat : Stat.values()) {
            Double value = def.baseStats().get(stat);
            if (value == null) {
                throw new DataException(file, who + " is missing base stat " + stat);
            }
            if (stat.isWholeNumber() && value != Math.floor(value)) {
                throw new DataException(file, who + " base stat " + stat + " must be a whole number");
            }
        }
        if (def.baseStats().get(Stat.MIN_SPEED) > def.baseStats().get(Stat.MAX_SPEED)) {
            throw new DataException(file, who + " has MIN_SPEED greater than MAX_SPEED");
        }

        if (def.skills() == null || def.skills().isEmpty()) {
            throw new DataException(file, who + " needs at least one offensive skill");
        }
        for (String skill : def.skills()) {
            if (!skills.hasOffensive(skill)) {
                throw new DataException(file, who + " uses unknown skill '" + skill
                        + "' (known: " + skills.offensiveIds() + ")");
            }
        }
        if (!skills.hasDefensive(def.defense())) {
            throw new DataException(file, who + " uses unknown defense '" + def.defense()
                    + "' (known: " + skills.defensiveIds() + ")");
        }

        ItemDef weapon = items.get(def.weapon());
        if (weapon == null) {
            throw new DataException(file, who + " uses unknown item '" + def.weapon() + "'");
        }
        if (weapon.weapon() == null) {
            throw new DataException(file, who + " uses item '" + def.weapon() + "', which is not a weapon");
        }
    }

    private static void requireText(String file, String value, String message) {
        if (value == null || value.isBlank()) {
            throw new DataException(file, message);
        }
    }

    private static <T> void putUnique(String file, Map<String, T> map, String id, T value) {
        if (map.putIfAbsent(id, value) != null) {
            throw new DataException(file, "duplicate id '" + id + "'");
        }
    }

    // ---------------- READING ----------------

    private static <T> T parse(String file, String json, TypeReference<T> type) {
        try {
            return MAPPER.readValue(json, type);
        } catch (JsonProcessingException e) {
            throw new DataException(file, e.getOriginalMessage(), e);
        }
    }

    private static String readResource(String file) {
        try (InputStream in = GameContentLoader.class.getResourceAsStream("/data/" + file)) {
            if (in == null) {
                throw new DataException(file, "file not found in resources/data/");
            }
            return new String(in.readAllBytes(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new DataException(file, "cannot be read", e);
        }
    }
}
