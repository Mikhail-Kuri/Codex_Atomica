package org.example.core.character;

import org.example.Skills.DefensiveSkills.DefensiveSkill;
import org.example.Skills.OffensiveSkills.BasicAttack;
import org.example.Skills.OffensiveSkills.OffensiveSkill;
import org.example.Weapons.Weapon;
import org.example.core.character.Attributes.CharacterAttributes;
import org.example.core.character.Attributes.CharacterProfile;
import org.example.core.character.Attributes.CharacterState;
import org.example.core.character.Attributes.arsenal.CharacterEquipment;
import org.example.core.character.Attributes.arsenal.CharacterSkills;
import org.example.gameplay.mental.MentalState;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class Character {
    private CharacterState state;
    private int currentHP;
    private CharacterProfile profile;
    private CharacterAttributes stats;
    private CharacterEquipment equipment;
    private CharacterSkills skills;
    private boolean isDefending = false;
    private boolean isAlive = true;



    public Character(CharacterProfile profile,
                     CharacterAttributes stats,
                     CharacterEquipment equipment,
                     CharacterSkills skills
    ) {
        this.profile = profile;
        this.stats = stats;
        this.currentHP = stats.vitality;
        this.equipment = equipment;
        this.skills = skills;
    }
    public Character(CharacterState state,
                     CharacterProfile profile,
                     CharacterAttributes stats,
                     CharacterEquipment equipment,
                     CharacterSkills skills
    ) {
        this.state = state;
        this.profile = profile;
        this.stats = stats;
        this.equipment = equipment;
        this.skills = skills;

        this.state.initFromStats(stats);

    }


    public void setWeapon(Weapon newWeapon) {
        this.equipment.equipWeapon(newWeapon);
    }

    public Weapon getEquippedWeapon() {
        return this.equipment.getEquippedWeapon();
    }

    public void prepareDefense() {
        this.isDefending = true;
        System.out.println(getName() + " se prépare avec " + getCurrentDefense().getName());
    }

    public CharacterAttributes getAttributes() {
        return stats;
    }

    public String getName() {
        return this.profile.getName();
    }

    public int getCurrentHP() {
        return currentHP;
    }

    public boolean isAlive() {
        return isAlive;
    }


    public void setCurrentHP(int currentHP) {
        this.currentHP = currentHP;
    }

    public CharacterProfile getProfile() {
        return profile;
    }

    public void setProfile(CharacterProfile profile) {
        this.profile = profile;
    }

    public CharacterAttributes getStats() {
        return stats;
    }

    public void setStats(CharacterAttributes stats) {
        this.stats = stats;
    }

    public CharacterEquipment getEquipment() {
        return equipment;
    }

    public void setEquipment(CharacterEquipment equipment) {
        this.equipment = equipment;
    }

    public CharacterSkills getSkills() {
        return skills;
    }

    public void setSkills(CharacterSkills skills) {
        this.skills = skills;
    }

    public boolean isDefending() {
        return isDefending;
    }

    public void setDefending(boolean defending) {
        isDefending = defending;
    }

    public void setAlive(boolean alive) {
        isAlive = alive;
    }

    public DefensiveSkill getCurrentDefense() {
        return this.skills.getCurrentDefense();
    }

    public List<OffensiveSkill> getOffensiveSkills() {
        return this.skills.getOffensiveSkills();
    }

    public OffensiveSkill getDefaultOffensiveSkill() {
        return getOffensiveSkills().stream()
                .filter(skill -> skill instanceof BasicAttack)
                .findFirst()
                .orElse(null);
    }

    public void printStats() {
        System.out.println("\n===== STATS DE " + getName() + " =====");

        System.out.println("HP : " + currentHP + "/" + stats.vitality);
        System.out.println("Alive : " + isAlive);

        System.out.println("Weapon : " +
                (getEquippedWeapon() != null ? getEquippedWeapon().getName() : "Aucune"));

        System.out.println("Defense : " +
                (getCurrentDefense() != null ? getCurrentDefense().getName() : "Aucune"));

        System.out.println("Vigor : " + stats.vigor);
        System.out.println("Resonance : " + stats.resonance);

        System.out.println("================================\n");
    }

    public void takeDamage(int amount, Character attacker) {
        if (!isAlive) {
            System.out.println(this.getName() + " est déjà hors de combat !");
            return;
        }

        int finalDamage;

        if (isDefending && getCurrentDefense() != null) {
            finalDamage = getCurrentDefense().onDamageTaken(amount, this, attacker);
            isDefending = false;
        } else {
            finalDamage = Math.max(0, amount - this.stats.vigor);
        }

        System.out.println(this.getName() + " subit " + finalDamage + " dégâts de la part de " + attacker.getName());

        this.currentHP -= finalDamage;

        if (this.currentHP <= 0) {
            this.currentHP = 0;
            this.isAlive = false;
            System.out.println("💀 " + this.getName() + " a succombé à ses blessures !");
        } else {
            System.out.println(this.getName() + " HP restants : " + this.currentHP);
        }
    }

    public int rollSpeed() {

        int min = stats.minSpeed;
        int max = stats.maxSpeed;

        if (min > max) {
            throw new IllegalStateException("Invalid speed: min > max");
        }

        if (min == max) return min;

        return ThreadLocalRandom.current().nextInt(min, max + 1);
    }


}