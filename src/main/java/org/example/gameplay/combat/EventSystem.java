package org.example.gameplay.combat;

import java.util.*;

import org.example.Skills.DefensiveSkills.Counter;
import org.example.core.character.Character;

public class EventSystem {

    public void process(List<CombatEvent> events,
                        Character source,
                        Character target) {

        Queue<CombatEvent> queue = new LinkedList<>(events);
        boolean counterTriggered = false;
        Set<Character> dead = new HashSet<>();

        while (!queue.isEmpty()) {

            CombatEvent event = queue.poll();

            if (shouldSkip(event, dead)) continue;

            counterTriggered = handleEvent(event, queue, counterTriggered, dead);
        }
    }

    private boolean shouldSkip(CombatEvent event, Set<Character> dead) {

        boolean isDeathEvent =
                event.getType() == CombatEventType.ENEMY_DEFEATED ||
                        event.getType() == CombatEventType.ALLY_DEFEATED;

        return !isDeathEvent &&
                (isDead(event.getSource(), dead) ||
                        isDead(event.getTarget(), dead));
    }

    private boolean handleEvent(CombatEvent event,
                                Queue<CombatEvent> queue,
                                boolean counterTriggered,
                                Set<Character> dead) {

        return switch (event.getType()) {

            case DAMAGE_DEALT -> handleDamageDealt(event);

            case DAMAGE_RECEIVED -> handleDamageReceived(event, queue, counterTriggered, dead);

            case ENEMY_DEFEATED -> handleEnemyDefeated(event);

            case ALLY_DEFEATED -> handleAllyDefeated(event);

            case DEFENSE_PREPARED -> handleDefensePrepared(event);

            case ATTACK_MISSED, TURN_STARTED, TURN_ENDED, ATTACK_BLOCKED -> false;
        };
    }

    // ---------------- DAMAGE ----------------

    private boolean handleDamageReceived(CombatEvent event,
                                         Queue<CombatEvent> queue,
                                         boolean counterTriggered,
                                         Set<Character> dead) {

        Character defender = event.getSource();
        Character attacker = event.getTarget();

        int before = defender.getCurrentHP();

        defender.takeDamage(event.getValue(), attacker);

        int after = defender.getCurrentHP();

        if (before == after) return counterTriggered;

        checkDeath(queue, defender, attacker, before, after, dead);

        if (after <= 0) return counterTriggered;

        if (isCounter(defender, counterTriggered)) {
            triggerCounter(queue, attacker, defender);
            return true;
        }

        defender.getMentalState()
                .onEvent(event.getType(), defender, attacker);

        return counterTriggered;
    }

    private boolean handleDamageDealt(CombatEvent event) {
        event.getSource()
                .getMentalState()
                .onEvent(event.getType(), event.getSource(), event.getTarget());
        return false;
    }

    // ---------------- DEATH ----------------

    private void checkDeath(Queue<CombatEvent> queue,
                            Character defender,
                            Character attacker,
                            int before,
                            int after,
                            Set<Character> dead) {

        if (!(before > 0 && after <= 0)) return;

        dead.add(defender);

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

    // ---------------- COUNTER ----------------

    private boolean isCounter(Character defender, boolean alreadyTriggered) {
        return defender.getCurrentDefense() instanceof Counter && !alreadyTriggered;
    }

    private void triggerCounter(Queue<CombatEvent> queue,
                                Character attacker,
                                Character defender) {

        int dmg = defender.getState().getPendingCounterDamage();

        queue.add(new CombatEvent(
                CombatEventType.DAMAGE_DEALT,
                defender,
                attacker,
                dmg
        ));

        queue.add(new CombatEvent(
                CombatEventType.DAMAGE_RECEIVED,
                attacker,
                defender,
                dmg
        ));
    }

    // ---------------- STATES ----------------

    private boolean handleEnemyDefeated(CombatEvent event) {
        event.getSource()
                .getMentalState()
                .onEvent(event.getType(), event.getSource(), event.getTarget());
        return false;
    }

    private boolean handleAllyDefeated(CombatEvent event) {
        return false;
    }

    private boolean handleDefensePrepared(CombatEvent event) {
        event.getSource()
                .getMentalState()
                .onEvent(event.getType(), event.getSource(), event.getTarget());
        return false;
    }

    // ---------------- UTILS ----------------

    private boolean isDead(Character c, Set<Character> dead) {
        return dead.contains(c) || c.getCurrentHP() <= 0;
    }
}