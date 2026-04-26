package org.example.Skills.Scaling;

import java.util.Set;

public interface DamageSource {

    Set<DamageType> getDamageTypes();

    int getBasePower();
}
