package org.example.Skills.Scaling;
import org.example.core.character.Character;

public class AttributeScaling implements DamageScaling {
    private final ScalingType type;
    private final float ratio;

    public AttributeScaling(ScalingType type, float ratio) {
        this.type = type;
        this.ratio = ratio;
    }

    @Override
    public float compute(Character source, Character target) {
        return type.compute(source, target) * ratio;
    }
}
