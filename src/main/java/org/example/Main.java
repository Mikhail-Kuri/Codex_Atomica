package org.example;



public class Main {
    public static void main(String[] args) {

        Character hero = new Character("Aragorn",
                new Attributes(
                10,
                8,
                7,
                5,
                6,
                0.5f,
                3,
                2)
        );

        Character villain = new Character("Sauron",
                new Attributes(
                15,
                10,
                5,
                8,
                9,
                0.8f,
                5,
                4)
        );
    }
}