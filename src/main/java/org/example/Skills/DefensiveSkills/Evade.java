package org.example.Skills.DefensiveSkills;

import org.example.Skills.Scaling.ScalingType;
import org.example.Skills.Targeting.TargetType;
import org.example.core.character.Character;
import org.example.data.GameTuning;
import org.example.gameplay.combat.CombatEvent;
import org.example.gameplay.combat.CombatEventType;

import java.util.List;
import java.util.Random;

/**
 * Esquive relative : la chance dépend du rapport entre l'Agilité du défenseur et
 * celle de l'attaquant. Comme les deux grandissent avec le niveau, la chance reste
 * stable du niveau 1 au niveau 60. Réglages : tuning.json, section combat.evade.
 */
public class Evade extends DefensiveSkill {

    private final GameTuning.Evade tuning;
    private final Random random;

    /**
     * @param tuning réglages de la formule
     * @param random source de hasard (truquée dans les tests)
     */
    public Evade(GameTuning.Evade tuning, Random random) {
        super(
                "Evade",
                TargetType.SELF,
                List.of(ScalingType.AGILITY),
                List.of(CombatEventType.DEFENSE_PREPARED));
        this.tuning = tuning;
        this.random = random;
    }

    /**
     * Chance d'esquiver le coup de {@code attacker}, entre minChance et maxChance.
     * Sans attaquant connu, on compte comme à Agilité égale.
     */
    public double dodgeChance(Character owner, Character attacker) {
        double defense = owner.getAttributes().getAgility();
        double offense = (attacker != null) ? attacker.getAttributes().getAgility() : defense;

        double share = (defense + offense > 0) ? defense / (defense + offense) : 0.5;
        double chance = tuning.maxShare() * share
                + tuning.luckPerPoint() * owner.getAttributes().getLuck();

        return Math.max(tuning.minChance(), Math.min(tuning.maxChance(), chance));
    }

    @Override
    public int onDamageTaken(int rawDamage, Character owner, Character attacker) {
        // Réussite : 0 dégât, le combat annonce l'esquive dans le journal
        return (random.nextDouble() < dodgeChance(owner, attacker)) ? 0 : rawDamage;
    }

    @Override
    public List<CombatEvent> execute(Character source, List<CombatEventType> combatEventTypesList) {
        return resolve(source, combatEventTypesList);
    }
}
