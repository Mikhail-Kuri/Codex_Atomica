package org.example.Skills.OffensiveSkills;


import org.example.Skills.Scaling.ScalingType;
import org.example.Skills.Targeting.TargetType;
import org.example.Weapons.Weapon;
import org.example.core.character.Character;

import java.util.List;


public class SelfAttack extends OffensiveSkill {

    public SelfAttack() {
        super("Attaque de base", TargetType.SELF, List.of(ScalingType.STRENGTH));
    }



    @Override
    public void execute(Character source, Character target) {

        if (source == null || target == null) return;
        if (!source.isAlive() || !target.isAlive()) return;

        if (isValidTarget(source, target)) {
            System.out.println("❌ Cible invalide pour " + name);
            return;
        }

        Weapon weapon = source.getEquippedWeapon();
        if (weapon == null) return;

        float rawPower = weapon.getBasePower() * source.getAttributes().resonance;
        int damage = Math.max(0, Math.round(rawPower) - target.getAttributes().vigor);

        System.out.println(source.getName() + " attaque " + target.getName()
                + " pour " + damage + " dégâts");

        target.takeDamage(damage, source);
    }

}

