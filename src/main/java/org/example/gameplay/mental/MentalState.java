package org.example.gameplay.mental;

import org.example.gameplay.combat.CombatEvent;

public interface MentalState {

    void onEvent(CombatEvent event, Character self, Character source);
}
