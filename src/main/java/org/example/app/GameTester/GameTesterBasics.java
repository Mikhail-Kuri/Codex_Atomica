//package org.example.app.GameTester;
//
//import org.example.Weapons.WeaponRange;
//import org.example.core.Attributes;
//import org.example.Skills.DefensiveSkills.Counter;
//import org.example.Skills.DefensiveSkills.Evade;
//import org.example.Skills.DefensiveSkills.Guard;
//import org.example.Weapons.DamageType;
//import org.example.Weapons.Weapon;
//import org.example.core.Character;
//
//import java.util.Set;
//
//public class GameTesterBasics {
//
//
//    public static void runTestGarde1() {
//        System.out.println("========== TEST GUARD ==========");
//
//        Character paladin = createPaladin();
//        Character enemy = createEnemy();
//
//        paladin.prepareDefense();
//        enemy.performAction(0, paladin);
//
//        System.out.println("========== END ==========\n");
//    }
//
//    public static void testEsquive1() {
//        System.out.println("========== TEST EVADE ==========");
//
//        Character scout = createScout();
//        Character enemy = createEnemy();
//
//        scout.prepareDefense();
//        enemy.performAction(0, scout);
//
//        System.out.println("========== END ==========\n");
//    }
//
//    public static void runTestCounter1() {
//        System.out.println("========== TEST COUNTER ==========");
//
//        Character mutant = createMutant();
//        Character enemy = createEnemy();
//
//        mutant.prepareDefense();
//        enemy.performAction(0, mutant);
//
//        System.out.println("\nHP Mutant : " + mutant.getCurrentHP());
//        System.out.println("HP Ennemi : " + enemy.getCurrentHP());
//
//        System.out.println("========== END ==========\n");
//    }
//
//    public static void runTestMortality() {
//        System.out.println("========== TEST MORTALITY ==========");
//
//        Character fragile = new Character("Fragile",
//                new Attributes(20, 0, 0, 0, 0, 1, 0, 0),
//                RUSTY_SWORD,
//                new Guard());
//
//        Character bourreau = createEnemy();
//
//        bourreau.performAction(0, fragile);
//        bourreau.performAction(0, fragile);
//        fragile.performAction(0, bourreau);
//
//        System.out.println("========== END ==========\n");
//    }
//
//    public static void main(String[] args) {
//        runAllTests();
//    }
//}