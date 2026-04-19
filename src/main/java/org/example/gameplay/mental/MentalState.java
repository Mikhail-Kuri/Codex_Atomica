package org.example.gameplay.mental;

import org.example.gameplay.combat.CombatEventType;
import org.example.core.character.Character;

public interface MentalState {

    void onEvent(CombatEventType event, Character self, Character source);
}
