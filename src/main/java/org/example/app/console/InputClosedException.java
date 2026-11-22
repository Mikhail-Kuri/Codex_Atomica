package org.example.app.console;

/** L'entrée du joueur s'est terminée (fin de fichier, console fermée) pendant un choix. */
public class InputClosedException extends RuntimeException {

    public InputClosedException() {
        super("L'entrée du joueur est terminée.");
    }
}
