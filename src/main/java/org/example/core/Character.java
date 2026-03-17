package org.example.core;

import org.example.Skills.Action;
import org.example.Skills.DefensiveSkills.Counter;
import org.example.Skills.OffensiveSkills.BasicAttack;
import org.example.Skills.DefensiveSkills.DefensiveSkill;
import org.example.Weapons.Weapon;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Character {
    private String name;
    private int currentHP;
    private Attributes stats;
    private Weapon equippedWeapon;
    private List<Action> offensiveActions = new ArrayList<>();
    private DefensiveSkill currentDefense;
    private boolean isDefending = false;
    private boolean isAlive = true;

    public Character(String name, Attributes stats, Weapon startingWeapon) {
        this.name = name;
        this.stats = stats;
        this.currentHP = stats.vitality;
        this.equippedWeapon = startingWeapon;
        this.offensiveActions.add(new BasicAttack());
    }

    public Character(String name, Attributes stats, DefensiveSkill defensiveSkill) {
        this.name = name;
        this.stats = stats;
        this.currentHP = stats.vitality;
        this.currentDefense = defensiveSkill;
        this.equippedWeapon = null;
        this.offensiveActions.add(new BasicAttack());
    }

    public Character(String name, Attributes mStats, Weapon weapon, DefensiveSkill defensiveSkill) {
        this.name = name;
        this.stats = mStats;
        this.currentHP = mStats.vitality;
        this.equippedWeapon = weapon;
        this.currentDefense = defensiveSkill;
        this.offensiveActions.add(new BasicAttack());
    }


    public void setWeapon(Weapon newWeapon) {
        this.equippedWeapon = newWeapon;
    }

    public Weapon getEquippedWeapon() { return equippedWeapon; }

    public void prepareDefense() {
        this.isDefending = true;
        System.out.println(this.name + " se prépare avec : " + this.currentDefense.getName());
    }


    public void performAction(int actionIndex, Character target) {
        if (!this.isAlive) {
            System.out.println("❌ " + this.name + " ne peut pas agir car il est mort.");
            return;
        }

        if (target != null && !target.isAlive()) {
            System.out.println("❌ " + this.name + " essaie d'attaquer un cadavre (" + target.getName() + ").");
            return;
        }

        if (actionIndex >= 0 && actionIndex < offensiveActions.size()) {
            Action selectedAction = offensiveActions.get(actionIndex);

            selectedAction.execute(this, target);
        } else {
            System.out.println(this.name + " ne sait pas quoi faire !");
        }
    }

    public void takeDamage(int amount, Character attacker) {
        if (!isAlive) {
            System.out.println(this.name + " est déjà hors de combat !");
            return;
        }

        int finalDamage;

        if (this.isDefending && currentDefense != null) {
            finalDamage = currentDefense.reduceDamage(amount, this);

            if (currentDefense instanceof Counter && attacker != null) {
                int reflect = (int)(this.stats.resonance * 10);
                attacker.takeDamage(reflect, this);
            }

            this.isDefending = false;
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

    public Attributes getAttributes() { return stats; }
    public String getName() { return name; }
    public int getCurrentHP() { return currentHP; }
    public boolean isAlive() {
        return isAlive;
    }

    public Collection<Action> getOffensiveActions() {
        return offensiveActions;
    }
}