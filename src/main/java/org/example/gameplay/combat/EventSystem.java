package org.example.gameplay.combat;

import java.util.*;

import org.example.Skills.DefensiveSkills.Counter;
import org.example.Skills.Scaling.DamageResolver;
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
                event.type() == CombatEventType.ENEMY_DEFEATED ||
                        event.type() == CombatEventType.ALLY_DEFEATED;

        return !isDeathEvent &&
                (isDead(event.source(), dead) ||
                        isDead(event.target(), dead));
    }

    private boolean handleEvent(CombatEvent event,
                                Queue<CombatEvent> queue,
                                boolean counterTriggered,
                                Set<Character> dead) {

        return switch (event.type()) {

            case DAMAGE_DEALT -> handleDamageDealt(event, queue);

            case DAMAGE_RECEIVED, SELF_DAMAGE -> handleDamageReceived(event, queue, counterTriggered, dead);

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

        Character defender = event.source();
        Character attacker = event.target();

        int before = defender.getCurrentHP();

        int finalDamage = DamageResolver.resolve(
                event.value(),
                attacker,
                defender,
                event.damageTypes()
        );

        defender.takeDamage(finalDamage, attacker);

        int after = defender.getCurrentHP();

        if (before == after) return counterTriggered;

        checkDeath(queue, event, defender, attacker, before, after, dead);

        if (after <= 0) return counterTriggered;

        if (isCounter(defender, counterTriggered)) {
            triggerCounter(queue, event,attacker, defender);
            return true;
        }

        defender.getMentalState()
                .onEvent(event.type(), defender, attacker);

        return counterTriggered;
    }

    private boolean handleDamageDealt(CombatEvent event,
                                      Queue<CombatEvent> queue) {

        Character attacker = event.source();
        Character defender = event.target();
        int damage = event.value();


        // 🧠 mental state trigger (attacker side)
        attacker.getMentalState()
                .onEvent(event.type(), attacker, defender);

        // 🔁 CREATE the actual damage event
        queue.add(new CombatEvent(
                CombatEventType.DAMAGE_RECEIVED,
                defender,   // now becomes "source" (the one receiving)
                attacker,// attacker becomes target
                event.damageTypes(),
                damage
        ));

        return false;
    }

    // ---------------- DEATH ----------------

    private void checkDeath(Queue<CombatEvent> queue,
                            CombatEvent event,
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
                event.damageTypes(),
                0
        ));

        queue.add(new CombatEvent(
                CombatEventType.ALLY_DEFEATED,
                defender,
                attacker,
                event.damageTypes(),
                0
        ));
    }

    // ---------------- COUNTER ----------------

    private boolean isCounter(Character defender, boolean alreadyTriggered) {
        return defender.getCurrentDefense() instanceof Counter
                && defender.getState().getPendingCounterDamage() > 0
                && !alreadyTriggered;
    }

    private void triggerCounter(Queue<CombatEvent> queue,
                                CombatEvent event,
                                Character attacker,
                                Character defender) {

        int dmg = defender.getState().getPendingCounterDamage();

        defender.getState().setPendingCounterDamage(0);


        queue.add(new CombatEvent(
                CombatEventType.DAMAGE_DEALT,
                defender,
                attacker,
                event.damageTypes(),
                dmg
        ));

        queue.add(new CombatEvent(
                CombatEventType.DAMAGE_RECEIVED,
                attacker,
                defender,
                event.damageTypes(),
                dmg
        ));
    }

    // ---------------- STATES ----------------

    private boolean handleEnemyDefeated(CombatEvent event) {
        event.source()
                .getMentalState()
                .onEvent(event.type(), event.source(), event.target());
        return false;
    }

    private boolean handleAllyDefeated(CombatEvent event) {
        return false;
    }

    private boolean handleDefensePrepared(CombatEvent event) {
        event.source()
                .getMentalState()
                .onEvent(event.type(), event.source(), event.target());
        return false;
    }

    // ---------------- UTILS ----------------

    private boolean isDead(Character c, Set<Character> dead) {
        return dead.contains(c) || c.getCurrentHP() <= 0;
    }

}