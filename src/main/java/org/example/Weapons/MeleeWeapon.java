package org.example.Weapons;

public class MeleeWeapon extends Weapon{


    private int durability;

    public MeleeWeapon(String name, int basePower, DamageType damageType, int durability) {
        super(name, basePower, damageType);
        this.durability = durability;
    }

    @Override
    public int calculateDamage() {
        return basePower + (durability / 10);
    }
}
