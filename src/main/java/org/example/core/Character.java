package org.example.core;

import org.example.Skills.Actions.Action;
import org.example.Skills.DefensiveSkills.DefensiveSkill;
import org.example.Skills.OffensiveSkills.BasicAttack;
import org.example.Skills.OffensiveSkills.OffensiveSkill;
import org.example.Skills.OffensiveSkills.SelfAttack;
import org.example.Weapons.Weapon;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Character {
    private String name;
    private int currentHP;
    private Attributes stats;
    private Weapon equippedWeapon;
    private List<OffensiveSkill> offensiveSkills = new ArrayList<>();
    private DefensiveSkill currentDefense;
    private boolean isDefending = false;
    private boolean isAlive = true;

    public Character(String name, Attributes stats, Weapon weapon, DefensiveSkill defense) {
        if (weapon == null) throw new IllegalArgumentException("Un personnage doit avoir une arme !");
        if (defense == null) throw new IllegalArgumentException("Un personnage doit avoir une skill de défense !");

        this.name = name;
        this.stats = stats;
        this.currentHP = stats.vitality;
        this.equippedWeapon = weapon;
        this.currentDefense = defense;

        this.offensiveSkills.add(new BasicAttack());


    }

    public void setWeapon(Weapon newWeapon) {
        this.equippedWeapon = newWeapon;
    }

    public Weapon getEquippedWeapon() { return equippedWeapon; }

    public void prepareDefense() {
        this.isDefending = true;

        System.out.println(name + " se prépare avec " + getCurrentDefense().getName());
    }

    public void takeDamage(int amount, Character attacker) {
        if (!isAlive) {
            System.out.println(this.name + " est déjà hors de combat !");
            return;
        }

        int finalDamage;

        if (isDefending && currentDefense != null) {
            finalDamage = currentDefense.onDamageTaken(amount, this, attacker);
            isDefending = false;
        } else {
            finalDamage = Math.max(0, amount - this.stats.vigor);
        }

        this.currentHP -= finalDamage;

        if (this.currentHP <= 0) {
            this.currentHP = 0;
            this.isAlive = false;
            System.out.println("💀 " + this.name + " a succombé à ses blessures !");
        } else {
            System.out.println(this.name + " HP restants : " + this.currentHP);
        }
    }

    public void printStats() {
        System.out.println("\n===== STATS DE " + name + " =====");

        System.out.println("HP : " + currentHP + "/" + stats.vitality);
        System.out.println("Alive : " + isAlive);

        System.out.println("Weapon : " +
                (equippedWeapon != null ? equippedWeapon.getName() : "Aucune"));

        System.out.println("Defense : " +
                (currentDefense != null ? currentDefense.getName() : "Aucune"));

        System.out.println("Vigor : " + stats.vigor);
        System.out.println("Resonance : " + stats.resonance);

        System.out.println("================================\n");
    }

    public Attributes getAttributes() { return stats; }
    public String getName() { return name; }
    public int getCurrentHP() { return currentHP; }
    public boolean isAlive() {
        return isAlive;
    }
    public DefensiveSkill getCurrentDefense() {
        return currentDefense;
    }

    public List<OffensiveSkill> getOffensiveSkills() {
        return offensiveSkills;
    }

    public OffensiveSkill getDefaultOffensiveSkill() {
        return offensiveSkills.stream()
                .filter(skill -> skill instanceof BasicAttack)
                .findFirst()
                .orElse(null);
    }
}