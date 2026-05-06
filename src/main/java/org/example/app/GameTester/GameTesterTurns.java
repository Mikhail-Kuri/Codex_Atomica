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

    public static void runTestStatusEffects() {
        Character paladin = GameData.createPaladin();
        Character enemy = GameData.createEnemy();

        TurnManager tm = new TurnManager();

        tm.addAction(
                new OffensiveAction(paladin, enemy, paladin.getDefaultOffensiveSkill())
        );

        tm.addAction(
                new DefensiveAction(enemy, paladin, enemy.getCurrentDefense())
        );

        tm.resolveCurrentTurn();

        printStats(List.of(paladin, enemy));
    }

    // JE VEUX ATTAQUE 2 FOIS AVEC LA MEME ATTAQUE POUR TESTER LES EFFETS DE STATUTS QUI DURENT PLUSIEURS TOURS

    public static void runTestStatusEffectsMultiTurn() {
        Character paladin = GameData.createPaladin();
        Character enemy = GameData.createEnemy();

        enemy.getState().setCurrentHP(100000);


        TurnManager tm = new TurnManager();

        // Tour 1
        tm.addAction(
                new OffensiveAction(paladin, enemy, paladin.getDefaultOffensiveSkill())
        );

        tm.addAction(
                new DefensiveAction(enemy, paladin, enemy.getCurrentDefense())
        );

        tm.resolveCurrentTurn();

        // Tour 2
        tm.addAction(
                new OffensiveAction(paladin, enemy, paladin.getDefaultOffensiveSkill())
        );

        tm.addAction(
                new DefensiveAction(enemy, paladin, enemy.getCurrentDefense())
        );

        tm.resolveCurrentTurn();

        printStats(List.of(paladin, enemy));
    }

    public static void runAllTests() {
    }

    public static void main(String[] args) {
        runTestStatusEffectsMultiTurn();
    }
}
