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
import java.util.function.Supplier;

/**
 * Lance un combat 1 contre 1 en console : choix du personnage, de l'adversaire, puis combat.
 * Ajouter {@code --debug} pour afficher le détail des calculs.
 */
public class Main {

    /** Probabilité que l'ennemi défende à chaque tour. */
    private static final double ENEMY_DEFEND_CHANCE = 0.25;

    /** Limite de sécurité : au-delà, le combat est déclaré nul. */
    private static final int MAX_TURNS = 50;

    private record Fighter(String name, Supplier<Character> factory) {}

    private static final List<Fighter> FIGHTERS = List.of(
            new Fighter("Paladin du Noyau", GameData::createPaladin),
            new Fighter("Gnoll Irradié", GameData::createEnemy),
            new Fighter("Éclaireur Mutant", GameData::createScout),
            new Fighter("Mutant Électrique", GameData::createMutant),
            new Fighter("Rat Mutant", GameData::createFragile)
    );

    public static void main(String[] args) {
        boolean debug = List.of(args).contains("--debug");
        PrintStream out = new PrintStream(System.out, true, StandardCharsets.UTF_8);
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in, StandardCharsets.UTF_8));
        ConsoleInput input = new ConsoleInput(in, out);

        out.println("=== CODEX ATOMICA : combat d'entraînement ===");

        try {
            Character player = pickFighter("Choisis ton personnage :", input, out);
            Character enemy = pickFighter("Choisis ton adversaire :", input, out);

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
            out.println("Partie interrompue.");
        }
    }

    private static Character pickFighter(String title, ConsoleInput input, PrintStream out) {
        out.println();
        out.println(title);
        for (int i = 0; i < FIGHTERS.size(); i++) {
            out.println("  " + (i + 1) + ". " + FIGHTERS.get(i).name());
        }
        return FIGHTERS.get(input.readChoice(FIGHTERS.size()) - 1).factory().get();
    }

    private static String announce(DuelOutcome outcome) {
        return switch (outcome) {
            case PLAYER_WON -> "🏆 Victoire !";
            case ENEMY_WON -> "☠ Défaite…";
            case DRAW -> "🤝 Match nul.";
        };
    }
}
