package org.example.gameplay.combat;


import org.example.Skills.Actions.Action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.example.core.character.Character;


public class TurnManager {

    private final List<Action> plannedActions = new ArrayList<>();

    public void addAction(Action action) {
        plannedActions.add(action);
    }

    public void resolveTurn() {

        System.out.println("\n⚔️ === RESOLUTION DU TOUR ===");

        Map<Character, Integer> speedCache = buildSpeedCache();
        sortActions(speedCache);
        executeActions();
        cleanup();

        System.out.println("⚔️ === FIN DU TOUR ===\n");
    }

    private Map<Character, Integer> buildSpeedCache() {
        Map<Character, Integer> speedCache = new HashMap<>();

        for (Action action : plannedActions) {
            Character source = action.getSource();
            speedCache.putIfAbsent(source, source.rollSpeed());
        }

        return speedCache;
    }

    private void sortActions(Map<Character, Integer> speedCache) {

        plannedActions.sort((a, b) -> {

            int priorityCompare = Integer.compare(
                    b.getPriority(),
                    a.getPriority()
            );

            if (priorityCompare != 0) return priorityCompare;

            int speedA = speedCache.get(a.getSource());
            int speedB = speedCache.get(b.getSource());

            return Integer.compare(speedB, speedA);
        });
    }

    private void cleanup() {
        plannedActions.clear();
    }


    private void executeActions() {
        for (Action action : plannedActions) {
            List<CombatEvent> events = action.execute();
            processEvents(events, action.getSource(), action.getTarget());
        }
    }

    private void processEvents(List<CombatEvent> events, Character source, Character target) {

        for (CombatEvent event : events) {

            switch (event.getType()) {

                case DAMAGE_DEALT -> {
                    // optionnel: logs côté source
                }

                case DAMAGE_RECEIVED -> {
                    target.takeDamage(event.getValue(), source);
                    target.getMentalState()
                            .onEvent(event.getType(), target, source);
                }

                case ENEMY_DEFEATED -> {
                    target.die();
                    target.getMentalState()
                            .onEvent(event.getType(), target, source);
                }

                case DEFENSE_PREPARED -> {
                    // futur: buffs, shields, etc.
                }

                default -> {
                    // events non gérés
                }
            }
        }
    }


    public List<Action> getPlannedActions() {
        return plannedActions;
    }

}
