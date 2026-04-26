package org.example.Skills.Scaling;
import org.example.core.character.Character;

public enum ScalingType {

    WEAPON {
        public float compute(Character source, Character target) {
            return source.getEquippedWeapon() != null
                    ? source.getEquippedWeapon().getBasePower() * source.getAttributes().resonance
                    : 0;
        }
    },

    SKILL{
        @Override
        public float compute(Character source, Character target) {
            return source.getCurrentlyUsedOffensiveSkill().getBasePower();
        }
    },

    STRENGTH {
        public float compute(Character source, Character target) {
            return source.getAttributes().strength * 1.5f;
        }
    },

    INTELLIGENCE {
        public float compute(Character source, Character target) {
            return source.getAttributes().intelligence * 1.5f;
        }
    },

    VITALITY {
        public float compute(Character source, Character target) {
            return source.getAttributes().vitality * 1.5f;
        }
    },

    VIGOR {
        public float compute(Character source, Character target) {
            return source.getAttributes().vigor * 1.5f;
        }
    },

    AGILITY {
        public float compute(Character source, Character target) {
            return source.getAttributes().agility * 1.5f;
        }
    };

    public abstract float compute(Character source, Character target);
}