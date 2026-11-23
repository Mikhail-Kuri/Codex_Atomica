package org.example.Skills.DefensiveSkills;

import org.example.Skills.Scaling.ScalingType;
import org.example.Skills.Targeting.TargetType;
import org.example.core.character.Character;
import org.example.gameplay.combat.CombatEvent;
import org.example.gameplay.combat.CombatEventType;

import java.util.List;
import java.util.Random;

public class Evade extends DefensiveSkill {
    private Random random = new Random();

    public Evade() {
        super(
                "Evade",
                TargetType.SELF,
                List.of(ScalingType.AGILITY),
                List.of(CombatEventType.DEFENSE_PREPARED));
    }


    @Override
    public int onDamageTaken(int rawDamage, Character owner, Character attacker) {

        int dodgeChance = Math.min(100,
                (owner.getAttributes().agility * 4) + owner.getAttributes().luck
        );

        int roll = random.nextInt(101);

        // Réussite : 0 dégât, le combat annonce l'esquive dans le journal
        return (roll <= dodgeChance) ? 0 : rawDamage;
    }


    @Override
    public List<CombatEvent> execute(Character source, List<CombatEventType> combatEventTypesList) {
        return resolve(source, combatEventTypesList);
    }
}