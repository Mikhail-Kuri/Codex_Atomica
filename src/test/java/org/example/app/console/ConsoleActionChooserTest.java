package org.example.app.console;

import org.example.Skills.Actions.Action;
import org.example.Skills.Actions.OffensiveAction;
import org.example.app.data.GameData;
import org.example.core.character.Character;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/** Menu du joueur en console : affiche l'état et les choix, lit un numéro. */
class ConsoleActionChooserTest {

    private final ByteArrayOutputStream output = new ByteArrayOutputStream();

    @Test
    @DisplayName("Taper un numéro choisit la compétence correspondante")
    void numberPicksMatchingSkill() {
        Character paladin = GameData.createPaladin(); // Attaque, Frappe radioactive
        Character enemy = GameData.createEnemy();

        Action action = chooser("2\n").choose(paladin, enemy);

        assertEquals(paladin.getOffensiveSkills().get(1), ((OffensiveAction) action).getSkill());
        assertEquals(enemy, action.getTarget());
    }

    @Test
    @DisplayName("La dernière option est la défense")
    void lastOptionIsDefense() {
        Character paladin = GameData.createPaladin();
        int defenseOption = paladin.getOffensiveSkills().size() + 1;

        Action action = chooser(defenseOption + "\n").choose(paladin, GameData.createEnemy());

        assertTrue(action.isDefenseAction());
    }

    @Test
    @DisplayName("Une saisie invalide redemande le choix")
    void invalidInputAsksAgain() {
        Character paladin = GameData.createPaladin();

        Action action = chooser("x\n9\n1\n").choose(paladin, GameData.createEnemy());

        assertEquals(paladin.getDefaultOffensiveSkill(), ((OffensiveAction) action).getSkill());
        assertTrue(printed().contains("invalide"));
    }

    @Test
    @DisplayName("Le menu affiche les noms des compétences, la défense et les PV")
    void menuShowsSkillsDefenseAndHp() {
        Character paladin = GameData.createPaladin();
        Character enemy = GameData.createEnemy();

        chooser("1\n").choose(paladin, enemy);

        String menu = printed();
        for (var skill : paladin.getOffensiveSkills()) {
            assertTrue(menu.contains(skill.getName()));
        }
        assertTrue(menu.contains(paladin.getCurrentDefense().getName()));
        assertTrue(menu.contains(enemy.getCurrentHP() + "/"));
    }

    @Test
    @DisplayName("Si l'entrée se termine, le menu s'arrête proprement")
    void endOfInputStopsTheMenu() {
        assertThrows(InputClosedException.class,
                () -> chooser("").choose(GameData.createPaladin(), GameData.createEnemy()));
    }

    private ConsoleActionChooser chooser(String typed) {
        return new ConsoleActionChooser(
                new BufferedReader(new StringReader(typed)),
                new PrintStream(output, true, StandardCharsets.UTF_8));
    }

    private String printed() {
        return output.toString(StandardCharsets.UTF_8);
    }
}
