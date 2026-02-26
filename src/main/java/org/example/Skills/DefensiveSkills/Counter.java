

package org.example.Skills.DefensiveSkills;
import org.example.Character;

public class Counter extends DefensiveSkill {

    public Counter() {
        super("Contre");
    }

    public Counter (String name) {
        super(name);
    }

    @Override
    public int reduceDamage(int rawDamage, Character owner) {

        int weaponPower = (owner.getEquippedWeapon() != null) ? owner.getEquippedWeapon().getBasePower() : 5;

        int counterDamage = (int) (owner.getAttributes().vigor + (weaponPower * owner.getAttributes().resonance));

        System.out.println("[ACTION: CONTRE] " + owner.getName() + " encaisse et riposte immédiatement !");

        System.out.println("Effet de Contre : Inflige " + counterDamage + " dégâts à l'agresseur !");

        int reducedDamage = Math.max(0, rawDamage - (owner.getAttributes().vigor / 2));

        return reducedDamage;
    }
}