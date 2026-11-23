package org.example.data;

/** Données de jeu invalides : le message nomme le fichier et le problème. */
public class DataException extends RuntimeException {

    public DataException(String file, String message) {
        super(file + ": " + message);
    }

    public DataException(String file, String message, Throwable cause) {
        super(file + ": " + message, cause);
    }
}
