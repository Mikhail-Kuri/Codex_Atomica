package org.example.core.character;

import org.example.Skills.DefensiveSkills.DefensiveSkill;
import org.example.Skills.OffensiveSkills.BasicAttack;
import org.example.Skills.OffensiveSkills.OffensiveSkill;
import org.example.Weapons.Weapon;
import org.example.core.character.Attributes.*;
import org.example.core.character.Attributes.arsenal.*;
import org.example.core.character.loggs.CharacterLoggedEvents;
import org.example.data.EnemyRank;
import org.example.effects.StatusEffect;
import org.example.gameplay.mental.MentalState;
import org.example.progression.StatProfile;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class Character {

    private final CharacterState state;
    private final CharacterProfile profile;
    private final CharacterAttributes stats;
    private final CharacterEquipment equipment;
    private final CharacterSkills skills;
    private CharacterLoggedEvents loggedEvents;

    /** Base et croissance des stats ; null pour un personnage aux stats fixes (tests). */
    private final StatProfile statProfile;
    /** Rang d'ennemi pour l'XP ; null pour un héros. */
    private final EnemyRank rank;
    private int level = 1;
    private long experience = 0;

    /** Personnage aux statistiques fixes, de niveau 1. */
    public Character(
            CharacterProfile profile,
            CharacterAttributes stats,
            CharacterEquipment equipment,
            CharacterSkills skills) {
        this(profile, stats, equipment, skills, null, null);
    }

    /**
     * Personnage dont les statistiques suivent un profil de croissance par niveau.
     *
     * @param statProfile base et croissance (null = stats fixes)
     * @param rank        rang d'ennemi (null pour un héros)
     */
    public Character(
            CharacterProfile profile,
            CharacterAttributes stats,
            CharacterEquipment equipment,
            CharacterSkills skills,
            StatProfile statProfile,
            EnemyRank rank) {

        this.state = new CharacterState();
        this.profile = profile;
        this.stats = stats;
        this.equipment = equipment;
        this.skills = skills;
        this.loggedEvents = new CharacterLoggedEvents();
        this.statProfile = statProfile;
        this.rank = rank;

        this.state.init(stats,profile);

    }

    // =====================
    // LEVEL
    // =====================

    public int getLevel() {
        return level;
    }

    /** XP accumulée vers le niveau suivant. */
    public long getExperience() {
        return experience;
    }

    public void setExperience(long experience) {
        this.experience = experience;
    }

    /** Rang d'ennemi pour l'XP ; {@link EnemyRank#NORMAL} si ce n'est pas un ennemi. */
    public EnemyRank getRank() {
        return (rank != null) ? rank : EnemyRank.NORMAL;
    }

    /**
     * Passe au niveau donné et recalcule les statistiques (formule S1).
     * Les PV actuels gardent la même proportion des PV max (formule S2).
     */
    public void applyLevel(int newLevel) {
        if (newLevel < 1) {
            throw new IllegalArgumentException("Level must be at least 1");
        }
        int oldMaxHp = stats.getVitality();
        this.level = newLevel;

        if (statProfile == null) return;

        statProfile.applyTo(stats, newLevel);

        int newMaxHp = stats.getVitality();
        if (oldMaxHp > 0 && isAlive()) {
            int hp = Math.round((float) getCurrentHP() * newMaxHp / oldMaxHp);
            state.setCurrentHP(Math.max(1, Math.min(newMaxHp, hp)));
        }
    }

    // =====================
    // BASIC INFO
    // =====================

    public String getName() {
        return profile.getName();
    }

    public boolean isAlive() {
        return state.isAlive();
    }

    public int getCurrentHP() {
        return state.getCurrentHP();
    }

    public CharacterAttributes getAttributes() {
        return stats;
    }

    public Weapon getEquippedWeapon() {
        return equipment.getEquippedWeapon();
    }

    // =====================
    // COMBAT
    // =====================

    /**
     * Reçoit un coup d'un attaquant. Passe par la défense active si le personnage
     * défend (et la consomme), sinon soustrait la Vigueur.
     *
     * N'écrit rien : c'est le combat qui annonce le résultat dans le journal.
     *
     * @return les dégâts infligés, et si la défense les a réduits
     */
    public DamageResult takeDamage(int amount, Character attacker) {

        if (!isAlive()) {
            return DamageResult.NONE;
        }

        DamageResult result = computeFinalDamage(amount, attacker);

        applyHpLoss(result.damage());
        return result;
    }

    /**
     * Reçoit les dégâts de fin de tour d'un effet de statut (brûlure, saignement…).
     * Ne passe ni par la défense ni par la Vigueur, et ne consomme pas la défense.
     * C'est le point d'entrée où brancher plus tard les immunités et résistances aux effets.
     */
    public void takeStatusDamage(int amount, StatusEffect effect) {

        if (!isAlive()) {
            return;
        }

        applyHpLoss(amount);
    }

    private void applyHpLoss(int amount) {
        state.reduceHP(amount);

        if (!isAlive()) {
            die();
        }
    }

    private DamageResult computeFinalDamage(int amount, Character attacker) {

        int undefended = Math.max(0, amount - stats.vigor);

        if (state.isDefending() && getCurrentDefense() != null) {
            int dmg = getCurrentDefense().onDamageTaken(amount, this, attacker);
            state.setDefending(false);
            return new DamageResult(dmg, dmg < undefended);
        }

        return new DamageResult(undefended, false);
    }

    public void applyStatusEffect(StatusEffect newEffect) {

        for (StatusEffect existing : state.getStatusEffects()) {

            if (existing.getClass().equals(newEffect.getClass())) {

                existing.refreshDuration(
                        existing.getDuration() + newEffect.getDuration()
                );
                return;
            }
        }

        state.getStatusEffects().add(newEffect);
    }

    // =====================
    // DEFENSE
    // =====================

    public void prepareDefense() {
        state.setDefending(true);
    }

    public boolean isDefending() {
        return state.isDefending();
    }

    /** Met fin à la défense et à toute riposte en attente. Appelé en fin de tour. */
    public void endDefense() {
        state.setDefending(false);
        state.clearPendingCounterDamage();
    }

    // =====================
    // TURN
    // =====================

    /**
     * Nombre d'actions que ce personnage peut planifier par tour.
     * 1 pour tout le monde aujourd'hui ; un passif ou une compétence
     * (personnage très rapide, « +1 action ») pourra l'augmenter.
     */
    public int getActionsPerTurn() {
        return 1;
    }

    public DefensiveSkill getCurrentDefense() {
        return skills.getCurrentDefense();
    }

    public void setCurrentDefense(DefensiveSkill defensiveSkill){
        this.skills.setCurrentDefense(defensiveSkill);
    }

    // =====================
    // SKILLS
    // =====================

    public List<OffensiveSkill> getOffensiveSkills() {
        return skills.getOffensiveSkills();
    }

    public OffensiveSkill getDefaultOffensiveSkill() {
        return skills.getOffensiveSkills().stream()
                .filter(skill -> skill instanceof BasicAttack)
                .findFirst()
                .orElse(null);
    }

    // =====================
    // EQUIPMENT
    // =====================

    public void setWeapon(Weapon weapon) {
        equipment.equipWeapon(weapon);
    }

    // =====================
    // UTILS
    // =====================

    public int rollSpeed() {
        int min = stats.minSpeed;
        int max = stats.maxSpeed;

        if (min > max) {
            throw new IllegalStateException("Invalid speed: min > max");
        }

        return (min == max)
                ? min
                : ThreadLocalRandom.current().nextInt(min, max + 1);
    }

    /** Affichage de débogage pour les GameTester ; n'est jamais appelé par la logique du combat. */
    public void printStats() {
        System.out.println("\n===== STATS: " + getName() + " =====");

        System.out.println("HP : " + getCurrentHP() + "/" + stats.vitality);
        System.out.println("Alive : " + isAlive());

        System.out.println("Weapon : " +
                (getEquippedWeapon() != null ? getEquippedWeapon().getName() : "None"));

        System.out.println("Defense : " +
                (getCurrentDefense() != null ? getCurrentDefense().getName() : "None"));

        System.out.println("Vigor : " + stats.vigor);
        System.out.println("Resonance : " + stats.resonance);

        System.out.println("================================\n");
    }

    public CharacterState getState() {
        return state;
    }

    public MentalState getMentalState() {
        return state.getMentalState();
    }

    public OffensiveSkill getCurrentlyUsedOffensiveSkill(){
        return this.loggedEvents.getCurrentlyUsedOffensiveSkill();
    }

    public void setCurrentlyUsedOffensiveSkill(OffensiveSkill currentSkill){
        this.loggedEvents.setCurrentlyUsedOffensiveSkill(currentSkill);
    }

    public void die() {
        state.setAlive(false);
        state.setCurrentHP(0);
    }
}