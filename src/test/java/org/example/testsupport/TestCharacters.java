package org.example.testsupport;

import org.example.Skills.DefensiveSkills.DefensiveSkill;
import org.example.Skills.DefensiveSkills.Evade;
import org.example.app.data.GameData;
import org.example.core.character.Attributes.CharacterAttributes;
import org.example.core.character.Attributes.arsenal.CharacterEquipment;
import org.example.core.character.Attributes.arsenal.CharacterSkills;
import org.example.core.character.Character;

import java.util.List;
import java.util.Map;

/** Fabrique de personnages pour les tests, aux stats contrôlées. */
public final class TestCharacters {

    /** Assez de PV pour qu'aucun test ne tue le personnage par accident. */
    public static final int TANKY_HP = 1000;

    /** Esquive dont le jet réussit toujours (hasard truqué à 0). */
    public static final DefensiveSkill ALWAYS_DODGE =
            new Evade(GameData.TUNING.combat().evade(), new FixedRandom(0.0));

    private TestCharacters() {}

    /** Personnage du profil Paladin, sans résistances, avec la Vigueur, l'Agilité et la défense voulues. */
    public static Character create(int vigor, int agility, DefensiveSkill defense) {
        return new Character(GameData.paladinProfile, stats(vigor, agility),
                new CharacterEquipment(GameData.SWORD),
                new CharacterSkills(List.of(GameData.basicAttack), defense));
    }

    /** Comme {@link #create}, mais avec un nombre d'actions par tour imposé. */
    public static Character withActionsPerTurn(int actionsPerTurn, DefensiveSkill defense) {
        return new Character(GameData.paladinProfile, stats(0, 0),
                new CharacterEquipment(GameData.SWORD),
                new CharacterSkills(List.of(GameData.basicAttack), defense)) {
            @Override
            public int getActionsPerTurn() {
                return actionsPerTurn;
            }
        };
    }

    private static CharacterAttributes stats(int vigor, int agility) {
        CharacterAttributes base = GameData.PALADIN_STATS;
        return new CharacterAttributes(TANKY_HP, vigor, agility, base.strength,
                base.intelligence, base.willpower, base.resonance, 0,
                base.minSpeed, base.maxSpeed, Map.of());
    }
}
