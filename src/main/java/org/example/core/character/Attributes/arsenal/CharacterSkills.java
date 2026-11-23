package org.example.core.character.Attributes.arsenal;

import org.example.Skills.DefensiveSkills.DefensiveSkill;
import org.example.Skills.OffensiveSkills.OffensiveSkill;

import java.util.List;

public class CharacterSkills {
    private List<OffensiveSkill> offensiveSkills;
    private DefensiveSkill currentDefense;

    public CharacterSkills (List<OffensiveSkill> initialOffensives, DefensiveSkill initialDefensive) {
        if (initialOffensives == null) throw new IllegalArgumentException("A character needs at least one offensive skill!");
        if (initialDefensive == null) throw new IllegalArgumentException("A character needs a defensive skill!");

        this.offensiveSkills = initialOffensives;
        this.currentDefense = initialDefensive;
    }

    public List<OffensiveSkill> getOffensiveSkills() {
        return offensiveSkills;
    }

    public DefensiveSkill getCurrentDefense() {
        return currentDefense;
    }

    public void setCurrentDefense(DefensiveSkill defensiveSkill){
        this.currentDefense = defensiveSkill;
    }

}
