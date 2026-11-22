package org.example.gameplay.combat;

import org.example.Skills.Actions.Action;
import org.example.core.character.Character;

/**
 * Décide de l'action d'un personnage pour ce tour.
 * Un joueur (menu console aujourd'hui, interface Godot/Unity demain) ou une IA.
 */
@FunctionalInterface
public interface ActionChooser {

    /**
     * @param self     le personnage qui agit
     * @param opponent son adversaire dans ce combat 1 contre 1
     * @return l'action choisie (jamais null)
     */
    Action choose(Character self, Character opponent);
}
