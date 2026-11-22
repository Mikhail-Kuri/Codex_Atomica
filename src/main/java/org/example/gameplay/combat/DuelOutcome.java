package org.example.gameplay.combat;

/** Issue d'un duel, du point de vue du joueur. */
public enum DuelOutcome {
    PLAYER_WON,
    ENEMY_WON,
    /** Les deux tombent le même tour, ou la limite de tours est atteinte. */
    DRAW
}
