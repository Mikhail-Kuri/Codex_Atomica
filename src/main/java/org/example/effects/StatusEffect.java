package org.example.effects;

import org.example.core.character.Character;

public abstract class StatusEffect {

    protected String name;
    protected int duration; // nombre de tours restants

    public StatusEffect(String name, int duration) {
        this.name = name;
        this.duration = duration;
    }

    public String getName() {
        return name;
    }

    public int getDuration() {
        return duration;
    }

    public void refreshDuration(int newDuration) {
        this.duration = newDuration;
    }

    public boolean isExpired() {
        return duration <= 0;
    }

    // appelé au début du tour
    public void onTurnStart(Character target) {}

    // appelé à la fin du tour
    public void onTurnEnd(Character target) {
        duration--;
    }

    @Override
    public String toString() {
        return "StatusEffect{" +
                "name='" + name + '\'' +
                ", duration=" + duration +
                '}';
    }
}