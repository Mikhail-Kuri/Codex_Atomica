package org.example.core.character.Attributes.arsenal;

import org.example.Skills.DefensiveSkills.DefensiveSkill;
import org.example.Skills.OffensiveSkills.OffensiveSkill;

import java.util.List;

public class CharacterSkills {
    private List<OffensiveSkill> offensiveSkills;
    private DefensiveSkill currentDefense;

    public CharacterSkills (List<OffensiveSkill> initialOffensives, DefensiveSkill initialDefensive) {
        if (initialOffensives == null) throw new IllegalArgumentException("Un personnage doit avoir au moins une skill offensive !");
        if (initialDefensive == null) throw new IllegalArgumentException("Un personnage doit avoir une skill de défense !");

        this.offensiveSkills = initialOffensives;
        this.currentDefense = initialDefensive;
    }

    public List<OffensiveSkill> getOffensiveSkills() {
        return offensiveSkills;
    }

    public DefensiveSkill getCurrentDefense() {
        return currentDefense;
    }


}
