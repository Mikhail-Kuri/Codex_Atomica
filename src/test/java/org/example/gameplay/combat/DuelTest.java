package org.example.gameplay.combat;

import org.example.Skills.Actions.DefensiveAction;
import org.example.Skills.Actions.OffensiveAction;
import org.example.app.data.GameData;
import org.example.core.character.Character;
import org.example.gameplay.log.CombatLog;
import org.example.testsupport.TestCharacters;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/** Boucle de combat 1 contre 1 : tours successifs jusqu'à la victoire, la défaite ou la limite de tours. */
class DuelTest {

    private static final int MAX_TURNS = 5;

    private static final ActionChooser ALWAYS_ATTACK =
            (self, opponent) -> new OffensiveAction(self, opponent, self.getDefaultOffensiveSkill());

    private static final ActionChooser ALWAYS_DEFEND =
            (self, opponent) -> new DefensiveAction(self, self, self.getCurrentDefense());

    @Test
    @DisplayName("Le joueur gagne quand l'ennemi tombe")
    void playerWinsWhenEnemyFalls() {
        Character paladin = GameData.createPaladin();
        Character rat = GameData.createFragile();

        DuelOutcome outcome = duel(paladin, rat, ALWAYS_ATTACK, ALWAYS_ATTACK).run();

        assertEquals(DuelOutcome.PLAYER_WON, outcome);
    }

    @Test
    @DisplayName("Le joueur perd quand il tombe")
    void enemyWinsWhenPlayerFalls() {
        Character paladin = GameData.createPaladin();
        Character mutant = GameData.createMutant();

        DuelOutcome outcome = duel(paladin, mutant, ALWAYS_DEFEND, ALWAYS_ATTACK).run();

        assertEquals(DuelOutcome.ENEMY_WON, outcome);
    }

    @Test
    @DisplayName("Si personne ne tombe avant la limite de tours, c'est un match nul")
    void drawWhenTurnLimitIsReached() {
        Character paladin = GameData.createPaladin();
        Character enemy = GameData.createEnemy();

        DuelOutcome outcome = duel(paladin, enemy, ALWAYS_DEFEND, ALWAYS_DEFEND).run();

        assertEquals(DuelOutcome.DRAW, outcome);
        assertTrue(paladin.isAlive() && enemy.isAlive());
    }

    @Test
    @DisplayName("Un personnage à 2 actions choisit 2 fois par tour")
    void characterIsAskedOncePerAvailableAction() {
        Character rogue = TestCharacters.withActionsPerTurn(2, GameData.evade);
        Character enemy = GameData.createEnemy();
        AtomicInteger asked = new AtomicInteger();
        ActionChooser countingChooser = (self, opponent) -> {
            asked.incrementAndGet();
            return ALWAYS_ATTACK.choose(self, opponent);
        };

        new Duel(rogue, enemy, countingChooser, ALWAYS_DEFEND,
                new TurnManager(CombatLog.SILENT), 1).run();

        assertEquals(2, asked.get());
    }

    @Test
    @DisplayName("Un choix refusé (2e défense du tour) est redemandé")
    void refusedChoiceIsAskedAgain() {
        Character rogue = TestCharacters.withActionsPerTurn(2, GameData.evade);
        // Guard sans Vigueur : la cible ne peut pas esquiver au hasard, le coup passe toujours
        Character enemy = TestCharacters.create(0, 0, GameData.guard);
        AtomicInteger asked = new AtomicInteger();
        // Défend aux 2 premiers appels (le 2e est refusé), puis attaque
        ActionChooser chooser = (self, opponent) -> asked.incrementAndGet() <= 2
                ? ALWAYS_DEFEND.choose(self, opponent)
                : ALWAYS_ATTACK.choose(self, opponent);
        int enemyHpBefore = enemy.getCurrentHP();

        new Duel(rogue, enemy, chooser, ALWAYS_DEFEND,
                new TurnManager(CombatLog.SILENT), 1).run();

        assertEquals(3, asked.get());
        assertTrue(enemy.getCurrentHP() < enemyHpBefore, "l'attaque de remplacement a bien eu lieu");
    }

    private static Duel duel(Character player, Character enemy,
                             ActionChooser playerChooser, ActionChooser enemyChooser) {
        return new Duel(player, enemy, playerChooser, enemyChooser,
                new TurnManager(CombatLog.SILENT), MAX_TURNS);
    }
}
