package org.example.Skills.DefensiveSkills;
import org.example.Character;
import java.util.Random;

public class Evade extends DefensiveSkill {
    private Random random = new Random();

    public Evade() {
        super("Esquive Réflexe");
    }

    @Override
    public int reduceDamage(int rawDamage, Character owner) {
        // Logique : Chance de réussite basée sur l'Agilité
        // Formule : Agility * 4 + Luck (ex: 10 AGI + 5 LUCK = 45% de chance)
        int dodgeChance = (owner.getAttributes().agility * 4) + owner.getAttributes().luck;

        int roll = random.nextInt(101);

        if (roll <= dodgeChance) {
            System.out.println("[ACTION: ESQUIVE] " + owner.getName() + " réalise une pirouette parfaite ! (Roll: " + roll + "/" + dodgeChance + ")");
            return 0;
        } else {
            System.out.println("[ACTION: ÉCHEC] " + owner.getName() + " tente d'esquiver mais trébuche ! (Roll: " + roll + "/" + dodgeChance + ")");
            return rawDamage;
        }
    }
}