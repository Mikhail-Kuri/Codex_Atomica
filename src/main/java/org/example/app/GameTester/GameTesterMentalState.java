package org.example.app.GameTester;

import org.example.app.data.GameData;
import org.example.core.character.Character;
import org.example.gameplay.combat.TurnManager;

public class GameTesterMentalState {


    public static void test1() {
        Character paladin = GameData.createPaladin();
        Character enemy = GameData.createEnemy();

        TurnManager tm = new TurnManager();
//        System.out.println(paladin.get().getMentalState().getClass());
    }


    public static void main(String[] args) {
        test1();
    }
}
