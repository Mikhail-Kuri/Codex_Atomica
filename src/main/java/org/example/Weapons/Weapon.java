package org.example.Weapons;

import java.util.Set;

public  class Weapon {

    protected String name;
    protected int basePower;
    protected Set<DamageType> damageTypes;
    protected WeaponRange range;
    //private String modelPath; // Chemin vers le modèle 3D de l'arme

    public Weapon(String name, int basePower, Set<DamageType> damageTypes, WeaponRange range,String modelPath) {
        this.name = name;
        this.basePower = basePower;
        this.damageTypes = damageTypes;
        this.range = range;
        /*
        if(modelPath == null || modelPath.isEmpty()) {
            this.modelPath = "models/default_weapon.obj"; // Chemin par défaut
        } else {
            this.modelPath = modelPath;
        }
        */
        
    }

    public String getName() {
        return name;
    }

    public Set<DamageType> getDamageTypes() {
        return damageTypes;
    }

    public int getBasePower() {
        return basePower;
    }

    public WeaponRange getRange() {
        return range;
    }

}