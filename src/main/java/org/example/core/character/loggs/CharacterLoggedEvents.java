package org.example.core.character.loggs;

import org.example.Skills.OffensiveSkills.OffensiveSkill;

public class CharacterLoggedEvents {
    private OffensiveSkill currentlyUsedOffensiveSkill;

    public CharacterLoggedEvents() {
    }

    public OffensiveSkill getCurrentlyUsedOffensiveSkill() {
        return currentlyUsedOffensiveSkill;
    }

    public void setCurrentlyUsedOffensiveSkill(OffensiveSkill currentlyUsedOffensiveSkill) {
        this.currentlyUsedOffensiveSkill = currentlyUsedOffensiveSkill;
    }
}
