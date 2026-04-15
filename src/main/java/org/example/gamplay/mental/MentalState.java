package org.example.gamplay.mental;

public class MentalState {

    private int sanity; // -20 à +20

    public MentalState(int sanity) {
        this.sanity = sanity;
    }

    public int getSanity() {
        return sanity;
    }

    public void change(int amount) {
        sanity = Math.max(-20, Math.min(20, sanity + amount));
    }
}
