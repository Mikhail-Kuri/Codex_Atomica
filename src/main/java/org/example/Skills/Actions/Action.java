package org.example.Skills.Actions;

import org.example.core.Character;

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

    public abstract void execute();
}