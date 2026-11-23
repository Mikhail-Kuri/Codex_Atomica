package org.example.testsupport;

import java.util.Random;

/** Hasard truqué pour les tests : nextDouble() renvoie toujours la même valeur. */
public class FixedRandom extends Random {

    private final double value;

    /** @param value entre 0 (inclus) et 1 (exclu) */
    public FixedRandom(double value) {
        this.value = value;
    }

    @Override
    public double nextDouble() {
        return value;
    }
}
