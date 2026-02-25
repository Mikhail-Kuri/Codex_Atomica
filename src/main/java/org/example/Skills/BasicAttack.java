package org.example.Skills;

import org.example.Character;

public class BasicAttack extends Action{

    public BasicAttack(String name, int basePower) {
        super(name, basePower);
    }



    @Override
    public void execute(Character attacker, Character target) {
        float power = this.basePower * attacker.getAttributes().resonance;
        int damage = Math.max(0, (int)power - target.getAttributes().vigor);

        System.out.println(attacker.getName() + " lance " + this.name + " !");
        System.out.println("Resultat : " + damage + " dégâts sur " + target.getName());

        target.takeDamage(damage);
    }
}
