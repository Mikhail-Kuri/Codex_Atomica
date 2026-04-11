package org.example.Skills.OffensiveSkills;

import org.example.Skills.Targeting.TargetType;
import org.example.Weapons.Weapon;
import org.example.core.Character;


public class BasicAttack extends OffensiveSkill {

    public BasicAttack() {
        super("Attaque de base", TargetType.ENEMY);
    }



    @Override
    public void execute(Character source, Character target) {

        if (source == null || target == null) return;
        if (!source.isAlive() || !target.isAlive()) return;

        if (!isValidTarget(source, target)) {
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

    private boolean isValidTarget(Character source, Character target) {

        if (targetType == TargetType.ENEMY) {
            return source != target;
        }

        if (targetType == TargetType.SELF) {
            return source == target;
        }

        return true;
    }
}

