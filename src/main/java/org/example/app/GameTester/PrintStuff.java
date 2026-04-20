package org.example.app.GameTester;

import org.example.core.character.Character;
import org.example.gameplay.combat.CombatEventType;

import java.util.List;

public class PrintStuff {

    public static void printStats(List<org.example.core.character.Character> characters) {
        for (Character character : characters) {
            character.printStats();
        }
    }

    public static void printMentalStateEvent(CombatEventType event, Character self, Character source, int delta, int value) {
        System.out.println("💡 " + self.getName() + " a " + (delta >= 0 ? "gagné " : "perdu ") + Math.abs(delta) + " points de foi (total: " + value + ") pour l'événement " + event);
    }
}
