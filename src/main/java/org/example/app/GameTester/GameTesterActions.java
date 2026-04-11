package org.example.app.GameTester;

import org.example.Skills.DefensiveSkills.DefensiveSkill;
import org.example.Skills.DefensiveSkills.GuardAction;
import org.example.Skills.OffensiveSkills.BasicAttack;
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

        tm.addAction(new BasicAttack(paladin, enemy));

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

        tm.addAction(new GuardAction(paladin,enemy));
        tm.addAction(new BasicAttack(enemy, paladin));

        tm.resolveTurn();

        printStats(List.of(paladin, enemy));

        System.out.println("========== END ==========\n");
    }


        public static void runAllTests() {
        runTestAttaque1();
        runTestGarde1();
//        testEsquive1();
//        runTestCounter1();
//        runTestMortality();
    }

        public static void main(String[] args) {
        runAllTests();
    }

    public static void printStats(List<Character> characters){
        for (Character character : characters) {
            character.printStats();
        }
    }

}


