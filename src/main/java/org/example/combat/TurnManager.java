package org.example.combat;


import org.example.Skills.Action;

import java.util.ArrayList;
import java.util.List;

public class TurnManager {

    private final List<Action> plannedActions = new ArrayList<>();

    public void addAction(Action action) {
        plannedActions.add(action);
    }
    public void resolveTurn() {

        System.out.println("\n⚔️ === RESOLUTION DU TOUR ===");

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
