package org.example.Skills.OffensiveSkills;

import org.example.Skills.Scaling.ScalingType;
import org.example.Skills.Targeting.TargetType;
import org.example.core.character.Character;
import org.example.gameplay.combat.CombatEvent;
import org.example.gameplay.combat.CombatEventType;

import java.util.ArrayList;
import java.util.List;


public abstract class OffensiveSkill {

    protected String name;
    protected TargetType targetType;
    protected List<ScalingType> scalingTypes;

    public OffensiveSkill(String name, TargetType targetType, List<ScalingType> scalingTypes) {
        this.name = name;
        this.targetType = targetType;
        this.scalingTypes = scalingTypes;
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

        events.add(new CombatEvent(CombatEventType.DAMAGE_DEALT, source, target, damage));
        events.add(new CombatEvent(CombatEventType.DAMAGE_RECEIVED, target, source, damage));

        return events;
    }

    protected void applyEffects(Character source, Character target, int damage, int sanityDamage) {
        target.takeDamage(damage, source);
//        target.takeSanityDamage(sanityDamage, source);
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
        for (ScalingType scaling : scalingTypes) {
            switch (scaling) {
                case WEAPON -> raw += source.getEquippedWeapon() != null
                        ? source.getEquippedWeapon().getBasePower() * source.getAttributes().resonance
                        : 0;
                case STRENGTH -> raw += source.getAttributes().strength * 1.5f;
                case INTELLIGENCE -> raw += source.getAttributes().intelligence * 1.5f;
                case VITALITY -> raw += source.getAttributes().vitality * 1.5f;
            }
        }
        return Math.max(0, Math.round(raw) - target.getAttributes().vigor);
    }

    public String getName() {
        return name;
    }

    public abstract List<CombatEvent> execute(Character source, Character target);
}
