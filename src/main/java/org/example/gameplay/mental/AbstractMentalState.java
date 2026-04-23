package org.example.gameplay.mental;

import org.example.gameplay.combat.CombatEventType;
import org.example.core.character.Character;

public abstract class AbstractMentalState implements MentalState {

    protected int value;

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

    @Override
    public abstract void onEvent(CombatEventType event, Character self, Character source);
}
