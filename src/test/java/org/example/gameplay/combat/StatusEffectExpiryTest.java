package org.example.gameplay.combat;

import org.example.app.data.GameData;
import org.example.core.character.Character;
import org.example.effects.types.BurnEffect;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Régression : les effets de statut n'étaient jamais retirés (isExpired() n'était
 * appelé nulle part), donc une brûlure infligeait des dégâts pour toujours.
 */
class StatusEffectExpiryTest {

    private static final int BURN_DAMAGE = 4;
    private static final int BURN_DURATION = 2;

    @Test
    @DisplayName("Bug : une brûlure de 2 tours restait active indéfiniment")
    void expiredEffectIsRemovedAfterItsLastTurn() {
        Character target = GameData.createPaladin();
        target.applyStatusEffect(new BurnEffect(BURN_DAMAGE, BURN_DURATION));
        TurnManager turnManager = new TurnManager();

        turnManager.endTurn(List.of(target));
        assertEquals(1, target.getState().getStatusEffects().size(),
                "Après le tour 1, la brûlure doit encore être active");

        turnManager.endTurn(List.of(target));
        assertEquals(0, target.getState().getStatusEffects().size(),
                "Après le tour 2, la brûlure doit avoir expiré");
    }

    @Test
    @DisplayName("Bug : une brûlure expirée continuait d'infliger des dégâts")
    void expiredEffectNoLongerDealsDamage() {
        Character target = GameData.createPaladin();
        target.applyStatusEffect(new BurnEffect(BURN_DAMAGE, BURN_DURATION));
        TurnManager turnManager = new TurnManager();

        for (int turn = 0; turn < BURN_DURATION; turn++) {
            turnManager.endTurn(List.of(target));
        }
        int hpAfterBurn = target.getCurrentHP();

        turnManager.endTurn(List.of(target));

        assertEquals(hpAfterBurn, target.getCurrentHP());
    }
}
