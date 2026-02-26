package org.example.Weapons;

public class EnergyWeapon extends Weapon{

    private int radiationLevel;

    public EnergyWeapon(String name, int basePower, DamageType damageType, int radiationLevel) {
        super(name, basePower, damageType);
        this.radiationLevel = radiationLevel;
    }

    @Override
    public int calculateDamage() {
        return basePower + radiationLevel;
    }
}
