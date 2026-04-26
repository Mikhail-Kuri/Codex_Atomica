package org.example.Skills.Scaling;

public enum RangeScaling {
    MELEE("Mêlée"),
    SHORT("Courte"),
    MEDIUM("Moyenne"),
    LONG("Longue");

    private final String displayName;

    RangeScaling(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
