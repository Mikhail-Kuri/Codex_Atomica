package org.example;

public class Attributes {

    public int vitality;
    public int vigor;
    public int agility;
    public int intelligence;
    public int willpower;
    public float resonance;
    public int luck;
    public int faith;

    public Attributes(int vitality, int vigor, int agility, int intelligence, int willpower, float resonance, int luck, int faith) {
        this.vitality = vitality;
        this.vigor = vigor;
        this.agility = agility;
        this.intelligence = intelligence;
        this.willpower = willpower;
        this.resonance = resonance;
        this.luck = luck;
        this.faith = faith;
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

    public void setAgility(int agility) {
        this.agility = agility;
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

    public int getFaith() {
        return faith;
    }

    public void setFaith(int faith) {
        this.faith = faith;
    }
}
