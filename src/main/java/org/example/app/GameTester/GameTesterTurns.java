package org.example.app.GameTester;

import org.example.app.data.GameData;
import org.example.Skills.Actions.DefensiveAction;
import org.example.Skills.Actions.OffensiveAction;
import org.example.core.character.Character;
//import org.example.gameplay.combat.CombatEngine;
//import org.example.gameplay.combat.EventSystem;
import org.example.gameplay.combat.TurnManager;

import java.util.List;

import static org.example.app.GameTester.PrintStuff.printStats;

public class GameTesterTurns {

    public static void runTestMultiTurnCombat() {

        Character paladin = GameData.createPaladin();
        Character enemy = GameData.createEnemy();
        enemy.getState().setCurrentHP(100000);

        TurnManager tm = new TurnManager();

        tm.runCombatLoop(List.of(paladin, enemy));
    }

    public static void runAllTests() {
    }

    public static void main(String[] args) {
        runTestMultiTurnCombat();
    }
}
