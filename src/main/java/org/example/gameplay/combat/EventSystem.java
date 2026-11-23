package org.example.gameplay.combat;

import java.util.*;

import org.example.Skills.DefensiveSkills.Counter;
import org.example.Skills.Scaling.DamageResolver;
import org.example.Skills.Scaling.DamageType;
import org.example.core.character.Character;
import org.example.core.character.DamageResult;
import org.example.effects.StatusEffect;
import org.example.effects.StatusEffectFactory;
import org.example.gameplay.log.CombatLog;

public class EventSystem {

    /** Part du gain mental de l'attaquant quand une défense a réduit son coup. */
    private static final double MITIGATED_HIT_GAIN = 0.5;

    private final CombatLog log;

    /** @param log où annoncer ce qui se passe pendant le combat */
    public EventSystem(CombatLog log) {
        this.log = log;
    }

    /** Système d'événements sans journal. */
    public EventSystem() {
        this(CombatLog.SILENT);
    }

    public void process(List<CombatEvent> events,
                        Character source,
                        Character target) {

        if (events.isEmpty()) {
            if (source != null && !source.isAlive()) {
                log.info(source.getName() + " is out of combat and cannot act.");
            } else {
                String who = (source != null) ? source.getName() : "An action";
                log.info(who + ": action had no effect (invalid or defeated target).");
            }
            return;
        }

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
        boolean wasDefending = defender.isDefending() && defender.getCurrentDefense() != null;

        int finalDamage = DamageResolver.resolve(
                event.value(),
                attacker,
                defender,
                event.damageTypes(),
                log
        );

        DamageResult result = defender.takeDamage(finalDamage, attacker);

        int after = defender.getCurrentHP();

        reportHit(defender, attacker, before, after, wasDefending, result);

        if (event.type() == CombatEventType.DAMAGE_RECEIVED) {
            rewardAttacker(attacker, defender, before - after, result);
        }

        if (before == after) return counterTriggered;

        applyDamageTypeEffects(defender, event.damageTypes());

        checkDeath(queue, event, defender, attacker, before, after, dead);

        if (after <= 0) return counterTriggered;

        if (isCounter(defender, counterTriggered)) {
            triggerCounter(queue, event,attacker, defender);
            return true;
        }

        applyMental(defender, event.type(), attacker, 1.0);

        return counterTriggered;
    }

    /** Annonce au joueur le résultat d'un coup : défense, dégâts, PV restants. */
    private void reportHit(Character defender,
                           Character attacker,
                           int before,
                           int after,
                           boolean wasDefending,
                           DamageResult result) {

        String defense = wasDefending ? defender.getCurrentDefense().getName() : null;

        if (wasDefending && result.damage() == 0) {
            log.info("🛡 " + defender.getName() + " negates "
                    + attacker.getName() + "'s hit (" + defense + ")!");
            return;
        }

        if (wasDefending && result.mitigatedByDefense()) {
            log.info("🛡 " + defender.getName() + " softens the hit (" + defense + ").");
        }

        if (before == after) {
            log.info(defender.getName() + " takes no damage from " + attacker.getName() + ".");
            return;
        }

        log.info(defender.getName() + " takes " + (before - after) + " damage from "
                + attacker.getName() + " (" + after + "/"
                + defender.getAttributes().getVitality() + " HP)");
    }

    /** Applique une variation d'état mental et la note en debug. */
    private void applyMental(Character self,
                             CombatEventType type,
                             Character source,
                             double gainScale) {

        int delta = self.getMentalState().onEvent(type, self, source, gainScale);

        if (delta != 0) {
            log.debug("🧠 " + self.getName() + ": mental "
                    + (delta > 0 ? "+" : "") + delta + " (" + type + ") → "
                    + self.getMentalState().getValue());
        }
    }

    private void applyDamageTypeEffects(Character defender, Set<DamageType> damageTypes) {

        List<StatusEffect> effects =
                StatusEffectFactory.fromDamageTypes(damageTypes);

        for (StatusEffect effect : effects) {
            defender.applyStatusEffect(effect);
        }

        if (!effects.isEmpty()) {
            log.info(defender.getName() + " is afflicted by: " +
                    String.join(", ", effects.stream().map(StatusEffect::getName).toList()));
        }
    }

    private boolean handleDamageDealt(CombatEvent event,
                                      Queue<CombatEvent> queue) {

        Character attacker = event.source();
        Character defender = event.target();
        int damage = event.value();

        // Le gain mental de l'attaquant est donné après les dégâts
        // (voir rewardAttacker), selon que le coup est passé ou non.

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

    /**
     * Ressource de l'attaquant : gain complet si le coup passe, réduit si une
     * défense l'a atténué, aucun gain s'il n'a fait perdre aucun PV.
     */
    private void rewardAttacker(Character attacker,
                                Character defender,
                                int hpLost,
                                DamageResult result) {

        if (hpLost <= 0) return;

        double gainScale = result.mitigatedByDefense() ? MITIGATED_HIT_GAIN : 1.0;

        applyMental(attacker, CombatEventType.DAMAGE_DEALT, defender, gainScale);
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
        log.info("💀 " + defender.getName() + " has fallen.");

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

        log.info("↩ " + defender.getName() + " counterattacks!");

        // handleDamageDealt crée lui-même le DAMAGE_RECEIVED correspondant
        queue.add(new CombatEvent(
                CombatEventType.DAMAGE_DEALT,
                defender,
                attacker,
                event.damageTypes(),
                dmg
        ));
    }

    // ---------------- STATES ----------------

    private boolean handleEnemyDefeated(CombatEvent event) {
        applyMental(event.source(), event.type(), event.target(), 1.0);
        return false;
    }

    private boolean handleAllyDefeated(CombatEvent event) {
        return false;
    }

    private boolean handleDefensePrepared(CombatEvent event) {
        Character defender = event.source();
        String defense = (defender.getCurrentDefense() != null)
                ? defender.getCurrentDefense().getName()
                : "defense";

        log.info("🛡 " + defender.getName() + " takes a defensive stance (" + defense + ").");
        applyMental(defender, event.type(), event.target(), 1.0);
        return false;
    }

    // ---------------- UTILS ----------------

    private boolean isDead(Character c, Set<Character> dead) {
        return dead.contains(c) || c.getCurrentHP() <= 0;
    }

}