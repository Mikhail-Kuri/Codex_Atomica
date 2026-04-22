package org.example.Skills.Actions;

import org.example.core.character.Character;
import org.example.gameplay.combat.CombatEvent;
import org.example.gameplay.combat.CombatEventType;

import java.util.List;

public abstract class Action {

    protected Character source;
    protected Character target;
    

    public Action(Character source, Character target) {
        this.source = source;
        this.target = target;
    }

    public Character getSource() {
        return source;
    }

    public Character getTarget() {
        return target;
    }

    public abstract boolean isDefenseAction();

    

    public abstract int getPriority();


    public abstract List<CombatEvent> execute();

    public abstract List<CombatEventType> getCombatEventTypesList();
    }