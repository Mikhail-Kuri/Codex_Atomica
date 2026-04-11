package org.example.Skills.DefensiveSkills;

import org.example.Skills.Action;
import org.example.core.Character;

public class GuardAction extends Action {

        public GuardAction(Character source, Character target) {
            super(source, target);
        }
    @Override
    public void execute() {
        source.prepareDefense();
    }
}
