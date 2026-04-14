package org.example.app.data;

import org.example.core.Attributes;
import org.example.Weapons.*;
import org.example.Skills.DefensiveSkills.*;
import org.example.core.Character;

import java.util.Set;

public class GameData {


    public static final Attributes PALADIN_STATS =
            new Attributes(100, 10, 5, 5, 5, 10, 1.5f, 0, 0, 3,6);

    public static final Attributes ENEMY_STATS =
            new Attributes(50, 1, 2,5, 2, 2, 1, 0, 0, 3, 5);

    public static final Attributes SCOUT_STATS =
            new Attributes(80, 4, 15, 5,5, 8, 1.0f, 10, 0, 7,10);

    public static final Attributes MUTANT_STATS =
            new Attributes(100, 5, 8, 5,10, 10, 2.0f, 5, 0, 1,11);

    public static final Attributes FRAGILE_ENEMY_STATS =
            new Attributes(30, 1, 1, 5,1, 1, 0.5f, 0, 0, 2,5);

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
    // FACTORY METHODS
    // =====================

    public static Character createPaladin() {
        return new Character("Paladin du Noyau", PALADIN_STATS, SWORD, new Guard());
    }

    public static Character createEnemy() {
        return new Character("Gnoll Irradié", ENEMY_STATS, RUSTY_SWORD, new Evade());
    }

    public static Character createScout() {
        return new Character("Éclaireur Mutant", SCOUT_STATS, RUSTY_SWORD, new Evade());
    }

    public static Character createMutant() {
        return new Character("Mutant Électrique", MUTANT_STATS, RUSTY_SWORD, new Counter());
    }

    public static Character createFragile() {
        return new Character("Rat Mutant", FRAGILE_ENEMY_STATS,RUSTY_SWORD, new Evade());
    }
}