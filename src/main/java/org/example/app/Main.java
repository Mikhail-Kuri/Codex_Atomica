package org.example.app;

import org.example.app.console.ConsoleActionChooser;
import org.example.app.console.ConsoleInput;
import org.example.app.console.InputClosedException;
import org.example.app.data.GameData;
import org.example.core.character.Character;
import org.example.gameplay.ai.SimpleAiChooser;
import org.example.gameplay.combat.Duel;
import org.example.gameplay.combat.DuelOutcome;
import org.example.gameplay.combat.TurnManager;
import org.example.gameplay.log.ConsoleCombatLog;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Random;
import java.util.stream.Stream;

/**
 * Lance un combat 1 contre 1 en console : choix du personnage, de l'adversaire, puis combat.
 * Ajouter {@code --debug} pour afficher le détail des calculs.
 */
public class Main {

    /** Probabilité que l'ennemi défende à chaque tour. */
    private static final double ENEMY_DEFEND_CHANCE = 0.25;

    /** Limite de sécurité : au-delà, le combat est déclaré nul. */
    private static final int MAX_TURNS = 50;

    /** Héros puis ennemis, dans l'ordre des fichiers classes.json et enemies.json. */
    private static List<String> fighterIds() {
        return Stream.concat(
                GameData.CONTENT.classIds().stream(),
                GameData.CONTENT.enemyIds().stream()).toList();
    }

    public static void main(String[] args) {
        boolean debug = List.of(args).contains("--debug");
        PrintStream out = new PrintStream(System.out, true, StandardCharsets.UTF_8);
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in, StandardCharsets.UTF_8));
        ConsoleInput input = new ConsoleInput(in, out);

        out.println("=== CODEX ATOMICA: training duel ===");

        try {
            Character player = pickFighter("Choose your fighter:", input, out);
            Character enemy = pickFighter("Choose your opponent:", input, out);

            Duel duel = new Duel(
                    player,
                    enemy,
                    new ConsoleActionChooser(in, out),
                    new SimpleAiChooser(ENEMY_DEFEND_CHANCE, new Random()),
                    new TurnManager(new ConsoleCombatLog(out, debug)),
                    MAX_TURNS);

            out.println();
            out.println(announce(duel.run()));
        } catch (InputClosedException e) {
            out.println();
            out.println("Game interrupted.");
        }
    }

    private static Character pickFighter(String title, ConsoleInput input, PrintStream out) {
        List<String> ids = fighterIds();
        out.println();
        out.println(title);
        for (int i = 0; i < ids.size(); i++) {
            out.println("  " + (i + 1) + ". " + GameData.CONTENT.nameOf(ids.get(i)));
        }
        return GameData.CONTENT.create(ids.get(input.readChoice(ids.size()) - 1));
    }

    private static String announce(DuelOutcome outcome) {
        return switch (outcome) {
            case PLAYER_WON -> "🏆 Victory!";
            case ENEMY_WON -> "☠ Defeat…";
            case DRAW -> "🤝 Draw.";
        };
    }
}
