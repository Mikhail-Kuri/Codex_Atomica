package org.example.gameplay.mental;

import org.example.gameplay.combat.CombatEventType;

public class CalmMentalState extends AbstractMentalState {

    @Override
    protected int deltaFor(CombatEventType event) {
        return switch (event) {
            case DAMAGE_DEALT -> 2;
            case DAMAGE_RECEIVED -> 1;
            case ALLY_DEFEATED -> 3;
            case ENEMY_DEFEATED -> -1;
            case DEFENSE_PREPARED -> -1;
            default -> 1;
        };
    }
}
