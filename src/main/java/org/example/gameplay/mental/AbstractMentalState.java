package org.example.gameplay.mental;

import org.example.gameplay.combat.CombatEventType;
import org.example.core.character.Character;

public abstract class AbstractMentalState implements MentalState {

    protected int value;

    /**
     * Gain (ou perte) de cette personnalité pour un événement, à pleine intensité.
     * C'est le seul endroit que chaque état mental doit définir.
     */
    protected abstract int deltaFor(CombatEventType event);

    @Override
    public int onEvent(CombatEventType event, Character self, Character source) {
        return onEvent(event, self, source, 1.0);
    }

    @Override
    public int onEvent(CombatEventType event, Character self, Character source, double gainScale) {
        int before = value;

        value += (int) (deltaFor(event) * gainScale);
        clamp();

        return value - before;
    }

    @Override
    public int getValue() {
        return value;
    }

    protected void clamp() {
        value = Math.max(-20, Math.min(20, value));
    }

    protected float normalize() {
        return (float) Math.tanh(value / 10.0);
    }

    @Override
    public double getDamageMultiplier() {

        float n = normalize(); // [-1, 1]
        return 1.0 + (n * 0.4);
    }

    @Override
    public double getIncomingDamageMultiplier() {

        float n = normalize();

        return 1.0 - (n * 0.3);
    }
}
