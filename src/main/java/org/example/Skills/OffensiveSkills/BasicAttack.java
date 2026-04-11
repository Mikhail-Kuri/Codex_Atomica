package org.example.Skills.OffensiveSkills;

import org.example.Skills.Action;
import org.example.Weapons.Weapon;
import org.example.core.Character;


public class BasicAttack extends Action {


    public BasicAttack(Character source, Character target) {
        super(source, target);
    }

    @Override
    public void execute() {

        if (source == null || target == null) return;
        if (!source.isAlive() || !target.isAlive()) return;

        Weapon weapon = source.getEquippedWeapon();

        if (weapon == null) return;

        float rawPower = weapon.getBasePower() * source.getAttributes().resonance;
        int damage = Math.max(0, Math.round(rawPower) - target.getAttributes().vigor);

        System.out.println(source.getName() + " attaque " + target.getName()
                + " pour " + damage + " dégâts");

        target.takeDamage(damage, source);
    }
}

