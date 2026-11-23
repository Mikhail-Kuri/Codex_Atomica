package org.example.data;

/** Statistiques d'un personnage, telles que nommées dans les fichiers de données. */
public enum Stat {
    VITALITY,
    VIGOR,
    AGILITY,
    STRENGTH,
    INTELLIGENCE,
    WILLPOWER,
    /** Seule statistique décimale : multiplie la puissance de l'arme. */
    RESONANCE,
    LUCK,
    MIN_SPEED,
    MAX_SPEED;

    /** Vrai si la statistique doit être un nombre entier. */
    public boolean isWholeNumber() {
        return this != RESONANCE;
    }
}
