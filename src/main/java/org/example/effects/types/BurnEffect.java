package org.example.effects.types;

import org.example.core.character.Character;
import org.example.effects.StatusEffect;

public class BurnEffect extends StatusEffect {

    private int damagePerTurn;

    public BurnEffect(int damagePerTurn, int duration) {
        super("Burn", duration);
        this.damagePerTurn = damagePerTurn;
    }

    @Override
    public void onTurnEnd(Character target) {
        System.out.println(target.getName() + " subit " + damagePerTurn + " dégâts de feu");

        target.takeDamage(damagePerTurn, null);

        super.onTurnEnd(target); // réduit la durée
    }

    @Override
    public String toString() {
        return "BurnEffect{" +
                "name='" + name + '\'' +
                "damagePerTurn=" + damagePerTurn +
                ", duration=" + duration +
                '}';
    }
}
