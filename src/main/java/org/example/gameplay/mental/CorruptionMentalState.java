package org.example.gameplay.mental;

import org.example.gameplay.combat.CombatEventType;
import org.example.core.character.Character;

import static org.example.app.GameTester.PrintStuff.printMentalStateEvent;


public class CorruptionMentalState implements MentalState {

    private int value;

    @Override
    public void onEvent(CombatEventType event, Character self, Character source) {
        int delta = 0;

        switch (event) {
            case DAMAGE_DEALT -> delta = +1;
            case DAMAGE_RECEIVED -> delta = +2;
            case ALLY_DEFEATED -> delta = +3;
            case ENEMY_DEFEATED -> delta = -1;
            case DEFENSE_PREPARED -> delta = -1;
        }

        value += delta;
        printMentalStateEvent(event, self, source, delta,value);
    }
}