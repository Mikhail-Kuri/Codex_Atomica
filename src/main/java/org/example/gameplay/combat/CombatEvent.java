package org.example.gameplay.combat;
import org.example.core.character.Character;


public class CombatEvent {

    private final CombatEventType type;
    private final Character source;
    private final Character target;
    private final int value; // dégâts, heal, sanity, etc.

    public CombatEvent(CombatEventType type, Character source, Character target, int value) {
        this.type = type;
        this.source = source;
        this.target = target;
        this.value = value;
    }

    public CombatEventType getType() { return type; }
    public Character getSource() { return source; }
    public Character getTarget() { return target; }
    public int getValue() { return value; }
}