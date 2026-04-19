package org.example.gameplay.mental;

import org.example.gameplay.combat.CombatEventType;
import org.example.core.character.Character;


public class HonourMentalState implements MentalState {

    private int value;

    @Override
    public void onEvent(CombatEventType event, Character self, Character source) {
        int delta = 0;

        switch (event) {
            case DAMAGE_DEALT -> delta = +2;
            case DAMAGE_RECEIVED -> delta = -3;
            case ALLY_DEFEATED -> delta = -2;
            case ENEMY_DEFEATED -> delta = +5;
        }

        value += delta;
        System.out.println(" (Honneur): " + value);
    }
}