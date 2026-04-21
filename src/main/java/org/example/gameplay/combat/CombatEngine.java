package org.example.gameplay.combat;

import org.example.Skills.Actions.Action;

import java.util.List;

public class CombatEngine {

    private final EventSystem eventSystem;

    public CombatEngine(EventSystem eventSystem) {
        this.eventSystem = eventSystem;
    }

    public void execute(Action action) {

        List<CombatEvent> events = action.execute();

        eventSystem.process(
                events,
                action.getSource(),
                action.getTarget()
        );
    }
}