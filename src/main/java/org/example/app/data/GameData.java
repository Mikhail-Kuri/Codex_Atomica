package org.example.app.data;

import org.example.Skills.OffensiveSkills.BasicAttack;
import org.example.Skills.OffensiveSkills.OffensiveSkill;
import org.example.Skills.OffensiveSkills.SelfAttack;
import org.example.core.character.Attributes.CharacterAttributes;
import org.example.Weapons.*;
import org.example.Skills.DefensiveSkills.*;
import org.example.core.character.Character;
import org.example.core.character.Attributes.arsenal.CharacterEquipment;
import org.example.core.character.Attributes.arsenal.CharacterSkills;
import org.example.core.character.Attributes.CharacterProfile;
import org.example.gameplay.mental.*;

import java.util.List;
import java.util.Set;

public class GameData {


    public static final CharacterAttributes PALADIN_STATS =
            new CharacterAttributes(100, 10, 5, 5, 5, 10, 1.5f, 0, 3, 6);

    public static final CharacterAttributes ENEMY_STATS =
            new CharacterAttributes(50, 1, 2, 5, 2, 2, 1, 0, 3, 5);

    public static final CharacterAttributes SCOUT_STATS =
            new CharacterAttributes(80, 4, 15, 5, 5, 8, 1.0f, 10, 7, 10);

    public static final CharacterAttributes MUTANT_STATS =
            new CharacterAttributes(100, 5, 8, 5, 10, 10, 2.0f, 5, 1, 11);

    public static final CharacterAttributes FRAGILE_ENEMY_STATS =
            new CharacterAttributes(30, 1, 1, 5, 1, 1, 0.5f, 0, 2, 5);

    // =====================
    // WEAPONS
    // =====================

    public static final Weapon SWORD =
            new Weapon("Épée en fer", 35, Set.of(DamageType.PHYSICAL), WeaponRange.MELEE, null);

    public static final Weapon RUSTY_SWORD =
            new Weapon("Épée en Fer-Rouille", 25, Set.of(DamageType.SLASHING), WeaponRange.MELEE, null);

    public static final Weapon PLASMA =
            new Weapon("Fusil à Plasma", 60, Set.of(DamageType.ENERGY), WeaponRange.LONG, null);

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
    public static final DefensiveSkill guard = new Guard();
    public static final DefensiveSkill evade = new Evade();
    public static final DefensiveSkill counter = new Counter();

    // =====================
    // Character Skills
    // =====================

    public static final CharacterSkills paladinSkills = new CharacterSkills(
            List.of(basicAttack), guard
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

    public static final CharacterSkills fagileEnemySkills = new CharacterSkills(
            List.of(basicAttack), evade
    );

    // =====================
    // Character Equipment
    // =====================

    public static final CharacterEquipment paladinEquipment = new CharacterEquipment(SWORD);
    public static final CharacterEquipment enemyEquipment = new CharacterEquipment(RUSTY_SWORD);
    public static final CharacterEquipment scoutEquipment = new CharacterEquipment(RUSTY_SWORD);

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
        return new Character(fragileEnemyProfile, FRAGILE_ENEMY_STATS, enemyEquipment, fagileEnemySkills);
    }
}