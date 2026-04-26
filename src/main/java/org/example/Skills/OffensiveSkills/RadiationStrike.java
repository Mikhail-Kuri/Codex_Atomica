package org.example.Skills.OffensiveSkills;
import org.example.Skills.Scaling.ScalingType;
import org.example.Skills.Targeting.TargetType;
import org.example.core.character.Character;
import org.example.gameplay.combat.CombatEvent;
import org.example.gameplay.combat.CombatEventType;

import java.util.List;

public class RadiationStrike extends OffensiveSkill {

    public RadiationStrike() {
        super(
                "Radiation Strike",
                TargetType.ENEMY,
                List.of(ScalingType.STRENGTH),
                List.of(
                        CombatEventType.DAMAGE_DEALT
                )
        );
    }

    @Override
    public List<CombatEvent> execute(Character source, Character target) {
        return resolve(source, target);
    }
}