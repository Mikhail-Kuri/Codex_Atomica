package org.example.GameTester;

import org.example.Attributes;
import org.example.Skills.DefensiveSkills.Evade;
import org.example.Skills.DefensiveSkills.Guard;
import org.example.Weapons.Weapon;
import org.example.Character;

public class GameTester1 {

    public static void runTestAttaque1() {
        System.out.println("========== RUNNING: TEST ATTAQUE 1 ==========");

        Attributes pStats = new Attributes(100, 10, 5, 5, 10, 1.5f, 0, 0);
        Attributes eStats = new Attributes(50, 2, 2, 2, 2, 1, 0, 0);

        Weapon sword = new Weapon("Épée en Fer-Rouille", 15, "Slashing");
        Weapon plasmaRifle = new Weapon("Fusil à Plasma", 40, "Radioactive");

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
        Weapon sword = new Weapon("Épée en Fer-Rouille", 15, "Slashing");

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

        System.out.println("--- Tentative d'esquive contre une attaque de 40 ---");
        scout.prepareDefense();
        scout.takeDamage(40);
        System.out.println("========== END OF TEST ==========\n");
    }

    public static void runTestSurvieRadiation() {
    }


}