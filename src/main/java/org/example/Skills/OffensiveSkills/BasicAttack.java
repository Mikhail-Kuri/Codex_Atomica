package org.example.Skills.OffensiveSkills;

import org.example.Skills.Scaling.ScalingType;
import org.example.Skills.Targeting.TargetType;
import org.example.core.character.Character;

import java.util.List;


public class BasicAttack extends OffensiveSkill {

    public BasicAttack() {
        super("Attaque de base", TargetType.ENEMY, List.of(ScalingType.STRENGTH, ScalingType.WEAPON));
    }


    @Override
    public void execute(Character source, Character target) {

        if (source == null || target == null) return;
        if (!source.isAlive() || !target.isAlive()) return;

        if (isValidTarget(source, target)) {
            System.out.println("❌ Cible invalide pour " + name);
            return;
        }

        int damage = calculateDamage(source, target);

        System.out.println(source.getName() + " attaque " + target.getName()
                + " pour " + damage + " dégâts");

        target.takeDamage(damage, source);

    }


}

