package org.example.gameplay.mental;

import org.example.gameplay.combat.CombatEventType;
import org.example.core.character.Character;

import static org.example.app.GameTester.PrintStuff.printMentalStateEvent;


public class CorruptionMentalState extends AbstractMentalState {

    @Override
    public void onEvent(CombatEventType event, Character self, Character source) {

        int delta = switch (event) {
            case DAMAGE_DEALT -> 2;
            case DAMAGE_RECEIVED -> 1;
            case ALLY_DEFEATED -> 3;
            case ENEMY_DEFEATED -> -1;
            case DEFENSE_PREPARED -> -1;
            default -> 1;
        };

        value += delta;

        clamp();
        printMentalStateEvent(event, self, source, delta, value);
    }
}