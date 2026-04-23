package org.example.Skills.DefensiveSkills;

import org.example.Skills.Scaling.ScalingType;
import org.example.Skills.Targeting.TargetType;
import org.example.core.character.Character;
import org.example.gameplay.combat.CombatEvent;
import org.example.gameplay.combat.CombatEventFactory;
import org.example.gameplay.combat.CombatEventType;

import java.util.ArrayList;
import java.util.List;

public abstract class DefensiveSkill {
    protected String name;
    protected TargetType targetType;
    protected List<ScalingType> scalingTypes;
    protected List<CombatEventType> combatEventTypesList;

    public DefensiveSkill(String name,
                          TargetType targetType,
                          List<ScalingType> scalingTypes,
                          List<CombatEventType> combatEventTypesList
    ) {
        this.name = name;
        this.targetType = targetType;
        this.scalingTypes = scalingTypes;
        this.combatEventTypesList = combatEventTypesList;
    }

    public abstract int onDamageTaken(int rawDamage, Character owner, Character attacker);

    public List<CombatEvent> resolve(Character source, List<CombatEventType> combatEventTypesList) {

        List<CombatEvent> events = new ArrayList<>();

        for (CombatEventType type : combatEventTypesList) {
            events.add(CombatEventFactory.create(type, source, source, 0));
        }

        return events;
    }

    public int calculateMentalScaling(Character owner, int dmg) {

        return (int) (dmg * owner.getMentalState().getIncomingDamageMultiplier());
    }

    public abstract List<CombatEvent> execute(Character source, List<CombatEventType> combatEventTypesList);

    public List<CombatEventType> getCombatEventTypesList() {
        return combatEventTypesList;
    }

    public String getName() {
        return name;
    }
}