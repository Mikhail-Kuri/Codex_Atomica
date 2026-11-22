package org.example.app.console;

import org.example.Skills.Actions.Action;
import org.example.Skills.Actions.DefensiveAction;
import org.example.Skills.Actions.OffensiveAction;
import org.example.Skills.OffensiveSkills.OffensiveSkill;
import org.example.Skills.Targeting.TargetType;
import org.example.core.character.Character;
import org.example.effects.StatusEffect;
import org.example.gameplay.combat.ActionChooser;

import java.io.BufferedReader;
import java.io.PrintStream;
import java.util.List;

/**
 * Le joueur choisit son action dans un menu en console.
 * Affiche l'état des deux combattants, liste les compétences puis la défense,
 * et redemande tant que la saisie n'est pas un numéro valide.
 */
public class ConsoleActionChooser implements ActionChooser {

    private final ConsoleInput input;
    private final PrintStream out;

    public ConsoleActionChooser(BufferedReader in, PrintStream out) {
        this.input = new ConsoleInput(in, out);
        this.out = out;
    }

    @Override
    public Action choose(Character self, Character opponent) {
        out.println();
        out.println(statusLine(self));
        out.println(statusLine(opponent));
        out.println();
        out.println("Que fait " + self.getName() + " ?");

        List<OffensiveSkill> skills = self.getOffensiveSkills();
        for (int i = 0; i < skills.size(); i++) {
            out.println("  " + (i + 1) + ". " + skills.get(i).getName());
        }

        int defenseOption = skills.size() + 1;
        boolean canDefend = self.getCurrentDefense() != null;
        if (canDefend) {
            out.println("  " + defenseOption + ". Défendre (" + self.getCurrentDefense().getName() + ")");
        }

        int maxOption = canDefend ? defenseOption : skills.size();
        int choice = input.readChoice(maxOption);

        if (canDefend && choice == defenseOption) {
            return new DefensiveAction(self, self, self.getCurrentDefense());
        }

        OffensiveSkill skill = skills.get(choice - 1);
        Character target = (skill.getTargetType() == TargetType.SELF) ? self : opponent;
        return new OffensiveAction(self, target, skill);
    }

    /** Ex. « Paladin du Noyau  71/120 PV  · mental +3  · Burn (2) ». */
    private static String statusLine(Character c) {
        StringBuilder line = new StringBuilder()
                .append(c.getName())
                .append("  ").append(c.getCurrentHP()).append("/")
                .append(c.getAttributes().getVitality()).append(" PV")
                .append("  · mental ").append(signed(c.getMentalState().getValue()));

        for (StatusEffect effect : c.getState().getStatusEffects()) {
            line.append("  · ").append(effect.getName())
                    .append(" (").append(effect.getDuration()).append(")");
        }
        return line.toString();
    }

    private static String signed(int value) {
        return (value > 0 ? "+" : "") + value;
    }
}
