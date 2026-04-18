package org.example.core.character.Attributes;

public class CharacterAttributes {

    public int vitality;
    public int vigor;
    public int agility;
    public int strength;
    public int intelligence;
    public int willpower;
    public float resonance;
    public int luck;
    public int minSpeed;
    public int maxSpeed;

    public CharacterAttributes(int vitality, int vigor, int agility,
                               int strength, int intelligence,
                               int willpower, float resonance,
                               int luck,
                               int minSpeed, int maxSpeed) {

        this.vitality = vitality;
        this.vigor = vigor;
        this.agility = agility;
        this.strength = strength;
        this.intelligence = intelligence;
        this.willpower = willpower;
        this.resonance = resonance;
        this.luck = luck;
        this.minSpeed = minSpeed;
        this.maxSpeed = maxSpeed;
    }

    public void setSpeed(int min, int max) {
        if (min > max) {
            throw new IllegalArgumentException("minSpeed cannot be > maxSpeed");
        }

        this.minSpeed = min;
        this.maxSpeed = max;
    }

    public int getVitality() {
        return vitality;
    }

    public void setVitality(int vitality) {
        this.vitality = vitality;
    }

    public int getVigor() {
        return vigor;
    }

    public void setVigor(int vigor) {
        this.vigor = vigor;
    }

    public int getAgility() {
        return agility;
    }

    public int getStrength() {
        return strength;
    }

    public void setAgility(int agility) {
        this.agility = agility;
    }

    public void setStrength(int strength) {
        this.strength = strength;
    }

    public int getIntelligence() {
        return intelligence;
    }

    public void setIntelligence(int intelligence) {
        this.intelligence = intelligence;
    }

    public int getWillpower() {
        return willpower;
    }

    public void setWillpower(int willpower) {
        this.willpower = willpower;
    }

    public float getResonance() {
        return resonance;
    }

    public void setResonance(float resonance) {
        this.resonance = resonance;
    }

    public int getLuck() {
        return luck;
    }

    public void setLuck(int luck) {
        this.luck = luck;
    }

    public int getMinSpeed() {
        return minSpeed;
    }

    public void setMinSpeed(int minSpeed) {
        this.minSpeed = minSpeed;
    }

    public int getMaxSpeed() {
        return maxSpeed;
    }

    public void setMaxSpeed(int maxSpeed) {
        this.maxSpeed = maxSpeed;
    }
}
