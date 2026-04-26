package org.example.app.GameTester;

import org.example.Skills.Actions.DefensiveAction;
import org.example.Skills.Actions.OffensiveAction;
import org.example.Skills.DefensiveSkills.Counter;
import org.example.Skills.OffensiveSkills.OffensiveSkill;
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

        tm.resolveCurrentTurn();

        printStats(List.of(paladin, enemy));

        System.out.println("========== END ==========\n");
    }

    public static void runTestAttaque2() {
        System.out.println("========== TEST ATTAQUE ==========");

        Character paladin = GameData.createPaladin();
        Character enemy = GameData.createEnemy();

        paladin.getAttributes().setSpeed(0, 0);

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

        tm.resolveCurrentTurn();

        printStats(List.of(paladin, enemy));

        System.out.println("========== END ==========\n");

    }

    public static void runTestAttaque3() {

        System.out.println("========== TEST ATTAQUE 3 ==========");

        Character paladin = GameData.createPaladin();
        paladin.setCurrentDefense(new Counter());
        Character enemy = GameData.createMutant();


        TurnManager tm = new TurnManager();

        tm.addAction(
                new DefensiveAction(paladin, enemy, paladin.getCurrentDefense())
        );


        tm.addAction(
                new OffensiveAction(enemy, paladin, enemy.getDefaultOffensiveSkill())
        );


        tm.resolveCurrentTurn();

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

        tm.resolveCurrentTurn();

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

        tm.resolveCurrentTurn();

        printStats(List.of(scout, enemy));


        System.out.println("========== END ==========\n");
    }

    public static void runTestCounter1() {
        System.out.println("========== TEST COUNTER ==========");

        Character mutant = GameData.createMutant();
        Character enemy = GameData.createEnemy();
        TurnManager tm = new TurnManager();

        tm.addAction(new DefensiveAction(mutant, enemy, mutant.getCurrentDefense()));
        tm.addAction(
                new OffensiveAction(enemy, mutant, enemy.getDefaultOffensiveSkill())
        );

        tm.resolveCurrentTurn();

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

        tm.resolveCurrentTurn();


        System.out.println("========== END ==========\n");
    }

    public static void runSelfDamageTest() {
        System.out.println("========== TEST SELF DAMAGE 1==========");

        Character mutant = GameData.createMutant();


        TurnManager tm = new TurnManager();

        tm.addAction(
                new OffensiveAction(mutant, mutant, mutant.getDefaultOffensiveSkill())
        );

        tm.resolveCurrentTurn();

        printStats(List.of(mutant));

        System.out.println("========== END ==========\n");
    }

    public static void runDamageCheckWithSelfType() {
        System.out.println("========== TEST SELF DAMAGE 2 ==========");

        Character mutant = GameData.createMutant();
        Character paladin = GameData.createPaladin();



        TurnManager tm = new TurnManager();

        tm.addAction(
                new OffensiveAction(mutant, paladin, getOffensiveSkillByName(mutant,"Attaque de base de soi"))
        );

        tm.resolveCurrentTurn();

        printStats(List.of(mutant,paladin));

        System.out.println("========== END ==========\n");
    }

    public static void runRadStrikeTest() {
        System.out.println("========== TEST RAD STRIKE ==========");


        Character paladin = GameData.createPaladin();
        Character mutant = GameData.createMutant();

        TurnManager tm = new TurnManager();

        tm.addAction(
                new OffensiveAction(paladin, mutant, getOffensiveSkillByName(paladin, "Radiation Strike"))
        );

        tm.resolveCurrentTurn();

        printStats(List.of(mutant));

        System.out.println("========== END ==========\n");
    }

    public static void runTestAttaqueWithResistances() {
        System.out.println("========== TEST ATTAQUE AVEC RESISTANCES ==========");
        Character paladin = GameData.createPaladin();
        Character mutant = GameData.createMutant();

        TurnManager tm = new TurnManager();

        tm.addAction(
                new OffensiveAction(paladin, mutant, getOffensiveSkillByName(paladin, "1"))
        );

        tm.resolveCurrentTurn();

        printStats(List.of(paladin,mutant));

        System.out.println("========== END ==========\n");
    }

    public static void runTestAttaqueWithResistances2() {
        System.out.println("========== TEST ATTAQUE AVEC RESISTANCES ==========");
        Character paladin = GameData.createPaladin();
        Character mutant = GameData.createMutant();

        TurnManager tm = new TurnManager();

        tm.addAction(
                new OffensiveAction(paladin, mutant, getOffensiveSkillByName(paladin, "2"))
        );

        tm.resolveCurrentTurn();

        printStats(List.of(paladin,mutant));

        System.out.println("========== END ==========\n");
    }


    public static void runAllTests() {
        runTestAttaque1();
        runTestAttaque2();
        runTestGarde1();
        testEsquive1();
        runTestCounter1();
        runTestMortality();
        runSelfDamageTest();
        runDamageCheckWithSelfType();
    }

    public static OffensiveSkill getOffensiveSkillByName(Character character, String name) {
        return character.getOffensiveSkills().stream()
                .filter(skill -> skill.getName().equals(name))
                .findFirst()
                .orElse(null);
    }

    public static void main(String[] args) {
        runTestAttaqueWithResistances2();
    }


}





