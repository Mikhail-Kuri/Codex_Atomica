package org.example.core.character;

/**
 * Résultat d'un coup reçu.
 *
 * @param damage             dégâts finaux calculés (avant le plancher à 0 PV)
 * @param mitigatedByDefense vrai si la défense active a réduit le coup
 *                           par rapport à un coup non défendu
 */
public record DamageResult(int damage, boolean mitigatedByDefense) {

    /** Aucun dégât et aucune défense utilisée (cible déjà hors de combat, par exemple). */
    public static final DamageResult NONE = new DamageResult(0, false);
}
