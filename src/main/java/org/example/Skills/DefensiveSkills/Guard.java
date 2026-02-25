package org.example.Skills.DefensiveSkills;

import org.example.Character;

public class Guard extends DefensiveSkill {

    public Guard() {
        super("Guard");
    }

    @Override
    public int reduceDamage(int rawDamage, Character owner) {
        int effectiveVigor = owner.getAttributes().vigor * 2; // Bonus de 5 points
        int finalDamage = Math.max(0, rawDamage - effectiveVigor);
        System.out.println("[ACTION: GARDE] " + owner.getName() + " s'ancre au sol !");
        System.out.println("Vigueur effective : " + effectiveVigor + " | Dégâts absorbés : " + (rawDamage - finalDamage));

        return finalDamage;
    }
}
