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
import org.example.progression.CombatXp;
import org.example.progression.LevelCurve;
import org.example.progression.LevelUp;
import org.example.progression.Progression;

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

            DuelOutcome outcome = duel.run();

            out.println();
            out.println(announce(outcome));

            if (outcome == DuelOutcome.PLAYER_WON) {
                rewardVictory(player, enemy, out);
            }
        } catch (InputClosedException e) {
            out.println();
            out.println("Game interrupted.");
        }
    }

    /** XP de combat (formules X4 et X6), puis montée de niveau éventuelle. */
    private static void rewardVictory(Character player, Character enemy, PrintStream out) {
        CombatXp combatXp = new CombatXp(GameData.TUNING);
        Progression progression = new Progression(GameData.TUNING);

        long xp = combatXp.forDefeating(player.getLevel(), enemy.getLevel(), enemy.getRank());
        if (xp == 0) {
            out.println(enemy.getName() + " is too weak for you: no XP.");
            return;
        }

        LevelUp levelUp = progression.grantXp(player, xp);
        out.println("+" + xp + " XP");
        if (levelUp.levelsGained() > 0) {
            out.println("⬆ Level up! " + player.getName() + " is now level " + levelUp.toLevel() + ".");
        }
        long remaining = progression.xpToNextLevel(player);
        if (remaining > 0) {
            out.println(remaining + " XP to level " + (player.getLevel() + 1) + ".");
        }
    }

    private static Character pickFighter(String title, ConsoleInput input, PrintStream out) {
        List<String> ids = fighterIds();
        out.println();
        out.println(title);
        for (int i = 0; i < ids.size(); i++) {
            out.println("  " + (i + 1) + ". " + GameData.CONTENT.nameOf(ids.get(i)));
        }
        String id = ids.get(input.readChoice(ids.size()) - 1);

        int maxLevel = new LevelCurve(GameData.TUNING).maxLevel();
        out.println("Level (1-" + maxLevel + "):");
        int level = input.readChoice(maxLevel);

        return GameData.CONTENT.create(id, level);
    }

    private static String announce(DuelOutcome outcome) {
        return switch (outcome) {
            case PLAYER_WON -> "🏆 Victory!";
            case ENEMY_WON -> "☠ Defeat…";
            case DRAW -> "🤝 Draw.";
        };
    }
}
