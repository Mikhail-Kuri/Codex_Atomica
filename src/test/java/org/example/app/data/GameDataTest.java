package org.example.app.data;

import org.example.Skills.DefensiveSkills.Counter;
import org.example.core.character.Character;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertSame;

/**
 * Régression : les createX() réutilisaient des CharacterSkills et
 * CharacterEquipment statiques, donc modifier un personnage modifiait les autres.
 */
class GameDataTest {

    @Test
    @DisplayName("Bug : changer la défense d'un ennemi changeait celle d'un autre")
    void changingOneCharacterDefenseDoesNotAffectAnother() {
        Character first = GameData.createEnemy();
        Character second = GameData.createEnemy();
        var originalDefense = second.getCurrentDefense();

        first.setCurrentDefense(new Counter());

        assertSame(originalDefense, second.getCurrentDefense());
    }

    @Test
    @DisplayName("Bug : le Mutant et le Paladin partageaient le même équipement")
    void changingOneCharacterWeaponDoesNotAffectAnother() {
        Character paladin = GameData.createPaladin();
        Character mutant = GameData.createMutant();
        var mutantWeapon = mutant.getEquippedWeapon();

        paladin.setWeapon(GameData.PLASMA);

        assertSame(mutantWeapon, mutant.getEquippedWeapon());
    }
}
