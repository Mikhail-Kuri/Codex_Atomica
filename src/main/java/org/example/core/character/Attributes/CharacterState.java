package org.example.core.character.Attributes;

public class CharacterState {

    private int currentHP;
    private int currentSanity;
    private boolean isDefending;
    private boolean isAlive;


    public CharacterState() {
        this.isDefending = false;
        this.isAlive = true;
    }

    public void initFromStats(CharacterAttributes stats) {
        this.currentHP = stats.getVitality();
    }

    public int getCurrentHP() {
        return currentHP;
    }

    public void setCurrentHP(int currentHP) {
        this.currentHP = currentHP;
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
}


