package org.example.app.data;

import org.example.Skills.OffensiveSkills.BasicAttack;
import org.example.Skills.OffensiveSkills.OffensiveSkill;
import org.example.Skills.OffensiveSkills.SelfAttack;
import org.example.core.Attributes;
import org.example.Weapons.*;
import org.example.Skills.DefensiveSkills.*;
import org.example.core.character.Character;
import org.example.core.character.arsenal.CharacterEquipment;
import org.example.core.character.arsenal.CharacterSkills;
import org.example.core.character.profile.CharacterProfile;
import org.example.gameplay.mental.*;

import java.util.List;
import java.util.Set;

public class GameData {


    public static final Attributes PALADIN_STATS =
            new Attributes(100, 10, 5, 5, 5, 10, 1.5f, 0, 0, 3, 6);

    public static final Attributes ENEMY_STATS =
            new Attributes(50, 1, 2, 5, 2, 2, 1, 0, 0, 3, 5);

    public static final Attributes SCOUT_STATS =
            new Attributes(80, 4, 15, 5, 5, 8, 1.0f, 10, 0, 7, 10);

    public static final Attributes MUTANT_STATS =
            new Attributes(100, 5, 8, 5, 10, 10, 2.0f, 5, 0, 1, 11);

    public static final Attributes FRAGILE_ENEMY_STATS =
            new Attributes(30, 1, 1, 5, 1, 1, 0.5f, 0, 0, 2, 5);

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

    public static final MentalState faithState = new FaithMentalState();
    public static final MentalState corruption = new CorruptionMentalState();
    public static final MentalState Honour = new HonourMentalState();
    public static final MentalState rage = new RageMentalState();
    public static final MentalState calm = new CalmMentalState();

    public static final MentalStateType faithStateType = MentalStateType.FAITH;
    public static final MentalStateType corruptionStateType = MentalStateType.CORRUPTION;
    public static final MentalStateType HonourhStateType = MentalStateType.HONOUR;
    public static final MentalStateType rageStateType = MentalStateType.RAGE;
    public static final MentalStateType calmStateType = MentalStateType.CALM;

    // =====================
    // Character Profiles
    // =====================

    public static final CharacterProfile paladinProfile = new CharacterProfile("Paladin du Noyau", faithStateType);
    public static final CharacterProfile enemyProfile = new CharacterProfile("Gnoll Irradié", corruptionStateType);
    public static final CharacterProfile scoutProfile = new CharacterProfile("Éclaireur Mutant", calmStateType);
    public static final CharacterProfile mutantProfile = new CharacterProfile("Mutant Électrique", faithStateType);
    public static final CharacterProfile fragileEnemyProfile = new CharacterProfile("Rat Mutant", faithStateType);

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