package org.example.gameplay.combat;


import org.example.Skills.Actions.Action;

import java.util.*;

import org.example.Skills.Actions.OffensiveAction;
import org.example.core.character.Character;
import org.example.effects.StatusEffect;

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

    /**
     * Planifie une action pour ce tour.
     * Refusée si le personnage a épuisé son budget d'actions
     * ({@link Character#getActionsPerTurn()}), ou s'il défend déjà ce tour.
     *
     * @return true si l'action a été acceptée
     */
    public boolean addAction(Action action) {

        Character source = action.getSource();

        long alreadyPlanned = plannedActions.stream()
                .filter(a -> a.getSource().equals(source))
                .count();

        if (alreadyPlanned >= source.getActionsPerTurn()) {
            System.out.println(source.getName() + " n'a plus d'action ce tour !");
            return false;
        }

        boolean alreadyDefending =
                source.isDefending() ||
                        plannedActions.stream()
                                .anyMatch(a ->
                                        a.getSource().equals(source)
                                                && a.isDefenseAction()
                                );

        if (action.isDefenseAction() && alreadyDefending) {
            System.out.println(source.getName() + " défend déjà !");
            return false;
        }

        plannedActions.add(action);
        return true;
    }

    public void resolveCurrentTurn() {

        System.out.println("\n⚔️ === TOUR " + turnNumber + " ===");

        List<Action> ordered = resolver.sort(plannedActions);

        for (Action action : ordered) {
            combatEngine.execute(action);
        }

        //Aller chercher les stastus a appliquer a la fin du tour et les appliquer
        Set<Character> involvedCharacters = new HashSet<>();
        for (Action action : ordered) {
            involvedCharacters.add(action.getSource());
            if (action.getTarget() != null) {
                involvedCharacters.add(action.getTarget());
            }
        }


        System.out.println("\n🔁 Application des effets de fin de tour :");
        System.out.println(
                involvedCharacters.stream()
                        .flatMap(c -> c.getState().getStatusEffects().stream())
                        .toList()
        );
        plannedActions.clear();

        System.out.println("⚔️ === FIN DU TOUR " + turnNumber + " ===\n");

        turnNumber++;
    }

    public void runCombatLoop(List<Character> characters) {

        while (!combatEnded) {

            System.out.println(">>> Préparation des actions (Tour " + turnNumber + ")");

            autoPlanActions(characters);

            resolveCurrentTurn();

            endTurn(characters);

            printStats(characters);

            checkEndCondition(characters);
        }

        System.out.println("🏁 Combat terminé !");
    }

    /**
     * Termine le tour : applique les effets de statut, retire ceux qui ont expiré,
     * et met fin aux défenses (une défense ne dure qu'un tour).
     * À appeler après {@link #resolveCurrentTurn()}, avec tous les personnages du combat.
     */
    public void endTurn(List<Character> characters) {
        applyEndOfTurnEffects(characters);

        for (Character character : characters) {
            character.endDefense();
        }
    }

    /** Déclenche onTurnEnd de chaque effet de statut, puis retire les effets expirés. */
    private void applyEndOfTurnEffects(List<Character> characters) {
        for (Character character : characters) {
            List<StatusEffect> effects = character.getState().getStatusEffects();

            // Copie : un effet peut modifier la liste pendant son onTurnEnd
            for (StatusEffect effect : new ArrayList<>(effects)) {
                effect.onTurnEnd(character);
            }

            effects.removeIf(StatusEffect::isExpired);
        }
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
