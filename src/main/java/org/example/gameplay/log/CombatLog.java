package org.example.gameplay.log;

/**
 * Journal de combat : la seule sortie de la logique du jeu.
 * La logique n'écrit jamais dans la console ; elle écrit ici, et c'est l'affichage
 * (console aujourd'hui, interface Godot/Unity demain) qui décide quoi montrer.
 */
public interface CombatLog {

    /** Message pour le joueur : dégâts, morts, défenses, actions refusées… */
    void info(String message);

    /** Détail technique pour le développeur : calculs, résistances, état mental… */
    void debug(String message);

    /** Journal qui ignore tout, pour les cas où personne ne lit. */
    CombatLog SILENT = new CombatLog() {
        @Override
        public void info(String message) {}

        @Override
        public void debug(String message) {}
    };
}
