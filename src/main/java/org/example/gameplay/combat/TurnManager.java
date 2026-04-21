package org.example.gameplay.combat;


import org.example.Skills.Actions.Action;

import java.util.*;

import org.example.core.character.Character;

public class TurnManager {

    private final List<Action> plannedActions = new ArrayList<>();
    private final CombatEngine combatEngine;

    public TurnManager(CombatEngine combatEngine) {
        this.combatEngine = combatEngine;
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

        Map<Character, Integer> speedCache = buildSpeedCache();
        sortActions(speedCache);

        for (Action action : plannedActions) {
            combatEngine.execute(action);
        }

        plannedActions.clear();

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

            return Integer.compare(
                    speedCache.get(b.getSource()),
                    speedCache.get(a.getSource())
            );
        });
    }

    public List<Action> getPlannedActions() {
        return plannedActions;
    }
}