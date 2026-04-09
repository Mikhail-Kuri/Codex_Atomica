package org.example.Weapons;

public enum WeaponRange {
    MELEE("Mêlée"),
    SHORT("Courte"),
    MEDIUM("Moyenne"),
    LONG("Longue");

    private final String displayName;

    WeaponRange(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
