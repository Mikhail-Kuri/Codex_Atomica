package org.example.gameplay.combat;


import org.example.Skills.Actions.Action;

import java.util.*;

import org.example.Skills.Actions.OffensiveAction;
import org.example.core.character.Character;

import static org.example.app.GameTester.PrintStuff.printStats;

public class TurnManager {

    private final List<Action> plannedActions = new ArrayList<>();
    private final CombatEngine combatEngine;
    private final ActionOrderResolver resolver;
    private int turnNumber = 1;
    private boolean combatEnded = false;


    public TurnManager(CombatEngine combatEngine, ActionOrderResolver resolver) {
        this.combatEngine = combatEngine;
        this.resolver = resolver;
    }

    public TurnManager() {
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

    public void resolveCurrentTurn() {

        System.out.println("\n⚔️ === TOUR " + turnNumber + " ===");

        List<Action> ordered = resolver.sort(plannedActions);

        for (Action action : ordered) {
            combatEngine.execute(action);
        }

        plannedActions.clear();

        System.out.println("⚔️ === FIN DU TOUR " + turnNumber + " ===\n");

        turnNumber++;
    }

    public void runCombatLoop(List<Character> characters) {

        while (!combatEnded) {

            System.out.println(">>> Préparation des actions (Tour " + turnNumber + ")");

            autoPlanActions(characters);

            resolveCurrentTurn();

            printStats(characters);

            checkEndCondition(characters);
        }

        System.out.println("🏁 Combat terminé !");
    }

    private void autoPlanActions(List<Character> characters) {

        if (characters.size() < 2) return;

        Character c1 = characters.get(0);
        Character c2 = characters.get(1);

        if (c1.getCurrentHP() > 0) {
            addAction(new OffensiveAction(c1, c2, c1.getDefaultOffensiveSkill()));
        }

        if (c2.getCurrentHP() > 0) {
            addAction(new OffensiveAction(c2, c1, c2.getDefaultOffensiveSkill()));
        }
    }

    private void checkEndCondition(List<Character> characters) {
            long alive = characters.stream()
                    .filter(c -> c.getCurrentHP() > 0)
                    .count();

            if (alive <= 1) {
                combatEnded = true;
            }
        }
}
