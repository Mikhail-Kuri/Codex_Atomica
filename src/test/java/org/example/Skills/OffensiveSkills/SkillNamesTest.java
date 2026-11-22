package org.example.Skills.OffensiveSkills;

import org.example.app.data.GameData;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;

/** Les compétences portent un vrai nom, affichable dans un menu (avant : « 1 », « 2 », « 3 »). */
class SkillNamesTest {

    @Test
    @DisplayName("Bug : les compétences s'appelaient « 1 », « 2 », « 3 »")
    void skillsHaveReadableNames() {
        for (OffensiveSkill skill : List.of(GameData.basicAttack, GameData.radStrike, GameData.selfAttack)) {
            assertFalse(skill.getName().isBlank(), "nom vide");
            assertFalse(skill.getName().matches("\\d+"), "nom numérique : " + skill.getName());
        }
    }
}
