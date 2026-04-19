package org.example.gameplay.mental;

import org.example.gameplay.combat.CombatEventType;
import org.example.core.character.Character;

public class RageMentalState implements MentalState {

    private int value;

    @Override
    public void onEvent(CombatEventType event, Character self, Character source) {
        int delta = 0;

        switch (event) {
            case DAMAGE_DEALT -> delta = +3;
            case DAMAGE_RECEIVED -> delta = +4;
            case ALLY_DEFEATED -> delta = +1;
            case ENEMY_DEFEATED -> delta = +2;
        }

        value += delta;
        System.out.println(" (Rage): " + value);
    }
}