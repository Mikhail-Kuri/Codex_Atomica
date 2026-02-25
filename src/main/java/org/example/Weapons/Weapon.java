package org.example.Weapons;

public class Weapon {
    private String name;
    private int power;
    private String damageType; // "Slashing", "Crushing", "Radioactive"

    public Weapon(String name, int power, String damageType) {
        this.name = name;
        this.power = power;
        this.damageType = damageType;
    }

    public int getPower() { return power; }
    public String getDamageType() { return damageType; }
    public String getName() { return name; }
}