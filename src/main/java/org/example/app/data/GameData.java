package org.example.app.data;

import org.example.Skills.OffensiveSkills.BasicAttack;
import org.example.Skills.OffensiveSkills.OffensiveSkill;
import org.example.Skills.OffensiveSkills.RadiationStrike;
import org.example.Skills.OffensiveSkills.SelfAttack;
import org.example.Skills.Scaling.DamageType;
import org.example.Skills.Scaling.RangeScaling;
import org.example.core.character.Attributes.CharacterAttributes;
import org.example.Weapons.*;
import org.example.Skills.DefensiveSkills.*;
import org.example.core.character.Character;
import org.example.core.character.Attributes.arsenal.CharacterEquipment;
import org.example.core.character.Attributes.arsenal.CharacterSkills;
import org.example.core.character.Attributes.CharacterProfile;
import org.example.gameplay.mental.*;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class GameData {

    public static Map<DamageType, Float> resistancesPaladin = Map.of(
            DamageType.HOLY, 0.2f,
            DamageType.PHYSICAL, 0.7f,
            DamageType.NECROTIC, 1.5f,
            DamageType.VOID, 1.3f
    );

    public static Map<DamageType, Float> resistancesEnemy = Map.of(
            DamageType.PHYSICAL, 0.8f,
            DamageType.SLASHING, 0.9f,
            DamageType.RADIATION, 1.2f
    );

    public static Map<DamageType, Float> resistancesScout = Map.of(
            DamageType.PHYSICAL, 1.2f,
            DamageType.SLASHING, 1.2f,
            DamageType.RADIATION, 1.0f
    );

    public static Map<DamageType, Float> resistancesMutant = Map.of(
            DamageType.PHYSICAL, 0.7f,
            DamageType.SLASHING, 0.9f,
            DamageType.RADIATION, 0.5f
    );

    public static Map<DamageType, Float> resistancesFragileEnemy = Map.of(
            DamageType.PHYSICAL, 1.5f,
            DamageType.SLASHING, 1.8f,
            DamageType.RADIATION, 2.0f
    );


    public static final CharacterAttributes PALADIN_STATS =
            new CharacterAttributes(120, 0, 10, 5, 15, 10, 1.5f,
                    5, 3, 8,resistancesPaladin);

    public static final CharacterAttributes ENEMY_STATS =
            new CharacterAttributes(50, 0, 2, 5, 2, 2, 1, 0,
                    3, 5,resistancesEnemy);

    public static final CharacterAttributes SCOUT_STATS =
            new CharacterAttributes(80, 0, 15, 5, 5, 8, 1.0f, 10,
                    7, 10,resistancesScout);

    public static final CharacterAttributes MUTANT_STATS =
            new CharacterAttributes(100, 0, 8, 5, 10, 10, 2.0f, 5,
                    1, 11,resistancesMutant);

    public static final CharacterAttributes FRAGILE_ENEMY_STATS =
            new CharacterAttributes(30, 0, 1, 5, 1, 1, 0.5f, 0,
                    2, 5,resistancesFragileEnemy);

    // =====================
    // WEAPONS
    // =====================

    public static final Weapon SWORD =
            new Weapon("Épée en fer", 35, Set.of(DamageType.PHYSICAL), RangeScaling.MELEE, null);

    public static final Weapon RUSTY_SWORD =
            new Weapon("Épée en Fer-Rouille", 25, Set.of(DamageType.SLASHING), RangeScaling.MELEE, null);

    public static final Weapon PLASMA =
            new Weapon("Fusil à Plasma", 60, Set.of(DamageType.ENERGY), RangeScaling.LONG, null);

    // =====================
    // MENTAL STATES
    // =====================



    // =====================
    // Character Profiles
    // =====================

    public static final CharacterProfile paladinProfile = new CharacterProfile(
            "Paladin du Noyau", MentalStateType.FAITH
    );

    public static final CharacterProfile enemyProfile = new CharacterProfile(
            "Gnoll Irradié", MentalStateType.CORRUPTION
    );

    public static final CharacterProfile scoutProfile = new CharacterProfile(
            "Éclaireur Mutant", MentalStateType.CALM
    );

    public static final CharacterProfile mutantProfile = new CharacterProfile(
            "Mutant Électrique", MentalStateType.RAGE
    );

    public static final CharacterProfile fragileEnemyProfile = new CharacterProfile(
            "Rat Mutant", MentalStateType.HONOUR
    );


    // =====================
    // Offensive Skills
    // =====================

    public static final OffensiveSkill basicAttack = new BasicAttack();
    public static final OffensiveSkill selfAttack = new SelfAttack();
    public static final OffensiveSkill radStrike = new RadiationStrike();
    public static final DefensiveSkill guard = new Guard();
    public static final DefensiveSkill evade = new Evade();
    public static final DefensiveSkill counter = new Counter();

    // =====================
    // Character Skills
    // =====================

    public static final CharacterSkills paladinSkills = new CharacterSkills(
            List.of(basicAttack,radStrike), guard
    );

    public static final CharacterSkills enemySkills = new CharacterSkills(
            List.of(basicAttack), evade
    );

    public static final CharacterSkills scoutSkills = new CharacterSkills(
            List.of(basicAttack), evade
    );

    public static final CharacterSkills mutantSkills = new CharacterSkills(
            List.of(basicAttack,selfAttack), counter
    );

    public static final CharacterSkills fragileEnemySkills = new CharacterSkills(
            List.of(basicAttack), evade
    );

    // =====================
    // Character Equipment
    // =====================

    public static final CharacterEquipment paladinEquipment = new CharacterEquipment(SWORD);
    public static final CharacterEquipment enemyEquipment = new CharacterEquipment(SWORD);
    public static final CharacterEquipment scoutEquipment = new CharacterEquipment(RUSTY_SWORD);

    // =====================
    // Character States
    // =====================


    // =====================
    // FACTORY METHODS
    // =====================

    public static Character createPaladin() {
        return new Character(paladinProfile, PALADIN_STATS, paladinEquipment, paladinSkills);
    }

    public static Character createEnemy() {
        return new Character(enemyProfile, ENEMY_STATS, enemyEquipment, enemySkills);
    }

    public static Character createScout() {
        return new Character(scoutProfile, SCOUT_STATS, scoutEquipment, scoutSkills);
    }

    public static Character createMutant() {
        return new Character(mutantProfile, MUTANT_STATS, paladinEquipment, mutantSkills);
    }

    public static Character createFragile() {
        return new Character(fragileEnemyProfile, FRAGILE_ENEMY_STATS, enemyEquipment, fragileEnemySkills);
    }
}