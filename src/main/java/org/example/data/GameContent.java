package org.example.data;

import org.example.Skills.OffensiveSkills.OffensiveSkill;
import org.example.Skills.Scaling.DamageType;
import org.example.Weapons.Weapon;
import org.example.core.character.Attributes.CharacterAttributes;
import org.example.core.character.Attributes.CharacterProfile;
import org.example.core.character.Attributes.arsenal.CharacterEquipment;
import org.example.core.character.Attributes.arsenal.CharacterSkills;
import org.example.core.character.Character;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Tout le contenu du jeu chargé depuis les données, déjà vérifié.
 * Crée les personnages et les armes ; chaque personnage reçoit ses propres
 * statistiques, son propre équipement et sa propre liste de compétences.
 * Construit par {@link GameContentLoader}.
 */
public class GameContent {

    private final Map<String, ItemDef> items;
    private final Map<String, FighterDef> classes;
    private final Map<String, FighterDef> enemies;
    private final SkillRegistry skills;
    private final Map<String, Weapon> weapons = new HashMap<>();

    GameContent(Map<String, ItemDef> items,
                Map<String, FighterDef> classes,
                Map<String, FighterDef> enemies,
                SkillRegistry skills) {
        this.items = new LinkedHashMap<>(items);
        this.classes = new LinkedHashMap<>(classes);
        this.enemies = new LinkedHashMap<>(enemies);
        this.skills = skills;
    }

    /** Identifiants des classes de héros, dans l'ordre du fichier. */
    public List<String> classIds() {
        return List.copyOf(classes.keySet());
    }

    /** Identifiants des types d'ennemis, dans l'ordre du fichier. */
    public List<String> enemyIds() {
        return List.copyOf(enemies.keySet());
    }

    /** Nom affiché d'une classe ou d'un ennemi. */
    public String nameOf(String fighterId) {
        return fighter(fighterId).name();
    }

    /** Crée un personnage neuf (niveau 1) à partir d'une classe ou d'un type d'ennemi. */
    public Character create(String fighterId) {
        FighterDef def = fighter(fighterId);

        List<OffensiveSkill> offensive = def.skills().stream()
                .map(skills::offensive)
                .toList();

        return new Character(
                profileOf(fighterId),
                attributesOf(fighterId),
                new CharacterEquipment(weapon(def.weapon())),
                new CharacterSkills(offensive, skills.defensive(def.defense())));
    }

    /** Statistiques de niveau 1, dans un objet neuf à chaque appel. */
    public CharacterAttributes attributesOf(String fighterId) {
        FighterDef def = fighter(fighterId);
        Map<Stat, Double> s = def.baseStats();

        return new CharacterAttributes(
                whole(s, Stat.VITALITY), whole(s, Stat.VIGOR), whole(s, Stat.AGILITY),
                whole(s, Stat.STRENGTH), whole(s, Stat.INTELLIGENCE), whole(s, Stat.WILLPOWER),
                s.get(Stat.RESONANCE).floatValue(), whole(s, Stat.LUCK),
                whole(s, Stat.MIN_SPEED), whole(s, Stat.MAX_SPEED),
                resistancesOf(fighterId));
    }

    /** Résistances, dans une table neuve et modifiable à chaque appel. */
    public Map<DamageType, Float> resistancesOf(String fighterId) {
        Map<DamageType, Float> res = fighter(fighterId).resistances();
        return (res == null) ? new HashMap<>() : new HashMap<>(res);
    }

    public CharacterProfile profileOf(String fighterId) {
        FighterDef def = fighter(fighterId);
        return new CharacterProfile(def.name(), def.mentalState());
    }

    /** L'arme décrite par cet objet ; la même instance pour un même id. */
    public Weapon weapon(String itemId) {
        return weapons.computeIfAbsent(itemId, id -> {
            ItemDef item = items.get(id);
            if (item == null || item.weapon() == null) {
                throw new IllegalArgumentException("Unknown weapon: " + id);
            }
            ItemDef.WeaponDef w = item.weapon();
            return new Weapon(item.name(), w.basePower(), w.damageTypes(), w.range(), null);
        });
    }

    public SkillRegistry skills() {
        return skills;
    }

    private FighterDef fighter(String id) {
        FighterDef def = classes.containsKey(id) ? classes.get(id) : enemies.get(id);
        if (def == null) throw new IllegalArgumentException("Unknown class or enemy: " + id);
        return def;
    }

    private static int whole(Map<Stat, Double> stats, Stat stat) {
        return stats.get(stat).intValue();
    }
}
