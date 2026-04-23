package org.example.gameplay.mental;

import org.example.gameplay.combat.CombatEventType;
import org.example.core.character.Character;

import static org.example.app.GameTester.PrintStuff.printMentalStateEvent;

public class FaithMentalState extends AbstractMentalState {

    private int value;

    @Override
    public void onEvent(CombatEventType event, Character self, Character source) {
        int delta = 0;

        switch (event) {
            case DAMAGE_DEALT -> delta = +1;
            case DAMAGE_RECEIVED -> delta = -1;
            case ALLY_DEFEATED -> delta = -4;
            case ENEMY_DEFEATED -> delta = +2;
            case DEFENSE_PREPARED -> delta = +1;
        }

        value += delta;
        clamp();
        printMentalStateEvent(event, self, source, delta,value);
    }
}
