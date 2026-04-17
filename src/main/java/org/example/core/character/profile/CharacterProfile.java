package org.example.core.character.profile;
//import org.example.gameplay.combat.CombatEvent;
//import org.example.gameplay.mental.MentalState;
import org.example.gameplay.mental.MentalStateType;

public class CharacterProfile {

    private String name;
    private MentalStateType mentalStateType;

    public CharacterProfile (String name, MentalStateType mentalStateType) {
        this.name = name;
        this.mentalStateType = mentalStateType;
    }

    /*public void onEvent(CombatEvent event, Character source) {
        if (mentalState != null) {
            mentalState.onEvent(event, null, source);
        }
    } */
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public MentalStateType getMentalState() {
        return mentalStateType;
    }

    public void setMentalStateType(MentalStateType mentalStateType) {
        this.mentalStateType = mentalStateType;
    }
}
