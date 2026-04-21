package org.example.core.character.Attributes;

import org.example.gameplay.mental.MentalState;
import org.example.gameplay.mental.MentalStateType;

public class CharacterState {

    private int currentHP;
    private int currentSanity;
    private boolean isDefending;
    private boolean isAlive;
    private MentalState mentalState;
    private int pendingCounterDamage = 0;


    public CharacterState() {
        this.isDefending = false;
        this.isAlive = true;
        this.currentSanity = 0;
    }

    public void init(CharacterAttributes stats, CharacterProfile profile) {
        this.currentHP = stats.getVitality();
        MentalStateType mentalState = profile.getMentalState();
        this.mentalState = mentalState.create();
    }

    public int getCurrentHP() {
        return currentHP;
    }

    public void setCurrentHP(int currentHP) {
        this.currentHP = currentHP;
    }

    public void reduceHP(int amount) {
        currentHP -= amount;

        if (currentHP <= 0) {
            currentHP = 0;
            isAlive = false;
        }
    }

    public int getCurrentSanity() {
        return currentSanity;
    }

    public void setCurrentSanity(int currentSanity) {
        this.currentSanity = currentSanity;
    }

    public boolean isDefending() {
        return isDefending;
    }

    public void setDefending(boolean defending) {
        isDefending = defending;
    }

    public boolean isAlive() {
        return isAlive;
    }

    public void setAlive(boolean alive) {
        isAlive = alive;
    }

    public MentalState getMentalState() {
        return mentalState;
    }

    public int getPendingCounterDamage() {
        return pendingCounterDamage;
    }

    public void setPendingCounterDamage(int damage) {
        this.pendingCounterDamage = damage;
    }

    public void clearPendingCounterDamage() {
        this.pendingCounterDamage = 0;
    }
}


