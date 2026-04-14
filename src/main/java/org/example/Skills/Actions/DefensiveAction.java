package org.example.Skills.Actions;

import org.example.core.Character;

public class DefensiveAction extends Action {

        public DefensiveAction(Character source, Character target) {
            super(source, target);
        }
    @Override
    public void execute() {
        source.prepareDefense();
    }
}
