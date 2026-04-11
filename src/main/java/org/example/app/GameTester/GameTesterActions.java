package org.example.app.GameTester;

import org.example.Skills.Actions.DefensiveAction;
import org.example.Skills.Actions.OffensiveAction;
import org.example.app.data.GameData;
import org.example.combat.TurnManager;
import org.example.core.Character;

import java.util.List;


public class GameTesterActions {


    public static void runTestAttaque1() {

        System.out.println("========== TEST ATTAQUE ==========");

        Character paladin = GameData.createPaladin();
        Character enemy = GameData.createEnemy();

        TurnManager tm = new TurnManager();

        tm.addAction(
                new OffensiveAction(paladin, enemy, paladin.getDefaultOffensiveSkill())
        );

        tm.resolveTurn();

        printStats(List.of(paladin, enemy));

        System.out.println("========== END ==========\n");
    }

    public static void runTestGarde1() {
        System.out.println("========== TEST GUARD ==========");

        Character paladin = GameData.createPaladin();
        Character enemy = GameData.createEnemy();

        printStats(List.of(paladin, enemy));

        TurnManager tm = new TurnManager();

        tm.addAction(new DefensiveAction(paladin, enemy));
        tm.addAction(
                new OffensiveAction(paladin, enemy, paladin.getDefaultOffensiveSkill())
        );

        tm.resolveTurn();

        printStats(List.of(paladin, enemy));

        System.out.println("========== END ==========\n");
    }


    public static void testEsquive1() {
        System.out.println("========== TEST EVADE ==========");

        Character scout = GameData.createScout();
        Character enemy = GameData.createEnemy();

        TurnManager tm = new TurnManager();

        tm.addAction(new DefensiveAction(scout, enemy));
        tm.addAction(
                new OffensiveAction(enemy, scout, enemy.getDefaultOffensiveSkill())
        );

        tm.resolveTurn();

        printStats(List.of(scout, enemy));


        System.out.println("========== END ==========\n");
    }

    public static void runTestCounter1() {
        System.out.println("========== TEST COUNTER ==========");

        Character mutant = GameData.createMutant();
        Character enemy = GameData.createEnemy();
        TurnManager tm = new TurnManager();

        tm.addAction(new DefensiveAction(mutant, enemy));
        tm.addAction(
                new OffensiveAction(enemy, mutant, enemy.getDefaultOffensiveSkill())
        );

        tm.resolveTurn();

        printStats(List.of(mutant, enemy));

        System.out.println("\nHP Mutant : " + mutant.getCurrentHP());
        System.out.println("HP Ennemi : " + enemy.getCurrentHP());

        System.out.println("========== END ==========\n");
    }

    public static void runTestMortality() {
        System.out.println("========== TEST MORTALITY ==========");

        Character fragile = GameData.createFragile();
        Character enemy = GameData.createEnemy();

        TurnManager tm = new TurnManager();
        tm.addAction(
                new OffensiveAction(enemy, fragile, enemy.getDefaultOffensiveSkill())
        );
        tm.addAction(
                new OffensiveAction(enemy, fragile, enemy.getDefaultOffensiveSkill())
        );

        tm.addAction(
                new OffensiveAction(fragile, enemy, fragile.getDefaultOffensiveSkill())
        );

        tm.resolveTurn();


        System.out.println("========== END ==========\n");
    }


    public static void runAllTests() {
        runTestAttaque1();
        runTestGarde1();
        testEsquive1();
        runTestCounter1();
        runTestMortality();
    }

    public static void main(String[] args) {
        runAllTests();
    }

    public static void printStats(List<Character> characters) {
        for (Character character : characters) {
            character.printStats();
        }
    }

}


