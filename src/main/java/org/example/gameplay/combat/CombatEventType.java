package org.example.gameplay.combat;
import org.example.core.character.Character;

public enum CombatEventType {

    DAMAGE_DEALT {
        @Override
        public CombatEvent create(Character source, Character target, int value) {
            return new CombatEvent(this, source, target, value);
        }
    },

    DAMAGE_RECEIVED {
        @Override
        public CombatEvent create(Character source, Character target, int value) {
            return new CombatEvent(this, target, source, value);
        }
    },

    DEFENSE_PREPARED {
        @Override
        public CombatEvent create(Character source, Character target, int value) {
            return new CombatEvent(this, source, source, 0);
        }
    },

    ATTACK_MISSED {
        @Override
        public CombatEvent create(Character source, Character target, int value) {
            return new CombatEvent(this, source, target, 0);
        }
    },

    ATTACK_BLOCKED {
        @Override
        public CombatEvent create(Character source, Character target, int value) {
            return new CombatEvent(this, target, source, 0);
        }
    },

    ENEMY_DEFEATED {
        @Override
        public CombatEvent create(Character source, Character target, int value) {
            return new CombatEvent(this, source, target, 0);
        }
    },

    ALLY_DEFEATED {
        @Override
        public CombatEvent create(Character source, Character target, int value) {
            return new CombatEvent(this, source, target, 0);
        }
    },

    TURN_STARTED {
        @Override
        public CombatEvent create(Character source, Character target, int value) {
            return new CombatEvent(this, source, source, 0);
        }
    },

    TURN_ENDED {
        @Override
        public CombatEvent create(Character source, Character target, int value) {
            return new CombatEvent(this, source, source, 0);
        }
    };

    public abstract CombatEvent create(Character source, Character target, int value);
}