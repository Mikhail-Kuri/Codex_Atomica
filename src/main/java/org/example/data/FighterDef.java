package org.example.data;

import org.example.Skills.Scaling.DamageType;
import org.example.gameplay.mental.MentalStateType;

import java.util.List;
import java.util.Map;

/**
 * Une classe de héros (classes.json) ou un type d'ennemi (enemies.json).
 *
 * @param rank        rang d'ennemi ; absent pour une classe de héros
 * @param baseStats   valeurs au niveau 1 ; les 10 statistiques sont obligatoires
 * @param growth      gain par niveau ; une statistique absente ne grandit pas
 * @param resistances multiplicateur de dégâts reçus par type ; 1.0 si absent
 * @param skills      identifiants des compétences offensives
 * @param defense     identifiant de la compétence défensive
 * @param weapon      identifiant de l'arme équipée au départ
 */
public record FighterDef(
        String id,
        String name,
        EnemyRank rank,
        MentalStateType mentalState,
        Map<Stat, Double> baseStats,
        Map<Stat, Double> growth,
        Map<DamageType, Float> resistances,
        List<String> skills,
        String defense,
        String weapon
) {}
