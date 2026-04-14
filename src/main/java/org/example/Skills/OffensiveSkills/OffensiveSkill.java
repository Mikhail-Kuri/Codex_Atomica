package org.example.Skills.OffensiveSkills;

import org.example.Skills.Scaling.ScalingType;
import org.example.Skills.Targeting.TargetType;
import org.example.core.Character;

import java.util.List;


public abstract class OffensiveSkill {

    protected String name;
    protected TargetType targetType;
    protected List<ScalingType> scalingTypes;

    public OffensiveSkill(String name, TargetType targetType, List<ScalingType>  scalingTypes) {
        this.name = name;
        this.targetType = targetType;
        this.scalingTypes = scalingTypes;
    }

    protected boolean isValidTarget(Character source, Character target) {

        return !switch (targetType) {

            case ENEMY -> source != target;

            case SELF -> source == target;

            case ANY -> true;

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

    public abstract void execute(Character source, Character target);
}
