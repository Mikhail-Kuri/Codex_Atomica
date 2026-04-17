package org.example.gamplay.mental;

import org.example.gamplay.combat.CombatEvent;

public class FaithMentalState implements MentalState {

    private int value;

    @Override
    public void onEvent(CombatEvent event, Character self, Character source) {
        int delta = 0;

        switch (event) {
            case DAMAGE_DEALT -> delta = +1;
            case DAMAGE_RECEIVED -> delta = -1;
            case ALLY_DEFEATED -> delta = -4;
            case ENEMY_DEFEATED -> delta = +2;
        }

        value += delta;
        System.out.println(" (Foi): " + value);
    }
}
