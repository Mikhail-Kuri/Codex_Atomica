package org.example.app.data;

import org.example.Skills.DefensiveSkills.DefensiveSkill;
import org.example.Skills.OffensiveSkills.OffensiveSkill;
import org.example.Skills.Scaling.DamageType;
import org.example.Weapons.Weapon;
import org.example.core.character.Attributes.CharacterAttributes;
import org.example.core.character.Attributes.CharacterProfile;
import org.example.core.character.Character;
import org.example.data.GameContent;
import org.example.data.GameContentLoader;
import org.example.progression.ProgressionTuning;

import java.util.Map;

/**
 * Accès rapide au contenu du jeu, chargé depuis src/main/resources/data/*.json.
 * Les valeurs ne sont plus écrites ici : modifier un personnage, une arme ou une
 * résistance se fait dans les fichiers JSON. Cette classe garde ses anciens noms
 * pour que les GameTester et les tests existants continuent de fonctionner.
 */
public class GameData {

    /** Tout le contenu du jeu, chargé et vérifié au démarrage. */
    public static final GameContent CONTENT = GameContentLoader.loadDefault();

    /** Réglages de progression (niveaux, XP), lus dans tuning.json. */
    public static final ProgressionTuning TUNING = GameContentLoader.loadTuning();

    // Identifiants dans les fichiers de données
    public static final String PALADIN = "core_paladin";
    public static final String SCOUT = "mutant_scout";
    public static final String MUTANT = "electric_mutant";
    public static final String GNOLL = "irradiated_gnoll";
    public static final String RAT = "mutant_rat";

    // =====================
    // WEAPONS
    // =====================

    public static final Weapon SWORD = CONTENT.weapon("iron_sword");
    public static final Weapon RUSTY_SWORD = CONTENT.weapon("rusty_iron_sword");
    public static final Weapon PLASMA = CONTENT.weapon("plasma_rifle");

    // =====================
    // SKILLS
    // =====================

    public static final OffensiveSkill basicAttack = CONTENT.skills().offensive("attack");
    public static final OffensiveSkill radStrike = CONTENT.skills().offensive("radiation_strike");
    public static final OffensiveSkill selfAttack = CONTENT.skills().offensive("sacrificial_strike");
    public static final DefensiveSkill guard = CONTENT.skills().defensive("guard");
    public static final DefensiveSkill evade = CONTENT.skills().defensive("evade");
    public static final DefensiveSkill counter = CONTENT.skills().defensive("counter");

    // =====================
    // STATS & PROFILES (utilisés par les tests)
    // =====================

    public static final CharacterAttributes PALADIN_STATS = CONTENT.attributesOf(PALADIN);
    public static final CharacterAttributes ENEMY_STATS = CONTENT.attributesOf(GNOLL);
    public static final Map<DamageType, Float> resistancesEnemy = CONTENT.resistancesOf(GNOLL);
    public static final CharacterProfile paladinProfile = CONTENT.profileOf(PALADIN);
    public static final CharacterProfile enemyProfile = CONTENT.profileOf(GNOLL);

    // =====================
    // FACTORY METHODS
    // =====================
    // Chaque appel crée un personnage neuf, avec ses propres stats et son équipement.

    public static Character createPaladin() {
        return CONTENT.create(PALADIN);
    }

    public static Character createEnemy() {
        return CONTENT.create(GNOLL);
    }

    public static Character createScout() {
        return CONTENT.create(SCOUT);
    }

    public static Character createMutant() {
        return CONTENT.create(MUTANT);
    }

    public static Character createFragile() {
        return CONTENT.create(RAT);
    }
}
