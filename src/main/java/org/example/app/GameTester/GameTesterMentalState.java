package org.example.app.GameTester;

import org.example.Skills.Actions.DefensiveAction;
import org.example.Skills.Actions.OffensiveAction;
import org.example.app.data.GameData;
import org.example.core.character.Character;
import org.example.gameplay.combat.TurnManager;

import java.util.List;

import static org.example.app.GameTester.PrintStuff.printStats;

public class GameTesterMentalState {


    public static void runTestMentalState() {
        System.out.println("========== TEST MENTAL STATE ==========");

        Character paladin = GameData.createPaladin();
        Character enemy = GameData.createEnemy();

        TurnManager tm = new TurnManager();

        // 🛡️ Paladin se met en défense
        tm.addAction(new DefensiveAction(paladin, enemy, paladin.getCurrentDefense()));

        // ⚔️ Enemy attaque
        tm.addAction(
                new OffensiveAction(enemy, paladin, enemy.getDefaultOffensiveSkill())
        );

        tm.resolveCurrentTurn();

        printStats(List.of(paladin, enemy));

        System.out.println("========== END ==========\n");
    }

    public static void runTestMentalState2() {

        Character paladin = GameData.createPaladin();
        Character enemy = GameData.createEnemy();

        TurnManager tm = new TurnManager();

        // ===== TOUR 1 =====
        tm.addAction(new DefensiveAction(paladin, enemy, paladin.getCurrentDefense()));

        tm.addAction(
                new OffensiveAction(enemy, paladin, enemy.getDefaultOffensiveSkill())
        );

        tm.resolveCurrentTurn();

        // ===== TOUR 2 =====
        tm.addAction(new DefensiveAction(paladin, enemy, paladin.getCurrentDefense()));

        tm.addAction(
                new OffensiveAction(enemy, paladin, enemy.getDefaultOffensiveSkill())
        );

        tm.resolveCurrentTurn();

        printStats(List.of(paladin, enemy));
    }


    public static void main(String[] args) {
        runTestMentalState2();
    }
}
