package org.example.app.GameTester;

import org.example.Skills.Actions.DefensiveAction;
import org.example.Skills.Actions.OffensiveAction;
import org.example.app.data.GameData;
import org.example.core.character.Character;
//import org.example.gameplay.combat.CombatEngine;
//import org.example.gameplay.combat.EventSystem;
import org.example.gameplay.combat.TurnManager;

public class GameTesterSpeed {


    public static void runTestSpeed() {
        System.out.println("========== TEST SPEED ==========");

        Character paladin = GameData.createPaladin();
        Character scout = GameData.createMutant();


        TurnManager tm = new TurnManager();


        tm.addAction(
                new OffensiveAction(paladin, scout, paladin.getDefaultOffensiveSkill())
        );

        tm.addAction(
                new OffensiveAction(scout, paladin, scout.getDefaultOffensiveSkill())
        );


        tm.resolveTurn();

        System.out.println("========== END ==========\n");

    }

    public static void runTestSameSpeed() {
        System.out.println("========== TEST SAME SPEED ==========");

        Character paladin = GameData.createPaladin();
        Character enemy = GameData.createEnemy();

        paladin.getAttributes().setSpeed(10, 15);
        enemy.getAttributes().setSpeed(10, 15);

        TurnManager tm = new TurnManager();


        tm.addAction(
                new OffensiveAction(paladin, enemy, paladin.getDefaultOffensiveSkill())
        );

        tm.addAction(
                new OffensiveAction(enemy, paladin, enemy.getDefaultOffensiveSkill())
        );

        tm.resolveTurn();

        System.out.println("========== END ==========\n");
    }

    public static void runTestZeroSpeed() {
        System.out.println("========== TEST ZERO SPEED ==========");

        Character paladin = GameData.createPaladin();
        Character enemy = GameData.createEnemy();

        paladin.getAttributes().setSpeed(0, 0);
        enemy.getAttributes().setSpeed(0, 0);

        TurnManager tm = new TurnManager();


        tm.addAction(
                new OffensiveAction(enemy, paladin, enemy.getDefaultOffensiveSkill())
        );

        tm.addAction(
                new OffensiveAction(paladin, enemy, paladin.getDefaultOffensiveSkill())
        );

        tm.resolveTurn();

        System.out.println("========== END ==========\n");
    }

    public static void runTestNegativeSpeed() {
        System.out.println("========== TEST NEGATIVE SPEED ==========");

        Character paladin = GameData.createPaladin();
        Character enemy = GameData.createEnemy();

        paladin.getAttributes().setSpeed(-15, -10);
        enemy.getAttributes().setSpeed(-8, -5);

        TurnManager tm = new TurnManager();


        tm.addAction(
                new OffensiveAction(enemy, paladin, enemy.getDefaultOffensiveSkill())
        );

        tm.addAction(
                new OffensiveAction(paladin, enemy, paladin.getDefaultOffensiveSkill())
        );

        tm.resolveTurn();

        System.out.println("========== END ==========\n");

    }

    public static void runTestSpeedWithDefensive() {
        System.out.println("========== TEST SPEED WITH DEFENSIVE ACTIONS ==========");

        Character paladin = GameData.createPaladin();
        Character scout = GameData.createScout();

        TurnManager tm = new TurnManager();


        tm.addAction(
                new OffensiveAction(scout, paladin, scout.getDefaultOffensiveSkill())
        );

        tm.addAction(new DefensiveAction(paladin, scout, paladin.getCurrentDefense()));

        tm.resolveTurn();

        System.out.println("========== END ==========\n");

    }


    public static void run8SpeedTests() {
        System.out.println("========== TEST SPEED 8 TIMES ==========\n");

        for (int i = 0; i < 8; i++) {
            runTestSpeed();
            System.out.println("Test " + (i + 1) + " completed.\n");
        }
    }


    public static void runAllTests() {
        runTestSpeed();
        runTestSameSpeed();
        runTestZeroSpeed();
        runTestNegativeSpeed();
        runTestSpeedWithDefensive();
        run8SpeedTests();
    }


    public static void main(String[] args) {
        runAllTests();
    }
}


