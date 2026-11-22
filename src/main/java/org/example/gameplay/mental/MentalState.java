package org.example.gameplay.mental;

import org.example.gameplay.combat.CombatEventType;
import org.example.core.character.Character;

public interface MentalState {

    /**
     * Applique le gain (ou la perte) complet associé à cet événement.
     *
     * @return la variation réellement appliquée
     */
    int onEvent(CombatEventType event, Character self, Character source);

    /**
     * Applique le gain associé à cet événement, multiplié par {@code gainScale}
     * (1.0 = gain complet, 0.5 = moitié). Le résultat est tronqué vers 0.
     *
     * @return la variation réellement appliquée
     */
    int onEvent(CombatEventType event, Character self, Character source, double gainScale);

    /** Valeur actuelle de l'état mental, bornée à [-20, 20]. */
    int getValue();

    double getDamageMultiplier();

    double getIncomingDamageMultiplier();

}
