# Progression : niveaux, statistiques et expérience

> **Status** : In Design
> **Author** : Mikhail + Claude
> **Last Updated** : 2026-11-22
> **Last Verified** : 2026-11-22
> **Implements Pillar** : Un monde à explorer et à apprendre ; on progresse en le vivant, pas en farmant

## Summary

Les personnages et les ennemis ont un niveau de 1 à 60, réparti sur 8 actes. Les statistiques grandissent avec le niveau selon la classe (et un peu le clan) du personnage, et avec l'équipement. L'XP vient de plusieurs sources (quête principale, quêtes secondaires, combats, exploration, bonus de style), toutes mesurées en « unités de récompense » qui grandissent moins vite que l'XP demandée : chaque niveau prend environ 5 fois plus de temps de jeu au niveau 59 qu'au niveau 1. Un ennemi trop faible ne rapporte plus d'XP, mais il lâche toujours son butin.

> **Quick reference** — Layer : `Foundation` · Priority : `Vertical Slice` · Key deps : `Combat, Équipement (à venir), Quêtes (à venir), Codex (à venir)`

## Overview

Chaque personnage, héros ou ennemi, a un **niveau**. Les statistiques d'un héros viennent de sa **classe** (croissance principale), de son **clan** (petit bonus) et de son **équipement** (8 emplacements, chaque objet a son propre niveau). Le monde est ouvert : on peut suivre l'histoire, faire des quêtes secondaires, explorer, ou retourner dans un acte précédent chercher un objet précis. Toutes ces activités rapportent de l'XP, avec des bonus pour qui joue son personnage selon sa personnalité (état mental) et réussit les quêtes avec style. Le seuil « gris » empêche de monter en écrasant des ennemis faibles, sans jamais empêcher d'y revenir pour leur butin. Le niveau maximum est **60**.

## Player Fantasy

« Je deviens plus fort parce que j'ai vécu quelque chose. » Le monde récompense toutes les façons de jouer : celui qui fonce dans l'histoire, celui qui fait chaque quête, celui qui fouille chaque recoin, celui qui revient traquer le vampire pour son collier. Les premiers niveaux arrivent vite et accrochent ; les derniers se méritent. Atteindre 60 est un accomplissement.

## Detailed Design

### Core Rules

**Niveaux et actes**

1. Tout personnage a un niveau entier entre 1 et `NIVEAU_MAX` (60).
2. Le jeu compte 8 actes, chacun avec une plage de niveaux visée :

   | Acte | Niveaux | Rôle |
   |---|---|---|
   | 1 | 1 → 20 | Découverte : niveaux rapides, on apprend ses outils |
   | 2 | 20 → 30 | |
   | 3 | 30 → 36 | |
   | 4 | aucun | Acte des personnages : histoire, pas de montée de niveau |
   | 5 | 36 → 44 | |
   | 6 | 44 → 50 | |
   | 7 | 50 → 58 | |
   | 8 | 58 → 60 | Final : deux niveaux qui se méritent |

3. Le niveau d'un ennemi est fixé par le contenu (zone, quête), pas par le niveau du joueur : le monde ne s'adapte pas.
4. Un personnage au niveau 60 ne gagne plus d'XP (voir Questions ouvertes).

**Statistiques**

5. Chaque **classe** de héros définit, pour chaque statistique, une valeur de base (niveau 1) et une croissance par niveau (formule S1). C'est la source principale de stats.
6. Chaque **clan** ajoute un **petit** bonus : quelques points sur une ou deux statistiques, ou un passif. Il ne doit jamais rendre une combinaison classe + clan obligatoire ; le choix du clan se fait par goût et par histoire.
7. Les ennemis n'ont ni classe ni clan : ils ont un **type** (Gnoll, Vampire…) avec ses propres base et croissance, selon la même formule.
8. L'**équipement** ajoute ses statistiques à celles du niveau. À équipement du niveau du personnage, viser environ **50 % des stats par le niveau, 50 % par l'équipement** (formule S3), pour que gagner un niveau et trouver un objet comptent tous les deux.
9. Les statistiques sont recalculées à chaque changement de niveau ou d'équipement ; les PV actuels gardent la même proportion des PV max (formule S2).

**Équipement (grandes lignes, détaillé dans un futur document)**

10. Huit emplacements : main principale, main secondaire, casque, torse, pantalon, pieds, collier, bague.
11. Chaque objet a **son propre niveau d'objet**, qui fixe son budget de statistiques, et un **niveau requis** pour l'équiper. Les objets ne changent pas de niveau.
12. L'arme de la main principale donne les dégâts de base ; les autres pièces donnent surtout des statistiques.

**Expérience : principes**

13. Toutes les récompenses d'XP s'expriment en **unités de récompense** du niveau de leur contenu (formule X2). Une unité = l'XP d'une quête principale de ce niveau.
14. Il faut de plus en plus d'unités pour monter d'un niveau (formule X3) : 1,5 au niveau 1, 7,9 au niveau 59. C'est ce qui rend les hauts niveaux plus longs **en temps de jeu**, pas seulement en chiffres.
15. Perdre ou fuir un combat ne rapporte aucune XP. Changer de niveau en plein combat est impossible : l'XP est distribuée à la fin.

**Expérience : sources**

16. **Quête principale** : 1 unité.
17. **Quête secondaire** : 0,4 unité.
18. **Combat** : 0,05 unité par ennemi normal, 0,15 par élite, 0,5 par boss, modifié par l'écart de niveau (formule X4). Un ennemi sous le **seuil gris** ne donne **aucune** XP.
19. **Exploration et découvertes** : 0,1 unité par lieu découvert, secret trouvé ou nouvelle fiche du Codex.
20. **Bonus de style en combat** : jouer selon sa personnalité (état mental) donne jusqu'à **+25 %** de l'XP du combat (formule X6). Exemples à préciser par personnalité : Rage = atteindre une rage élevée ; Foi = défendre au bon moment ; Calme = rester proche de 0.
21. **Bonus de quête** : objectifs optionnels réussis, choix fidèles à son clan ou à sa personnalité, solution cachée trouvée : jusqu'à **+30 %** de l'XP de la quête.
22. Les bonus ne sont **jamais des malus** : jouer autrement ne coûte rien, c'est seulement moins récompensé. L'expérimentation reste libre.
23. Une quête ou une découverte faite en retard (contenu de bas niveau) donne moins, mais **jamais moins de 25 %** (formule X5). Une bonne histoire ne doit jamais sembler « pour rien ».
24. Répartition visée sur une partie qui fait l'essentiel du contenu (à vérifier en jeu, elle dépend de la quantité de contenu) :

    | Source | Part visée |
    |---|---|
    | Quête principale | ~35 % |
    | Quêtes secondaires | ~30 % |
    | Combats | ~15 % |
    | Exploration et découvertes | ~10 % |
    | Bonus de style et de quête | ~10 % |

**Butin et retour dans les actes précédents**

25. Le butin est **indépendant de l'XP** : un ennemi gris lâche toujours son butin normalement. On peut revenir dans l'acte 2 chercher le collier de mana du vampire.
26. Pour que revenir en arrière ait du sens, certains objets doivent avoir quelque chose d'**unique** au-delà de leur niveau : effet spécial, pièce de set, apparence à collectionner, ou niveau qui suit celui du joueur pour quelques objets légendaires.
27. Compléter une fiche du **Codex** (toutes les compétences d'un ennemi vues, tout son butin trouvé) donne une récompense propre, qui n'est pas de l'XP.

### States and Transitions

| État | Transition | Condition |
|---|---|---|
| Niveau L (L < 60) | → Niveau L+1 | XP accumulée ≥ `xpPourNiveau(L)` ; l'XP en trop est conservée |
| Niveau L (L < 60) | → plusieurs niveaux d'un coup | Une grosse récompense applique la transition en boucle |
| Niveau 60 | (aucune) | L'XP n'est plus comptée |

### Interactions with Other Systems

- **Combat** : les statistiques calculées (niveau + classe + clan + équipement) remplacent les valeurs fixes actuelles de `GameData`. Le combat lui-même ne change pas.
- **Défense** : la Vigueur grandit avec le niveau et l'équipement, donc Guard (2 × Vigueur) et Contre (Vigueur / 2) restent utiles à tous les niveaux (cible S4).
- **États mentaux** : mesurent le bonus de style (`MentalState.getValue()` existe déjà).
- **Équipement** (à venir) : budget de stats par niveau d'objet, niveau requis, objets uniques.
- **Quêtes et missions** (à venir) : principale source d'XP, bonus de quête.
- **Codex des ennemis** (à venir) : fiches qui donnent de l'XP de découverte et des récompenses de complétion.

## Formulas

**S1. Statistique de classe au niveau L**
```
stat(L) = base + floor(croissance × (L − 1)) + bonusClan
```
Linéaire volontairement : PV et dégâts grandissent au même rythme, donc la durée d'un combat à niveau égal reste stable du niveau 1 au niveau 60. Une croissance de 0,5 donne +1 tous les 2 niveaux.

**S2. PV après un changement de niveau ou d'équipement**
```
pvActuels = round(pvActuels × pvMax(après) / pvMax(avant))
```

**S3. Part niveau / équipement (règle de réglage)**
```
statsÉquipement(objets du niveau L) ≈ statsNiveau(L)
```

**S4. Cible de Vigueur (règle de réglage)**
```
vigueur totale(L) ≈ 10 à 15 % d'une Attaque de base d'un adversaire de même niveau
```
Guard retire alors 20 à 30 % d'un coup ; la Vigueur hors défense reste visible sans rendre invulnérable.

**X1. XP nécessaire pour passer du niveau L à L+1**
```
xpPourNiveau(L) = round(100 × L^1,5)
```
Total de 1 à 60 : 1 092 274 XP.

**X2. Unité de récompense au niveau L**
```
unité(L) = round(65 × L^1,1)
```
Grandit moins vite que X1 : c'est ce décalage qui allonge les hauts niveaux.

**X3. Unités nécessaires par niveau (conséquence de X1 et X2)**
```
unitésParNiveau(L) = xpPourNiveau(L) / unité(L) ≈ 1,5 × L^0,4
```
| Niveau | XP pour le niveau suivant | 1 unité | Unités par niveau |
|---|---|---|---|
| 1 | 100 | 65 | 1,5 |
| 5 | 1 118 | 382 | 2,9 |
| 10 | 3 162 | 818 | 3,9 |
| 20 | 8 944 | 1 754 | 5,1 |
| 30 | 16 432 | 2 740 | 6,0 |
| 40 | 25 298 | 3 760 | 6,7 |
| 50 | 35 355 | 4 806 | 7,4 |
| 59 | 45 319 | 5 766 | 7,9 |

Taille de chaque acte, en unités (≈ quêtes principales équivalentes) :

| Acte | Niveaux | Unités | Unités par niveau |
|---|---|---|---|
| 1 | 1 → 20 | 70 | 3,7 |
| 2 | 20 → 30 | 55 | 5,5 |
| 3 | 30 → 36 | 37 | 6,2 |
| 4 | aucun | — | — |
| 5 | 36 → 44 | 54 | 6,7 |
| 6 | 44 → 50 | 43 | 7,1 |
| 7 | 50 → 58 | 60 | 7,6 |
| 8 | 58 → 60 | 16 | 7,8 |
| **Total** | 1 → 60 | **335** | |

Ce tableau sert à dimensionner le contenu : l'acte 1 doit offrir environ 70 unités (par exemple 25 quêtes principales + 60 secondaires + combats et découvertes), l'acte 8 environ 16.

**X4. Modificateur d'écart de niveau (combat)**
```
écart     = niveauEnnemi − niveauJoueur
seuilGris = niveauJoueur − (3 + floor(niveauJoueur / 10))

si niveauEnnemi < seuilGris     → 0 %
sinon si écart ≥ +3             → 120 % (plafond)
sinon si écart ≥ −2             → 100 %
sinon                           → max(20 %, 100 % − 20 % × (−2 − écart))
```
| Niveau joueur | Ennemi « gris » (0 XP) sous le niveau |
|---|---|
| 5 | 2 |
| 10 | 6 |
| 20 | 15 |
| 30 | 24 |
| 40 | 33 |
| 50 | 42 |
| 60 | 51 |

Exemple : joueur 20, ennemi 16 (écart −4) → 60 %. Ennemi 14 → gris, 0 XP (mais butin normal).

**X5. Modificateur de retard (quêtes et découvertes)**
```
retard = niveauJoueur − niveauContenu
si retard ≤ 2 → 100 %
sinon         → max(25 %, 100 % − 15 % × (retard − 2))
```

**X6. XP d'un combat gagné**
```
xpCombat = Σ (unité(niveauEnnemi) × rangEnnemi × modificateurÉcart)
         × (1 + bonusStyle)

rangEnnemi : 0,05 normal · 0,15 élite · 0,5 boss
bonusStyle : de 0 à 0,25 selon la personnalité jouée
```

**X7. XP d'une quête terminée**
```
xpQuête = unité(niveauQuête) × typeQuête × modificateurRetard × (1 + bonusQuête)

typeQuête  : 1,0 principale · 0,4 secondaire
bonusQuête : de 0 à 0,30
```

## Edge Cases

- **Grosse récompense** : peut faire passer plusieurs niveaux ; l'XP restante est conservée.
- **Niveau 60** : l'XP gagnée est ignorée (en attendant les Questions ouvertes).
- **Ennemi plus fort de 3 niveaux ou plus** : 120 % maximum, pour récompenser le risque sans pousser au suicide.
- **Retour dans un acte précédent** : 0 XP sur les ennemis gris, mais butin, découvertes (25 % minimum) et fiches du Codex restent récompensés.
- **Joueur en avance sur l'histoire** (il a fait beaucoup de contenu secondaire) : la quête principale doit garder du défi ; les boss d'histoire ont un niveau au plafond de l'acte.
- **Bonus de style sur ennemi gris** : 0 × bonus = 0. On ne peut pas farmer son style sur des ennemis faibles.
- **Acte 4** : pas de montée de niveau ; la progression passe par l'histoire, l'équipement, le Codex et les relations entre personnages.
- **Croissance décimale** : `floor` évite les demi-points.
- **Ennemi très faible face à une forte Vigueur** : peut ne faire aucun dégât ; c'est voulu (ennemi gris = inoffensif).

## Dependencies

- **Character / CharacterAttributes** : ajout du niveau, de la classe, du clan et du calcul des stats (aujourd'hui des valeurs fixes dans `GameData`).
- **GameData** : devient une table de classes, clans et types d'ennemis (base + croissance) au lieu de stats figées.
- **Duel** : distribue l'XP de combat à la fin d'une victoire.
- **Équipement, Quêtes, Codex** : n'existent pas encore ; chacun aura son document.

## Tuning Knobs

| Réglage | Défaut | Plage | Catégorie | Pourquoi |
|---|---|---|---|---|
| `NIVEAU_MAX` | 60 | — | gate | Plafond « mythique », réparti sur 8 actes |
| Plages des actes | voir règle 2 | — | gate | Rythme de l'histoire |
| Exposant de la courbe d'XP (X1) | 1,5 | 1,2–2,0 | curve | Taille des chiffres |
| Exposant de l'unité (X2) | 1,1 | 0,9–1,3 | curve | Plus il est bas, plus les hauts niveaux sont longs |
| Base de l'unité (X2) | 65 | — | curve | 1,5 quête principale pour le premier niveau |
| Quête principale / secondaire | 1 / 0,4 unité | — | curve | Histoire ≈ 35 %, secondaires ≈ 30 % |
| Ennemi normal / élite / boss | 0,05 / 0,15 / 0,5 unité | — | curve | Combats ≈ 15 % |
| Découverte | 0,1 unité | 0,05–0,2 | feel | Exploration ≈ 10 % |
| Bonus de style (combat) | +25 % max | 0–50 % | feel | Coup de pouce, pas obligation |
| Bonus de quête | +30 % max | 0–50 % | feel | Récompense les quêtes bien faites |
| Bonus d'écart positif | 120 % max | 100–150 % | feel | Récompense le risque |
| Seuil gris | `L − (3 + L/10)` | — | gate | Empêche le farm, s'élargit avec le niveau |
| Perte par niveau sous l'écart | 20 % | 10–30 % | curve | Décroissance avant le gris |
| Plancher retard (quêtes, découvertes) | 25 % | 0–50 % | feel | Jamais « pour rien » |
| Part niveau / équipement | 50 / 50 | 30/70–70/30 | curve | Niveau et objets comptent tous les deux |
| Bonus de clan | petit (1–2 stats ou un passif) | — | feel | Jamais de combinaison obligatoire |
| Cible Vigueur | 10–15 % d'un coup égal | — | feel | Défense utile à tous les niveaux |

Toutes ces valeurs vivent dans des données (aujourd'hui `GameData`, plus tard des fichiers de données), jamais en dur dans la logique.

## Visual/Audio Requirements

- Montée de niveau marquée (son et message distincts), y compris en console.
- Annonce du bonus de style en fin de combat (« Rage maîtrisée : +18 % XP »).

## Game Feel

- Monter de niveau doit arriver **après** quelque chose de mémorable : fin de quête, boss, combat serré, découverte.
- Un ennemi gris se lit comme « plus de défi ici », pas comme une punition : son butin reste intéressant.
- Les premiers niveaux s'enchaînent ; à partir de l'acte 5, chaque niveau se sent comme une étape.

## UI Requirements

- Niveau de chaque combattant à côté de son nom.
- Barre d'XP du joueur (XP actuelle / XP pour le niveau suivant).
- Ennemis gris signalés (couleur ou mention « trop faible, 0 XP »).
- Écran de fin de combat : XP de base, modificateurs, bonus de style.

## Cross-References

- Règles de défense et budget d'actions (commit `7485965`).
- Équipement (document à écrire).
- Quêtes et missions (document à écrire).
- Codex des ennemis (document à écrire).
- Défense selon l'état mental (document à écrire).

## Acceptance Criteria

- [ ] Les statistiques d'un héros au niveau L suivent S1 (classe + clan) ; celles d'un ennemi suivent S1 avec son type.
- [ ] Passer de niveau ou changer d'équipement garde la même proportion de PV (S2).
- [ ] `xpPourNiveau` et `unité` donnent les valeurs des tableaux X1 et X3.
- [ ] Un ennemi sous le seuil gris donne 0 XP mais lâche son butin ; à écart −4 au niveau 20, 60 % ; à +5, 120 %.
- [ ] Le bonus de style ajoute au plus 25 % à l'XP d'un combat, et 0 sur un ennemi gris.
- [ ] Une quête terminée avec 10 niveaux de retard donne 25 % de son XP de base.
- [ ] Au niveau 60, l'XP gagnée ne change plus rien.
- [ ] Une grosse récompense peut faire passer plusieurs niveaux et conserve le reste.
- [ ] À niveau et équipement égaux, un combat 1 contre 1 dure environ 4 à 6 tours (validé en jouant).
- [ ] À niveau égal, Guard retire environ 20 à 30 % d'une Attaque de base (validé en jouant).
- [ ] Une partie complète, en jouant, approche la répartition visée de la règle 24.

## Open Questions

1. **Que devient l'XP au niveau 60 ?** Perdue, ou convertie (points de talent, monnaie, réputation de clan) ?
2. **Plafond dur par acte ?** Recommandation : non, un plafond « souple » suffit (ennemis gris, quêtes en retard). À confirmer.
3. **Que rapporte l'acte 4 ?** Pas de niveaux : quelle progression le remplace (objets, Codex, relations, états mentaux) ? L'XP gagnée pendant l'acte 4 est-elle conservée ?
4. **Valeurs de base et croissances de chaque classe, clan et type d'ennemi** : à fixer avec les compétences des personnages. Points connus à corriger : la Vigueur vaut 0 partout, un combat à niveau égal dure 2 à 3 tours, et le Mutant frappe trop fort.
5. **Que devient la résonance ?** Aujourd'hui elle multiplie l'arme ; avec l'équipement, en faire une statistique donnée par les objets ?
6. **Critères exacts du bonus de style** pour chaque personnalité (Rage, Foi, Calme, Honneur, Instinct).
7. **XP de groupe** : quand il y aura des équipes, l'XP est-elle partagée, ou chaque membre reçoit-il la totalité ?
