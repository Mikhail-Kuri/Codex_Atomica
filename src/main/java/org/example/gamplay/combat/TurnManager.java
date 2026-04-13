package org.example.gamplay.combat;


import org.example.Skills.Actions.Action;

import java.util.ArrayList;
import java.util.List;

public class TurnManager {

    private final List<Action> plannedActions = new ArrayList<>();

    public void addAction(Action action) {
        plannedActions.add(action);
    }

    public void resolveTurn() {

        System.out.println("\n⚔️ === RESOLUTION DU TOUR ===");

        plannedActions.sort((a, b) -> {

            int priorityCompare = Integer.compare(
                    b.getPriority(),
                    a.getPriority()
            );

            if (priorityCompare != 0) {
                return priorityCompare;
            }


            return Integer.compare(
                    b.getSource().rollSpeed(),
                    a.getSource().rollSpeed()
            );
        });

        for (Action action : plannedActions) {
            action.execute();
        }

        plannedActions.clear();

        System.out.println("⚔️ === FIN DU TOUR ===\n");
    }

    public List<Action> getPlannedActions() {
        return plannedActions;
    }

}
