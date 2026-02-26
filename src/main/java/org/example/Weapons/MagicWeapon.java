package org.example.Weapons;

public class MagicWeapon extends Weapon{

    private float manaScaling;

    public MagicWeapon(String name, int basePower, DamageType damageType, float manaScaling) {
        super(name, basePower, damageType);
        this.manaScaling = manaScaling;
    }

    @Override
    public int calculateDamage() {
        return (int)(basePower * manaScaling);
    }
}
