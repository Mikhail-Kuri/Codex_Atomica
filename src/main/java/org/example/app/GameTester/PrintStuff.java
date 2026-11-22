package org.example.app.GameTester;

import org.example.core.character.Character;

import java.util.List;

public class PrintStuff {

    public static void printStats(List<org.example.core.character.Character> characters) {
        for (Character character : characters) {
            character.printStats();
        }
    }
}
