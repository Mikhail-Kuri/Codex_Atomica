package org.example.app.GameTester;

import org.example.Skills.Actions.DefensiveAction;
import org.example.Skills.Actions.OffensiveAction;
import org.example.Skills.OffensiveSkills.OffensiveSkill;
import org.example.Skills.OffensiveSkills.SelfAttack;
import org.example.app.data.GameData;
import org.example.core.character.Character;
import org.example.gameplay.combat.TurnManager;

import java.util.List;

import static org.example.app.GameTester.PrintStuff.printStats;


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
    public static void runTestAttaque2(){
        System.out.println("========== TEST ATTAQUE ==========");

        Character paladin = GameData.createPaladin();
        Character enemy = GameData.createEnemy();

        TurnManager tm = new TurnManager();

        tm.addAction(
                new OffensiveAction(enemy, paladin, enemy.getDefaultOffensiveSkill())
        );

        tm.addAction(
                new OffensiveAction(enemy, paladin, enemy.getDefaultOffensiveSkill())
        );

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

        tm.addAction(new DefensiveAction(paladin, enemy, paladin.getCurrentDefense()));
        tm.addAction(
                new OffensiveAction(enemy, paladin, enemy.getDefaultOffensiveSkill())
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

        tm.addAction(new DefensiveAction(scout, enemy, enemy.getCurrentDefense()));
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

        tm.addAction(new DefensiveAction(mutant, enemy, enemy.getCurrentDefense()));
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

    public static void runSelfDamageTest() {
        System.out.println("========== TEST SELF DAMAGE 1==========");

        Character mutant = GameData.createMutant();


        TurnManager tm = new TurnManager();
        tm.addAction(
                new OffensiveAction(mutant, mutant, mutant.getDefaultOffensiveSkill())
        );

        tm.resolveTurn();

        printStats(List.of(mutant));

        System.out.println("========== END ==========\n");
    }

    public static void runDamageCheckWithSelfType() {
        System.out.println("========== TEST SELF DAMAGE 2 ==========");

        Character mutant = GameData.createMutant();
        mutant.getAttributes().setVigor(0);

        OffensiveSkill selfAttack = mutant.getOffensiveSkills().stream()
                .filter(skill -> skill instanceof SelfAttack)
                .map(skill -> (SelfAttack) skill)
                .findFirst()
                .orElse(null);

        TurnManager tm = new TurnManager();

        tm.addAction(
                new OffensiveAction(mutant, mutant, selfAttack)
        );

        tm.resolveTurn();

        printStats(List.of(mutant));

        System.out.println("========== END ==========\n");
    }


    public static void runAllTests() {
        runTestAttaque1();
        runTestGarde1();
        testEsquive1();
        runTestCounter1();
        runTestMortality();
        runSelfDamageTest();
        runDamageCheckWithSelfType();
    }

    public static void main(String[] args) {
        runTestAttaque2();
    }



}


