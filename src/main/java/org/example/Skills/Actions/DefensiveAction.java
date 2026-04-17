package org.example.Skills.Actions;

import org.example.core.character.Character;

public class DefensiveAction extends Action {

    protected final int priority = 100;

        public DefensiveAction(Character source, Character target) {
            super(source, target);
        }
    @Override
    public void execute() {
        source.prepareDefense();
    }

    public int getPriority() {
        return priority;
    }
}
