package org.example.Weapons;

public abstract class Weapon {

    protected String name;
    protected int basePower;
    protected DamageType damageType;

    public Weapon(String name, int basePower, DamageType damageType) {
        this.name = name;
        this.basePower = basePower;
        this.damageType = damageType;
    }

    public String getName() {
        return name;
    }

    public DamageType getDamageType() {
        return damageType;
    }

    public int getBasePower() {
        return basePower;
    }

    // Chaque type d'arme calcule ses dégâts différemment
    public abstract int calculateDamage();
}