package org.example.Weapons;

import org.example.Skills.Scaling.DamageSource;
import org.example.Skills.Scaling.DamageType;
import org.example.Skills.Scaling.RangeScaling;

import java.util.Set;

public  class Weapon implements DamageSource {

    protected String name;
    protected int basePower;
    protected Set<DamageType> damageTypes;
    protected RangeScaling range;
    //private String modelPath; // Chemin vers le modèle 3D de l'arme

    public Weapon(String name, int basePower, Set<DamageType> damageTypes, RangeScaling range, String modelPath) {
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

    @Override
    public Set<DamageType> getDamageTypes() {
        return damageTypes;
    }

    public String getName() {
        return name;
    }


    public int getBasePower() {
        return basePower;
    }

    public RangeScaling getRange() {
        return range;
    }

}