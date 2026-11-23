package org.example.Skills.Scaling;

public enum RangeScaling {
    MELEE("Melee"),
    SHORT("Short"),
    MEDIUM("Medium"),
    LONG("Long");

    private final String displayName;

    RangeScaling(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
