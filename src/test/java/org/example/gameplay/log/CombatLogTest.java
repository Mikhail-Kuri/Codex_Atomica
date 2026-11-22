package org.example.gameplay.log;

import org.example.Skills.Actions.DefensiveAction;
import org.example.Skills.Actions.OffensiveAction;
import org.example.app.data.GameData;
import org.example.core.character.Character;
import org.example.effects.types.BurnEffect;
import org.example.gameplay.combat.TurnManager;
import org.example.testsupport.RecordingCombatLog;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Règle : la logique du combat n'écrit jamais directement dans la console.
 * Tout passe par un CombatLog : « info » pour le joueur, « debug » pour le détail des calculs.
 */
class CombatLogTest {

    private static final int BURN_DAMAGE = 4;
    private static final int BURN_DURATION = 2;

    @Test
    @DisplayName("La logique du combat n'écrit rien dans la console")
    void combatLogicWritesNothingToConsole() {
        PrintStream realOut = System.out;
        ByteArrayOutputStream console = new ByteArrayOutputStream();
        System.setOut(new PrintStream(console, true, StandardCharsets.UTF_8));
        try {
            Character paladin = GameData.createPaladin();
            Character mutant = GameData.createMutant(); // Contre : déclenche une riposte
            mutant.applyStatusEffect(new BurnEffect(BURN_DAMAGE, BURN_DURATION));
            TurnManager turnManager = new TurnManager(new RecordingCombatLog());

            turnManager.addAction(new DefensiveAction(mutant, mutant, mutant.getCurrentDefense()));
            turnManager.addAction(attack(paladin, mutant));
            turnManager.addAction(attack(paladin, mutant)); // refusée : budget épuisé
            turnManager.resolveCurrentTurn();
            turnManager.endTurn(List.of(paladin, mutant));
        } finally {
            System.setOut(realOut);
        }

        assertEquals("", console.toString(StandardCharsets.UTF_8));
    }

    @Test
    @DisplayName("Les dégâts sont annoncés au joueur (info)")
    void damageIsReportedAsInfo() {
        Character paladin = GameData.createPaladin();
        Character enemy = GameData.createEnemy();
        RecordingCombatLog log = new RecordingCombatLog();
        TurnManager turnManager = new TurnManager(log);

        turnManager.addAction(attack(paladin, enemy));
        turnManager.resolveCurrentTurn();

        assertTrue(log.infoContains(enemy.getName() + " subit"));
    }

    @Test
    @DisplayName("Le détail des calculs de dégâts est en debug, pas en info")
    void damageDetailsAreDebugOnly() {
        Character paladin = GameData.createPaladin();
        Character enemy = GameData.createEnemy();
        RecordingCombatLog log = new RecordingCombatLog();
        TurnManager turnManager = new TurnManager(log);

        turnManager.addAction(attack(paladin, enemy));
        turnManager.resolveCurrentTurn();

        assertTrue(log.debugContains("résistance"));
        assertFalse(log.infoContains("résistance"));
    }

    @Test
    @DisplayName("Les dégâts d'un effet de statut en fin de tour sont annoncés")
    void statusDamageIsReported() {
        Character paladin = GameData.createPaladin();
        paladin.applyStatusEffect(new BurnEffect(BURN_DAMAGE, BURN_DURATION));
        RecordingCombatLog log = new RecordingCombatLog();

        new TurnManager(log).endTurn(List.of(paladin));

        assertTrue(log.infoContains("Burn"));
    }

    @Test
    @DisplayName("La mort d'un personnage est annoncée")
    void deathIsReported() {
        Character paladin = GameData.createPaladin();
        Character rat = GameData.createFragile(); // 30 PV, ne survit pas à un coup du Paladin
        RecordingCombatLog log = new RecordingCombatLog();
        TurnManager turnManager = new TurnManager(log);

        turnManager.addAction(attack(paladin, rat));
        turnManager.resolveCurrentTurn();

        assertFalse(rat.isAlive());
        assertTrue(log.infoContains(rat.getName() + " est mort"));
    }

    @Test
    @DisplayName("Une action refusée est expliquée au joueur")
    void refusedActionIsReported() {
        Character paladin = GameData.createPaladin();
        Character enemy = GameData.createEnemy();
        RecordingCombatLog log = new RecordingCombatLog();
        TurnManager turnManager = new TurnManager(log);

        turnManager.addAction(attack(paladin, enemy));
        turnManager.addAction(attack(paladin, enemy));

        assertTrue(log.infoContains(paladin.getName() + " n'a plus d'action"));
    }

    private static OffensiveAction attack(Character source, Character target) {
        return new OffensiveAction(source, target, source.getDefaultOffensiveSkill());
    }
}
