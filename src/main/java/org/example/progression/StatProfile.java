package org.example.progression;

import org.example.core.character.Attributes.CharacterAttributes;
import org.example.data.Stat;

import java.util.EnumMap;
import java.util.Map;

/**
 * Statistiques de base (niveau 1) et croissance par niveau d'une classe ou d'un ennemi.
 * Formule S1 : {@code stat(L) = base + croissance × (L − 1)}, arrondie vers le bas
 * pour les statistiques entières.
 */
public class StatProfile {

    private final Map<Stat, Double> base;
    private final Map<Stat, Double> growth;

    /** @param growth une statistique absente ne grandit pas */
    public StatProfile(Map<Stat, Double> base, Map<Stat, Double> growth) {
        this.base = new EnumMap<>(Stat.class);
        this.base.putAll(base);
        this.growth = new EnumMap<>(Stat.class);
        if (growth != null) this.growth.putAll(growth);
    }

    /** Valeur d'une statistique au niveau donné. */
    public double valueAt(Stat stat, int level) {
        double value = base.getOrDefault(stat, 0.0)
                + growth.getOrDefault(stat, 0.0) * (level - 1);
        return stat.isWholeNumber() ? Math.floor(value) : value;
    }

    /** Écrit toutes les statistiques du niveau donné dans {@code attributes}. */
    public void applyTo(CharacterAttributes attributes, int level) {
        attributes.setVitality(whole(Stat.VITALITY, level));
        attributes.setVigor(whole(Stat.VIGOR, level));
        attributes.setAgility(whole(Stat.AGILITY, level));
        attributes.setStrength(whole(Stat.STRENGTH, level));
        attributes.setIntelligence(whole(Stat.INTELLIGENCE, level));
        attributes.setWillpower(whole(Stat.WILLPOWER, level));
        attributes.setResonance((float) valueAt(Stat.RESONANCE, level));
        attributes.setLuck(whole(Stat.LUCK, level));
        attributes.setSpeed(whole(Stat.MIN_SPEED, level), whole(Stat.MAX_SPEED, level));
    }

    private int whole(Stat stat, int level) {
        return (int) valueAt(stat, level);
    }
}
