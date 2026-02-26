package org.example.GameTester;

import org.example.Attributes;
import org.example.Skills.DefensiveSkills.Counter;
import org.example.Skills.DefensiveSkills.Evade;
import org.example.Skills.DefensiveSkills.Guard;
import org.example.Weapons.DamageType;
import org.example.Weapons.MeleeWeapon;
import org.example.Weapons.RangedWeapon;
import org.example.Weapons.Weapon;
import org.example.Character;

public class GameTester1 {

    public static void runTestAttaque1() {
        System.out.println("========== RUNNING: TEST ATTAQUE 1 ==========");

        Attributes pStats = new Attributes(100, 10, 5, 5, 10, 1.5f, 0, 0);
        Attributes eStats = new Attributes(50, 2, 2, 2, 2, 1, 0, 0);

        Weapon sword = new MeleeWeapon("Épée en Fer-Rouille", 15, DamageType.SLASHING, 100);
        Weapon plasmaRifle = new RangedWeapon("Fusil à Plasma", 40, DamageType.ENERGY,10);

        Character paladin = new Character("Paladin du Noyau", pStats, sword);
        Character enemy = new Character("Gnoll Irradié", eStats, sword);

        System.out.println("[Phase A: Combat médiéval]");
        paladin.performAction(0, enemy);

        System.out.println("\n--- Le Paladin trouve un Fusil à Plasma ---");
        paladin.setWeapon(plasmaRifle);
        paladin.performAction(0, enemy);

        System.out.println("========== END OF TEST ==========\n");
    }

    public static void runTestGarde1() {
        System.out.println("========== RUNNING: TEST Guard ==========");
        Attributes pStats = new Attributes(100, 2, 5, 5, 10, 1.5f, 0, 0);
        Attributes eStats = new Attributes(50, 2, 2, 2, 2, 2, 0, 0);
        Weapon sword = new MeleeWeapon("Épée en Fer-Rouille", 15, DamageType.SLASHING, 100);

        Character paladin = new Character("Paladin du Noyau", pStats, new Guard());
        Character enemy = new Character("Gnoll Irradié", eStats, sword);

        System.out.println("[Phase A: Le Paladin se prépare à défendre]");
        paladin.prepareDefense();

        System.out.println("\n[Phase B: L'ennemi attaque]");
        enemy.performAction(0, paladin);

        System.out.println("========== END OF TEST ==========\n");
    }

    public static void testEsquive1() {
        // Un Éclaireur avec peu de Vigueur (4) mais beaucoup d'Agilité (15)
        System.out.println("========== RUNNING: TEST Evade ==========");
        Attributes scoutStats = new Attributes(80, 4, 15, 5, 8, 1.0f, 10, 0);
        Character scout = new Character("Éclaireur Mutant", scoutStats, new Evade());

        Attributes eStats = new Attributes(50, 2, 2, 2, 2, 2, 0, 0);
        Weapon sword = new MeleeWeapon("Épée en Fer-Rouille", 15, DamageType.SLASHING, 100);
        Character enemy = new Character("Gnoll Irradié", eStats, sword);

        System.out.println("--- Tentative d'esquive contre une attaque de 40 ---");
        scout.prepareDefense();

        enemy.performAction(0, scout);
        System.out.println("========== END OF TEST ==========\n");
    }

    public static void runTestCounter1() {
        System.out.println("========== RUNNING: TEST COUNTER (RIPOSTE) ==========");

        Weapon sword = new MeleeWeapon("Épée en Fer-Rouille", 15, DamageType.SLASHING, 100);
        Weapon sword2 = new MeleeWeapon("Épée en Fer-Rouille", 15, DamageType.SLASHING, 100);

        // 1. Setup du vengeur (Haute Résonance pour un contre puissant)
        Attributes mStats = new Attributes(100, 5, 8, 10, 10, 2.0f, 5, 0);
        Character mutant = new Character("Mutant Électrique", mStats,sword, new Counter());

        // 2. Setup de l'agresseur (Le Gnoll qui va regretter d'avoir frappé)
        Attributes gStats = new Attributes(50, 2, 5, 2, 2, 1.0f, 0, 0);
        Character gnoll = new Character("Gnoll Cobaye", gStats, sword2);

        System.out.println("Le Gnoll attaque le Mutant !");
        int forceDuCoup = 25;

        // --- SCÉNARIO : Le Mutant prépare son contre ---
        mutant.prepareDefense();

        // Le Gnoll inflige 25, mais le Mutant a le Counter activé
        gnoll.performAction(0, mutant);

        System.out.println("\nÉtat final après l'échange :");
        System.out.println(mutant.getName() + " HP : " + mutant.getCurrentHP());
        System.out.println(gnoll.getName() + " HP : " + gnoll.getCurrentHP());

        System.out.println("========== FIN DU TEST COUNTER ==========\n");
    }


}