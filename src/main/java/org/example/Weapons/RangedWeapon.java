package org.example.Weapons;

public class RangedWeapon extends Weapon{
    private int ammo;

    public RangedWeapon(String name, int basePower, DamageType damageType, int ammo) {
        super(name, basePower, damageType);
        this.ammo = ammo;
    }

    @Override
    public int calculateDamage() {
        if (ammo <= 0) {
            System.out.println("Plus de munitions !");
            return 0;
        }
        ammo--;
        return basePower;
    }
}
