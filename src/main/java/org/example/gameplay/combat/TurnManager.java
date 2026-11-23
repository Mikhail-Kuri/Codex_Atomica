package org.example.gameplay.combat;


import org.example.Skills.Actions.Action;

import java.util.*;

import org.example.Skills.Actions.OffensiveAction;
import org.example.core.character.Character;
import org.example.effects.StatusEffect;
import org.example.gameplay.log.CombatLog;
import org.example.gameplay.log.ConsoleCombatLog;

public class TurnManager {

    private final List<Action> plannedActions = new ArrayList<>();
    private final CombatEngine combatEngine;
    private final ActionOrderResolver resolver;
    private final CombatLog log;
    private int turnNumber = 1;
    private boolean combatEnded = false;


    public TurnManager(CombatEngine combatEngine, ActionOrderResolver resolver, CombatLog log) {
        this.combatEngine = combatEngine;
        this.resolver = resolver;
        this.log = log;
    }

    /** Combat qui écrit dans ce journal. */
    public TurnManager(CombatLog log) {
        this(new CombatEngine(new EventSystem(log)), new ActionOrderResolver(), log);
    }

    /** Combat qui écrit dans la console, détail des calculs compris (mode développeur). */
    public TurnManager() {
        this(new ConsoleCombatLog(true));
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
            log.info(source.getName() + " has no actions left this turn!");
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
            log.info(source.getName() + " is already defending!");
            return false;
        }

        plannedActions.add(action);
        return true;
    }

    public void resolveCurrentTurn() {

        log.info("\n⚔️ === TURN " + turnNumber + " ===");

        List<Action> ordered = resolver.sort(plannedActions);

        for (Action action : ordered) {
            combatEngine.execute(action);
        }

        plannedActions.clear();
        turnNumber++;
    }

    public void runCombatLoop(List<Character> characters) {

        while (!combatEnded) {

            autoPlanActions(characters);

            resolveCurrentTurn();

            endTurn(characters);

            log.info(hpSummary(characters));

            checkEndCondition(characters);
        }

        log.info("🏁 Combat over!");
    }

    /** Une ligne avec les PV de chacun, ex. « Paladin 71/120 PV · Gnoll 8/50 PV ». */
    private static String hpSummary(List<Character> characters) {
        return String.join(" · ", characters.stream()
                .map(c -> c.getName() + " " + c.getCurrentHP() + "/"
                        + c.getAttributes().getVitality() + " HP")
                .toList());
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
                boolean wasAlive = character.isAlive();
                int before = character.getCurrentHP();

                effect.onTurnEnd(character);

                int lost = before - character.getCurrentHP();
                if (lost > 0) {
                    log.info(character.getName() + " takes " + lost + " damage from "
                            + effect.getName() + " (" + character.getCurrentHP() + "/"
                            + character.getAttributes().getVitality() + " HP)");
                }
                if (wasAlive && !character.isAlive()) {
                    log.info("💀 " + character.getName() + " has fallen.");
                }
            }

            for (StatusEffect effect : effects) {
                if (effect.isExpired()) {
                    log.debug(effect.getName() + " wears off " + character.getName());
                }
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
