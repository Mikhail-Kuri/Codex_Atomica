package org.example.gameplay.log;

import java.io.PrintStream;

/** Journal de combat qui écrit dans la console. Le détail (debug) peut être masqué. */
public class ConsoleCombatLog implements CombatLog {

    private final PrintStream out;
    private final boolean showDebug;

    /**
     * @param showDebug true pour afficher aussi le détail des calculs (mode développeur)
     */
    public ConsoleCombatLog(boolean showDebug) {
        this(System.out, showDebug);
    }

    public ConsoleCombatLog(PrintStream out, boolean showDebug) {
        this.out = out;
        this.showDebug = showDebug;
    }

    @Override
    public void info(String message) {
        out.println(message);
    }

    @Override
    public void debug(String message) {
        if (showDebug) {
            out.println("   · " + message);
        }
    }
}
