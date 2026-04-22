package org.example.gameplay.combat;


import org.example.Skills.Actions.Action;

import java.util.*;

import org.example.core.character.Character;

public class TurnManager {

    private final List<Action> plannedActions = new ArrayList<>();
    private final CombatEngine combatEngine;
    private final ActionOrderResolver resolver;

    public TurnManager(CombatEngine combatEngine, ActionOrderResolver resolver) {
        this.combatEngine = combatEngine;
        this.resolver = resolver;
    }

    public TurnManager(){
        this.combatEngine = new CombatEngine(new EventSystem());
        this.resolver = new ActionOrderResolver();
    }

    public void addAction(Action action) {

        Character source = action.getSource();

        boolean alreadyDefending =
                source.isDefending() ||
                        plannedActions.stream()
                                .anyMatch(a ->
                                        a.getSource().equals(source)
                                                && a.isDefenseAction()
                                );

        if (action.isDefenseAction() && alreadyDefending) {
            System.out.println(source.getName() + " défend déjà !");
            return;
        }

        plannedActions.add(action);
    }

    public void resolveTurn() {

        System.out.println("\n⚔️ === RESOLUTION DU TOUR ===");

        List<Action> ordered = resolver.sort(plannedActions);

        for (Action action : ordered) {
            combatEngine.execute(action);
        }

        plannedActions.clear();

        System.out.println("⚔️ === FIN DU TOUR ===\n");
    }
}