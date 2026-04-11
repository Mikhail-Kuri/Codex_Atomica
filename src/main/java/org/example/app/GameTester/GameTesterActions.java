package org.example.app.GameTester;

import org.example.Skills.DefensiveSkills.Counter;
import org.example.Skills.DefensiveSkills.Evade;
import org.example.Skills.DefensiveSkills.Guard;
import org.example.Skills.OffensiveSkills.BasicAttack;
import org.example.Weapons.DamageType;
import org.example.Weapons.Weapon;
import org.example.Weapons.WeaponRange;
import org.example.combat.TurnManager;
import org.example.core.Attributes;
import org.example.core.Character;

import java.util.Set;

public class GameTesterActions {

    private static final Attributes PALADIN_STATS =
            new Attributes(100, 10, 5, 5, 10, 1.5f, 0, 0);

    private static final Attributes ENEMY_STATS =
            new Attributes(50, 2, 2, 2, 2, 1, 0, 0);

    private static final Attributes SCOUT_STATS =
            new Attributes(80, 4, 15, 5, 8, 1.0f, 10, 0);

    private static final Attributes MUTANT_STATS =
            new Attributes(100, 5, 8, 10, 10, 2.0f, 5, 0);

    // 🔹 ARMES
    private static final Weapon SWORD =
            new Weapon("Épée en fer", 35, Set.of(DamageType.PHYSICAL), WeaponRange.MELEE, null);

    private static final Weapon RUSTY_SWORD =
            new Weapon("Épée en Fer-Rouille", 25, Set.of(DamageType.SLASHING), WeaponRange.MELEE, null);

    private static final Weapon PLASMA =
            new Weapon("Fusil à Plasma", 60, Set.of(DamageType.ENERGY), WeaponRange.LONG, null);

    // 🔹 FACTORY METHODS (IMPORTANT)
    private static Character createPaladin() {
        return new Character("Paladin du Noyau", PALADIN_STATS, SWORD, new Guard());
    }

    private static Character createEnemy() {
        return new Character("Gnoll Irradié", ENEMY_STATS, RUSTY_SWORD, new Evade());
    }

    private static Character createScout() {
        return new Character("Éclaireur Mutant", SCOUT_STATS, RUSTY_SWORD, new Evade());
    }

    private static Character createMutant() {
        return new Character("Mutant Électrique", MUTANT_STATS, RUSTY_SWORD, new Counter());
    }

    public static void runTestAttaque1() {

        System.out.println("========== TEST ATTAQUE ==========");

        Character paladin = createPaladin();
        Character enemy = createEnemy();

        TurnManager tm = new TurnManager();

        tm.addAction(new BasicAttack(paladin, enemy));

        tm.resolveTurn();

        System.out.println("========== END ==========\n");
    }

        public static void runAllTests() {
        runTestAttaque1();
//        runTestGarde1();
//        testEsquive1();
//        runTestCounter1();
//        runTestMortality();
    }

        public static void main(String[] args) {
        runAllTests();
    }

}


