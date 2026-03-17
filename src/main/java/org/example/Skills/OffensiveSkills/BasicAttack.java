package org.example.Skills.OffensiveSkills;

import org.example.core.Character;
import org.example.Skills.Action;
import org.example.Weapons.Weapon;

public class BasicAttack extends Action {
    public BasicAttack() {
        super("Attaque de Base");
    }

    @Override
    public void execute(Character attacker, Character target) {
        Weapon w = attacker.getEquippedWeapon();

        float rawPower = w.getBasePower() * attacker.getAttributes().resonance;
        int finalDamage = Math.max(0, (int)rawPower - target.getAttributes().vigor);

        System.out.println(attacker.getName() + " utilise " + w.getName() + " (" + w.getDamageType() + ")");
        System.out.println("Dégâts : " + finalDamage + " sur " + target.getName());
        target.takeDamage(finalDamage,attacker);
    }
}

