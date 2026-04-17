package org.example.core.character.arsenal;

import org.example.Weapons.Weapon;

public class CharacterEquipment {

    private Weapon equippedWeapon;

    public CharacterEquipment (Weapon initialWeapon) {
        this.equippedWeapon = initialWeapon;
    }

    public Weapon getEquippedWeapon() {
        return equippedWeapon;
    }

    public void equipWeapon(Weapon weapon) {
        this.equippedWeapon = weapon;
    }

}