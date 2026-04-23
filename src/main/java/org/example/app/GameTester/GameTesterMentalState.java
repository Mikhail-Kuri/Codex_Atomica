package org.example.app.GameTester;

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

        tm.addAction(
                new OffensiveAction(enemy, paladin, enemy.getDefaultOffensiveSkill())
        );

        tm.resolveCurrentTurn();

        printStats(List.of(paladin, enemy));

        System.out.println("========== END ==========\n");
    }


    public static void main(String[] args) {
        runTestMentalState();
    }
}
