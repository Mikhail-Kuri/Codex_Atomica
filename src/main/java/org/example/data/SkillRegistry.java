package org.example.data;

import org.example.Skills.DefensiveSkills.Counter;
import org.example.Skills.DefensiveSkills.DefensiveSkill;
import org.example.Skills.DefensiveSkills.Evade;
import org.example.Skills.DefensiveSkills.Guard;
import org.example.Skills.OffensiveSkills.BasicAttack;
import org.example.Skills.OffensiveSkills.OffensiveSkill;
import org.example.Skills.OffensiveSkills.RadiationStrike;
import org.example.Skills.OffensiveSkills.SelfAttack;

import java.util.Map;
import java.util.Random;
import java.util.Set;

/**
 * Compétences connues, par identifiant. Les données les citent par id.
 * Étape de transition : les compétences sont encore des classes Java ;
 * elles passeront en données (skills.json) plus tard.
 */
public class SkillRegistry {

    private final Map<String, OffensiveSkill> offensive = Map.of(
            "attack", new BasicAttack(),
            "radiation_strike", new RadiationStrike(),
            "sacrificial_strike", new SelfAttack()
    );

    private final Map<String, DefensiveSkill> defensive;

    /** @param tuning réglages des formules de combat (Esquive…) */
    public SkillRegistry(GameTuning tuning) {
        this.defensive = Map.of(
                "guard", new Guard(),
                "evade", new Evade(tuning.combat().evade(), new Random()),
                "counter", new Counter()
        );
    }

    public boolean hasOffensive(String id) {
        return offensive.containsKey(id);
    }

    public boolean hasDefensive(String id) {
        return defensive.containsKey(id);
    }

    /** @throws IllegalArgumentException si l'id est inconnu */
    public OffensiveSkill offensive(String id) {
        OffensiveSkill skill = offensive.get(id);
        if (skill == null) throw new IllegalArgumentException("Unknown offensive skill: " + id);
        return skill;
    }

    /** @throws IllegalArgumentException si l'id est inconnu */
    public DefensiveSkill defensive(String id) {
        DefensiveSkill skill = defensive.get(id);
        if (skill == null) throw new IllegalArgumentException("Unknown defensive skill: " + id);
        return skill;
    }

    public Set<String> offensiveIds() {
        return offensive.keySet();
    }

    public Set<String> defensiveIds() {
        return defensive.keySet();
    }
}
