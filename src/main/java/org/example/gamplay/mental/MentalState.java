package org.example.gamplay.mental;

import org.example.gamplay.combat.CombatEvent;

public interface MentalState {

    void onEvent(CombatEvent event, Character self, Character source);
}
