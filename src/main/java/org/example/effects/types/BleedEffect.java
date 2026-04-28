package org.example.effects.types;

import org.example.core.character.Character;
import org.example.effects.StatusEffect;

public class BleedEffect extends StatusEffect {

    private int damagePerTurn;

    public BleedEffect(int damagePerTurn, int duration) {
        super("Bleed", duration);
        this.damagePerTurn = damagePerTurn;
    }

    @Override
    public void onTurnEnd(Character target) {
        System.out.println(target.getName() + " subit " + damagePerTurn + " dégâts de bleed");

        target.takeDamage(damagePerTurn, null);

        super.onTurnEnd(target); // réduit la durée
    }
}