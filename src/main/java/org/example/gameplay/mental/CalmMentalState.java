package org.example.gameplay.mental;

import org.example.gameplay.combat.CombatEventType;
import org.example.core.character.Character;
import static org.example.app.GameTester.PrintStuff.printMentalStateEvent;

public class CalmMentalState extends AbstractMentalState {

    @Override
    public void onEvent(CombatEventType event, Character self, Character source) {

        switch (event) {
            case DAMAGE_DEALT -> value += 1;
            case DAMAGE_RECEIVED -> value -= 1;
            case ENEMY_DEFEATED -> value += 2;
            case ALLY_DEFEATED -> value -= 2;
            case DEFENSE_PREPARED -> value += 3;
        }

        clamp();
    }
}