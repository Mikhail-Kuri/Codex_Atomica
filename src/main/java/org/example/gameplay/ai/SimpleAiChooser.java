package org.example.gameplay.ai;

import org.example.Skills.Actions.Action;
import org.example.Skills.Actions.DefensiveAction;
import org.example.Skills.Actions.OffensiveAction;
import org.example.Skills.OffensiveSkills.OffensiveSkill;
import org.example.Skills.Targeting.TargetType;
import org.example.core.character.Character;
import org.example.gameplay.combat.ActionChooser;

import java.util.List;
import java.util.Random;

/**
 * IA volontairement simple : défend avec une probabilité donnée,
 * sinon utilise une de ses compétences offensives au hasard.
 * Point de départ avant des IA propres à chaque ennemi (rythmes, intentions).
 */
public class SimpleAiChooser implements ActionChooser {

    private final double defendChance;
    private final Random random;

    /**
     * @param defendChance probabilité de défendre à chaque tour, entre 0 et 1
     * @param random       source de hasard (une graine fixe rend l'IA prévisible en test)
     */
    public SimpleAiChooser(double defendChance, Random random) {
        if (defendChance < 0 || defendChance > 1) {
            throw new IllegalArgumentException("defendChance must be between 0 and 1");
        }
        this.defendChance = defendChance;
        this.random = random;
    }

    @Override
    public Action choose(Character self, Character opponent) {
        boolean canDefend = self.getCurrentDefense() != null;

        if (canDefend && random.nextDouble() < defendChance) {
            return new DefensiveAction(self, self, self.getCurrentDefense());
        }

        List<OffensiveSkill> skills = self.getOffensiveSkills();
        OffensiveSkill skill = skills.get(random.nextInt(skills.size()));
        Character target = (skill.getTargetType() == TargetType.SELF) ? self : opponent;

        return new OffensiveAction(self, target, skill);
    }
}
