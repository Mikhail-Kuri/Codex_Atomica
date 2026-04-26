package org.example.Skills.OffensiveSkills;

import org.example.Skills.Scaling.AttributeScaling;
import org.example.Skills.Scaling.DamageContext;
import org.example.Skills.Scaling.DamageScaling;
import org.example.Skills.Scaling.ScalingType;
import org.example.Skills.Targeting.TargetType;
import org.example.core.character.Character;
import org.example.gameplay.combat.CombatEvent;
import org.example.gameplay.combat.CombatEventFactory;
import org.example.gameplay.combat.CombatEventType;

import java.util.ArrayList;
import java.util.List;


public abstract class OffensiveSkill {

    protected String name;
    protected TargetType targetType;
    protected List<ScalingType> scalingTypes;
    protected List<DamageScaling> scalings;
    protected List<CombatEventType> combatEventTypesList;

    public OffensiveSkill(String name,
                          TargetType targetType,
                          List<ScalingType> scalingTypes,
                          List<CombatEventType> combatEventTypesList
    ) {
        this.name = name;
        this.targetType = targetType;
        this.scalingTypes = scalingTypes;
        this.scalings = scalingTypes.stream()
                .map(this::toScaling)
                .toList();
        this.combatEventTypesList = combatEventTypesList;
    }

    public List<CombatEvent> resolve(Character source, Character target) {
        List<CombatEvent> events = new ArrayList<>();

        if (source == null || target == null) return events;
        if (!source.isAlive() || !target.isAlive()) return events;

        if (!isValidTarget(source, target)) {
            System.out.println("❌ Cible invalide pour " + name);
            return events;
        }

        int damage = calculateDamage(source, target);

        for (CombatEventType type : this.combatEventTypesList) {
            events.add(CombatEventFactory.create(type, source, target, damage));
        }

        return events;
    }

    protected boolean isValidTarget(Character source, Character target) {
        return switch (targetType) {

            case ENEMY -> source != target;

            case SELF -> source == target;

            case ANY -> true;

            // ALLY case is not implemented yet, as it requires a party system to determine allies

            default -> false;
        };
    }

    protected int calculateDamage(Character source, Character target) {

        float raw = 0;

        for (DamageScaling scaling : scalings) {
            raw += scaling.compute(source, target);
        }

        DamageContext context = new DamageContext();
        context.baseDamage = raw;
        context.source = source;
        context.target = target;

        float modified = applyMental(context);


        return Math.max(0,
                Math.round(modified) - target.getAttributes().vigor
        );
    }

    private float applyMental(DamageContext ctx) {
        return (float) (
                ctx.baseDamage *
                        ctx.source.getMentalState().getDamageMultiplier()
        );
    }

    private DamageScaling toScaling(ScalingType type) {
        return new AttributeScaling(type, 1.0f);
    }

    public String getName() {
        return name;
    }

    public abstract List<CombatEvent> execute(Character source, Character target);

    public List<CombatEventType> getCombatEventTypesList() {
        return combatEventTypesList;
    }
}
