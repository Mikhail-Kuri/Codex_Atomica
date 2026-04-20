package org.example.gameplay.combat;


import org.example.Skills.Actions.Action;

import java.util.*;

import org.example.Skills.DefensiveSkills.Counter;
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

    private boolean handleEvent(CombatEvent event,
                                Queue<CombatEvent> queue,
                                boolean counterTriggered,
                                Set<Character> dead) {

        return switch (event.getType()) {

            case DAMAGE_DEALT -> handleDamageDealt(event);

            case DAMAGE_RECEIVED -> handleDamageReceived(event, queue, counterTriggered, dead);

            case ALLY_DEFEATED -> handleAllyDefeated(event);

            case ATTACK_MISSED -> false;
            case ATTACK_BLOCKED -> false;

            case ENEMY_DEFEATED -> handleEnemyDefeated(event);

            case DEFENSE_PREPARED -> handleDefensePrepared(event);

            case TURN_STARTED -> false;
            case TURN_ENDED -> false;
        };
    }

    private boolean handleDamageDealt(CombatEvent event) {

        event.getSource()
                .getMentalState()
                .onEvent(event.getType(), event.getSource(), event.getTarget());

        return false;
    }

    private boolean handleDamageReceived(CombatEvent event,
                                         Queue<CombatEvent> queue,
                                         boolean counterTriggered,
                                         Set<Character> dead) {

        Character defender = event.getSource();
        Character attacker = event.getTarget();

        int beforeHP = defender.getCurrentHP();

        defender.takeDamage(event.getValue(), attacker);

        int afterHP = defender.getCurrentHP();

        if (beforeHP == afterHP) return counterTriggered;

        // 💡 CHECK DEATH ICI TOUJOURS
        checkDeathAndQueueEvents(queue, defender, attacker, beforeHP, afterHP,dead);

        // si mort → rien d'autre
        if (afterHP <= 0) return counterTriggered;

        if (isCounterTriggered(defender, counterTriggered)) {
            triggerCounter(queue, attacker, defender);
            return true;
        }

        defender.getMentalState()
                .onEvent(event.getType(), defender, attacker);

        return counterTriggered;
    }

    private boolean isDead(Character c, Set<Character> dead) {
        return dead.contains(c) || c.getCurrentHP() <= 0;
    }

    private boolean isCounterTriggered(Character defender, boolean alreadyTriggered) {
        return defender.getCurrentDefense() instanceof Counter && !alreadyTriggered;
    }

    private void handleDeathEvents(Queue<CombatEvent> queue,
                                   Character defender,
                                   Character attacker) {

        queue.add(new CombatEvent(
                CombatEventType.ALLY_DEFEATED,
                defender,
                attacker,
                0
        ));

        queue.add(new CombatEvent(
                CombatEventType.ENEMY_DEFEATED,
                attacker,
                defender,
                0
        ));
    }

    private void triggerCounter(Queue<CombatEvent> queue,
                                Character attacker,
                                Character defender) {

        queue.add(new CombatEvent(
                CombatEventType.DAMAGE_DEALT,
                defender,
                attacker,
                0
        ));

        queue.add(new CombatEvent(
                CombatEventType.DAMAGE_RECEIVED,
                attacker,
                defender,
                0
        ));

    }

    private boolean handleEnemyDefeated(CombatEvent event) {
        event.getSource()
                .getMentalState()
                .onEvent(event.getType(), event.getSource(), event.getTarget());

        return false;
    }

    private boolean handleAllyDefeated(CombatEvent event) {
        return false; // ou logique future
    }

    private boolean handleDefensePrepared(CombatEvent event) {
        event.getSource()
                .getMentalState()
                .onEvent(event.getType(), event.getSource(), event.getTarget());

        return false;
    }

    private void processEvents(List<CombatEvent> events, Character source, Character target) {

        Queue<CombatEvent> queue = new LinkedList<>(events);
        boolean counterTriggered = false;

        Set<Character> dead = new HashSet<>();

        while (!queue.isEmpty()) {

            CombatEvent event = queue.poll();

            // 💀 skip si mort
            if (isDead(event.getSource(), dead) || isDead(event.getTarget(), dead)) {
                continue;
            }

            counterTriggered = handleEvent(event, queue, counterTriggered, dead);
        }
    }

    private void checkDeathAndQueueEvents(
            Queue<CombatEvent> queue,
            Character defender,
            Character attacker,
            int beforeHP,
            int afterHP,
            Set<Character> deadCharacters
    ) {

        if (!(beforeHP > 0 && afterHP <= 0)) return;

        deadCharacters.add(defender); // 🔥 IMPORTANT

        queue.add(new CombatEvent(
                CombatEventType.ENEMY_DEFEATED,
                attacker,
                defender,
                0
        ));

        queue.add(new CombatEvent(
                CombatEventType.ALLY_DEFEATED,
                defender,
                attacker,
                0
        ));
    }

    public List<Action> getPlannedActions() {
        return plannedActions;
    }

}