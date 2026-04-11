package org.example.Skills.DefensiveSkills;
import org.example.core.Character;
import java.util.Random;

public class Evade extends DefensiveSkill {
    private Random random = new Random();

    public Evade() {
        super("Esquive");
    }

    @Override
    public int onDamageTaken(int rawDamage, Character owner, Character attacker) {

        int dodgeChance = Math.min(100,
                (owner.getAttributes().agility * 4) + owner.getAttributes().luck
        );

        int roll = random.nextInt(101);

        if (roll <= dodgeChance) {
            System.out.println("[ACTION: ESQUIVE] " + owner.getName() +
                    " réalise une pirouette parfaite ! (Roll: " + roll + "/" + dodgeChance + ")");
            return 0;
        } else {
            System.out.println("[ACTION: ÉCHEC] " + owner.getName() +
                    " tente d'esquiver mais échoue ! (Roll: " + roll + "/" + dodgeChance + ")");
            return rawDamage;
        }
    }
}