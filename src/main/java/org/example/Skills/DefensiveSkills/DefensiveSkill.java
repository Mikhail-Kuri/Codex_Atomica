package org.example.Skills.DefensiveSkills;

import org.example.core.character.Character;
import org.example.gameplay.combat.CombatEvent;
import org.example.gameplay.combat.CombatEventType;

import java.util.ArrayList;
import java.util.List;

public abstract class DefensiveSkill {
    protected String name;

    public DefensiveSkill(String name) {
        this.name = name;
    }

    public abstract int onDamageTaken(int rawDamage, Character owner, Character attacker);

    public List<CombatEvent> resolve(Character source) {

        List<CombatEvent> events = new ArrayList<>();

//        if (source == null || target == null) return events;
//        if (!source.isAlive() || !target.isAlive()) return events;
//
//        if (!isValidTarget(source, target)) {
//            System.out.println("❌ Cible invalide pour " + name);
//            return events;
//        }
//
//        int damage = calculateDamage(source, target);

        events.add(new CombatEvent(CombatEventType.DEFENSE_PREPARED, source, source, 0));

        return events;
    }

    public abstract List<CombatEvent> execute(Character source);


    public String getName() {
        return name;
    }


}