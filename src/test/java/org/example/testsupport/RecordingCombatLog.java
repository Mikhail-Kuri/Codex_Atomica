package org.example.testsupport;

import org.example.gameplay.log.CombatLog;

import java.util.ArrayList;
import java.util.List;

/** Journal de combat qui garde les messages en mémoire, pour les vérifier dans les tests. */
public class RecordingCombatLog implements CombatLog {

    public final List<String> infos = new ArrayList<>();
    public final List<String> debugs = new ArrayList<>();

    @Override
    public void info(String message) {
        infos.add(message);
    }

    @Override
    public void debug(String message) {
        debugs.add(message);
    }

    public boolean infoContains(String fragment) {
        return infos.stream().anyMatch(m -> m.contains(fragment));
    }

    public boolean debugContains(String fragment) {
        return debugs.stream().anyMatch(m -> m.contains(fragment));
    }
}
