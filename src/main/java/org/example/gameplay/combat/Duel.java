package org.example.gameplay.combat;

import org.example.Skills.Actions.Action;
import org.example.core.character.Character;

import java.util.List;

/**
 * Combat 1 contre 1 complet : à chaque tour, chacun choisit ses actions
 * (autant que son budget d'actions), le tour est résolu puis terminé,
 * jusqu'à ce qu'un combattant tombe ou que la limite de tours soit atteinte.
 * N'affiche rien : tout passe par le journal du TurnManager.
 */
public class Duel {

    /** Nombre d'essais si un choix est refusé (ex. deuxième défense dans le même tour). */
    private static final int MAX_ATTEMPTS_PER_ACTION = 3;

    private final Character player;
    private final Character enemy;
    private final ActionChooser playerChooser;
    private final ActionChooser enemyChooser;
    private final TurnManager turnManager;
    private final int maxTurns;

    /**
     * @param maxTurns limite de sécurité : au-delà, le duel est un match nul
     */
    public Duel(Character player,
                Character enemy,
                ActionChooser playerChooser,
                ActionChooser enemyChooser,
                TurnManager turnManager,
                int maxTurns) {
        this.player = player;
        this.enemy = enemy;
        this.playerChooser = playerChooser;
        this.enemyChooser = enemyChooser;
        this.turnManager = turnManager;
        this.maxTurns = maxTurns;
    }

    /** Joue le duel jusqu'au bout. */
    public DuelOutcome run() {
        for (int turn = 0; turn < maxTurns && bothAlive(); turn++) {
            planActions(player, enemy, playerChooser);
            planActions(enemy, player, enemyChooser);

            turnManager.resolveCurrentTurn();
            turnManager.endTurn(List.of(player, enemy));
        }
        return outcome();
    }

    private void planActions(Character self, Character opponent, ActionChooser chooser) {
        for (int i = 0; i < self.getActionsPerTurn(); i++) {
            for (int attempt = 0; attempt < MAX_ATTEMPTS_PER_ACTION; attempt++) {
                Action action = chooser.choose(self, opponent);
                if (turnManager.addAction(action)) {
                    break;
                }
            }
        }
    }

    private boolean bothAlive() {
        return player.isAlive() && enemy.isAlive();
    }

    private DuelOutcome outcome() {
        if (player.isAlive() && !enemy.isAlive()) return DuelOutcome.PLAYER_WON;
        if (!player.isAlive() && enemy.isAlive()) return DuelOutcome.ENEMY_WON;
        return DuelOutcome.DRAW;
    }
}
