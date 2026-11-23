package org.example.app.console;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintStream;
import java.io.UncheckedIOException;

/** Lecture d'un choix numéroté dans la console, partagée par tous les menus. */
public class ConsoleInput {

    private final BufferedReader in;
    private final PrintStream out;

    public ConsoleInput(BufferedReader in, PrintStream out) {
        this.in = in;
        this.out = out;
    }

    /**
     * Lit un numéro entre 1 et max ; redemande tant que la saisie est invalide.
     *
     * @throws InputClosedException si l'entrée se termine avant un choix valide
     */
    public int readChoice(int max) {
        while (true) {
            out.print("> ");
            out.flush();

            String line = readLine();
            if (line == null) {
                throw new InputClosedException();
            }

            // Ignore le BOM qu'ajoutent certains terminaux Windows en début d'entrée
            String cleaned = line.replace("﻿", "").trim();

            try {
                int choice = Integer.parseInt(cleaned);
                if (choice >= 1 && choice <= max) {
                    return choice;
                }
            } catch (NumberFormatException ignored) {
                // saisie non numérique : on redemande
            }

            out.println("Invalid choice: type a number between 1 and " + max + ".");
        }
    }

    private String readLine() {
        try {
            return in.readLine();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
