package org.example.core.character.profile;
import org.example.gameplay.combat.CombatEvent;
import org.example.gameplay.mental.MentalState;

public class CharacterProfile {

    private String name;
    private MentalState mentalState;

    public CharacterProfile (String name, MentalState mentalState) {
        this.name = name;
        this.mentalState = mentalState;
    }

    public void onEvent(CombatEvent event, Character source) {
        if (mentalState != null) {
            mentalState.onEvent(event, null, source);
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public MentalState getMentalState() {
        return mentalState;
    }

    public void setMentalState(MentalState mentalState) {
        this.mentalState = mentalState;
    }
}
