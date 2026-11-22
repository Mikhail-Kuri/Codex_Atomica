package org.example.Skills.OffensiveSkills;

import org.example.app.data.GameData;
import org.example.core.character.Attributes.CharacterAttributes;
import org.example.core.character.Character;
import org.example.gameplay.combat.CombatEvent;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Régression : la Vigueur du défenseur était soustraite deux fois
 * (dans OffensiveSkill.calculateDamage ET dans Character.takeDamage).
 * Elle ne doit l'être qu'une fois, côté défenseur.
 *
 * Note : tous les personnages de GameData ont 0 en Vigueur, d'où une
 * cible de test dédiée.
 */
class VigorTest {

    /** Vigueur volontairement non nulle : c'est la valeur testée. */
    private static final int TARGET_VIGOR = 10;

    @Test
    @DisplayName("Bug : l'attaque retirait déjà la Vigueur de la cible")
    void skillDamageDoesNotDependOnTargetVigor() {
        Character attacker = GameData.createPaladin();
        OffensiveSkill skill = attacker.getDefaultOffensiveSkill();

        int vsNoVigor = rawDamage(skill, attacker, withVigor(0));
        int vsVigor = rawDamage(skill, attacker, withVigor(TARGET_VIGOR));

        assertEquals(vsNoVigor, vsVigor);
    }

    @Test
    @DisplayName("Bug : la Vigueur était comptée deux fois sur un coup reçu")
    void takeDamageSubtractsVigorExactlyOnce() {
        Character attacker = GameData.createPaladin();
        Character target = withVigor(TARGET_VIGOR);
        int incoming = rawDamage(attacker.getDefaultOffensiveSkill(), attacker, target);
        int hpBefore = target.getCurrentHP();

        target.takeDamage(incoming, attacker);

        assertEquals(incoming - TARGET_VIGOR, hpBefore - target.getCurrentHP());
    }

    private static int rawDamage(OffensiveSkill skill, Character attacker, Character target) {
        List<CombatEvent> events = skill.execute(attacker, target);
        return events.get(0).value();
    }

    /** Un Gnoll de GameData, avec une Vigueur modifiée sur une copie de ses stats. */
    private static Character withVigor(int vigor) {
        CharacterAttributes base = GameData.ENEMY_STATS;
        CharacterAttributes stats = new CharacterAttributes(
                base.vitality, vigor, base.agility, base.strength,
                base.intelligence, base.willpower, base.resonance, base.luck,
                base.minSpeed, base.maxSpeed, GameData.resistancesEnemy);
        return new Character(GameData.enemyProfile, stats,
                new org.example.core.character.Attributes.arsenal.CharacterEquipment(GameData.SWORD),
                new org.example.core.character.Attributes.arsenal.CharacterSkills(
                        List.of(GameData.basicAttack), GameData.evade));
    }
}
