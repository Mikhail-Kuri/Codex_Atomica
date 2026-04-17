package org.example.Skills.Actions;

import org.example.core.character.Character;

public abstract class Action {

    protected Character source;
    protected Character target;
    protected int priority;

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

    public int getPriority() {
        return priority;
    }


    public abstract void execute();
}