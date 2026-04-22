package org.example.gameplay.combat;

import org.example.Skills.Actions.Action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.example.core.character.Character;


public class ActionOrderResolver {

    public List<Action> sort(List<Action> actions) {

        Map<Character, Integer> speedCache = new HashMap<>();

        for (Action action : actions) {
            Character source = action.getSource();
            speedCache.putIfAbsent(source, source.rollSpeed());
        }

        actions.sort((a, b) -> {

            int priorityCompare = Integer.compare(
                    b.getPriority(),
                    a.getPriority()
            );

            if (priorityCompare != 0) return priorityCompare;

            return Integer.compare(
                    speedCache.get(b.getSource()),
                    speedCache.get(a.getSource())
            );
        });

        return actions;
    }
}