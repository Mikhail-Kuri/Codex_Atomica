package org.example.Skills.DefensiveSkills;

import org.example.core.Character;

public class Guard extends DefensiveSkill {

    public Guard() {
        super("Guard");
    }

    public Guard(String name) {
        super(name);
    }

    @Override
    public int onDamageTaken(int rawDamage, Character owner, Character attacker) {

        int effectiveVigor = owner.getAttributes().vigor * 2;

        int finalDamage = Math.max(0, rawDamage - effectiveVigor);

        System.out.println("[ACTION: GARDE] " + owner.getName() + " s'ancre au sol !");
        System.out.println("Vigueur effective : " + effectiveVigor +
                " | Dégâts absorbés : " + (rawDamage - finalDamage));

        return finalDamage;
    }
}
