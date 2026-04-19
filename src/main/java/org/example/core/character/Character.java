package org.example.core.character;

import org.example.Skills.DefensiveSkills.DefensiveSkill;
import org.example.Skills.OffensiveSkills.BasicAttack;
import org.example.Skills.OffensiveSkills.OffensiveSkill;
import org.example.Weapons.Weapon;
import org.example.core.character.Attributes.*;
import org.example.core.character.Attributes.arsenal.*;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class Character {

    private final CharacterState state;
    private final CharacterProfile profile;
    private final CharacterAttributes stats;
    private final CharacterEquipment equipment;
    private final CharacterSkills skills;

    public Character(CharacterState state,
                     CharacterProfile profile,
                     CharacterAttributes stats,
                     CharacterEquipment equipment,
                     CharacterSkills skills) {

        this.state = state;
        this.profile = profile;
        this.stats = stats;
        this.equipment = equipment;
        this.skills = skills;
        this.state.initFromStats(stats);
    }

    // =====================
    // BASIC INFO
    // =====================

    public String getName() {
        return profile.getName();
    }

    public boolean isAlive() {
        return state.isAlive();
    }

    public int getCurrentHP() {
        return state.getCurrentHP();
    }

    public CharacterAttributes getAttributes() {
        return stats;
    }

    public Weapon getEquippedWeapon() {
        return equipment.getEquippedWeapon();
    }

    // =====================
    // COMBAT
    // =====================

    public void takeDamage(int amount, Character attacker) {

        if (!isAlive()) {
            System.out.println(getName() + " est déjà hors de combat !");
            return;
        }

        int finalDamage = computeFinalDamage(amount, attacker);

        System.out.println(getName() + " subit " + finalDamage +
                " dégâts de la part de " + attacker.getName());

        state.reduceHP(finalDamage);

        if (!isAlive()) {
            System.out.println("💀 " + getName() + " a succombé à ses blessures !");
        } else {
            System.out.println(getName() + " HP restants : " + getCurrentHP());
        }
    }

    private int computeFinalDamage(int amount, Character attacker) {

        if (state.isDefending() && getCurrentDefense() != null) {
            int dmg = getCurrentDefense().onDamageTaken(amount, this, attacker);
            state.setDefending(false);
            return dmg;
        }

        return Math.max(0, amount - stats.vigor);
    }

    // =====================
    // DEFENSE
    // =====================

    public void prepareDefense() {
        state.setDefending(true);
        System.out.println(getName() + " se prépare avec " + getCurrentDefense().getName());
    }

    public boolean isDefending() {
        return state.isDefending();
    }

    public DefensiveSkill getCurrentDefense() {
        return skills.getCurrentDefense();
    }

    // =====================
    // SKILLS
    // =====================

    public List<OffensiveSkill> getOffensiveSkills() {
        return skills.getOffensiveSkills();
    }

    public OffensiveSkill getDefaultOffensiveSkill() {
        return skills.getOffensiveSkills().stream()
                .filter(skill -> skill instanceof BasicAttack)
                .findFirst()
                .orElse(null);
    }

    // =====================
    // EQUIPMENT
    // =====================

    public void setWeapon(Weapon weapon) {
        equipment.equipWeapon(weapon);
    }

    // =====================
    // UTILS
    // =====================

    public int rollSpeed() {
        int min = stats.minSpeed;
        int max = stats.maxSpeed;

        if (min > max) {
            throw new IllegalStateException("Invalid speed: min > max");
        }

        return (min == max)
                ? min
                : ThreadLocalRandom.current().nextInt(min, max + 1);
    }

    public void printStats() {
        System.out.println("\n===== STATS DE " + getName() + " =====");

        System.out.println("HP : " + getCurrentHP() + "/" + stats.vitality);
        System.out.println("Alive : " + isAlive());

        System.out.println("Weapon : " +
                (getEquippedWeapon() != null ? getEquippedWeapon().getName() : "Aucune"));

        System.out.println("Defense : " +
                (getCurrentDefense() != null ? getCurrentDefense().getName() : "Aucune"));

        System.out.println("Vigor : " + stats.vigor);
        System.out.println("Resonance : " + stats.resonance);

        System.out.println("================================\n");
    }
}