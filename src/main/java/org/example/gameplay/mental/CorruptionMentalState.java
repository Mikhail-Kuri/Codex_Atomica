package org.example.gameplay.mental;

import org.example.gameplay.combat.CombatEvent;

public class CorruptionMentalState implements MentalState {

    private int value;

    @Override
    public void onEvent(CombatEvent event, Character self, Character source) {
        int delta = 0;

        switch (event) {
            case DAMAGE_DEALT -> delta = +1;
            case DAMAGE_RECEIVED -> delta = +2;
            case ALLY_DEFEATED -> delta = +3;
            case ENEMY_DEFEATED -> delta = -1;
        }

        value += delta;
        System.out.println(" (Corruption): " + value);
    }
}