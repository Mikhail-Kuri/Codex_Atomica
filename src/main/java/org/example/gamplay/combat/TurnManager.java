package org.example.gamplay.combat;


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

        // 1. Cache des speeds (UNE SEULE fois)
        Map<Character, Integer> speedCache = new HashMap<>();

        for (Action action : plannedActions) {
            Character source = action.getSource();
            speedCache.putIfAbsent(source, source.rollSpeed());
        }

        // 2. Tri propre (priority -> speed)
        plannedActions.sort((a, b) -> {

            int priorityCompare = Integer.compare(
                    b.getPriority(),
                    a.getPriority()
            );

            if (priorityCompare != 0) {
                return priorityCompare;
            }

            int speedA = speedCache.get(a.getSource());
            int speedB = speedCache.get(b.getSource());

            return Integer.compare(speedB, speedA);
        });

        // 3. Execution
        for (Action action : plannedActions) {
            action.execute();
        }

        // 4. Cleanup
        plannedActions.clear();

        System.out.println("⚔️ === FIN DU TOUR ===\n");
    }

    public List<Action> getPlannedActions() {
        return plannedActions;
    }

}
