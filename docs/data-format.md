# Format des données de jeu (JSON)

> **Statut** : brouillon · **Date** : 2026-11-22
> **But** : décrire en données tout le contenu du jeu (objets, classes, ennemis, compétences, effets, états mentaux, réglages), pour qu'ajouter ou équilibrer du contenu ne demande plus de toucher au Java, et que ces données passent telles quelles vers Godot ou Unity.

## Principes

1. **Les classes Java restent ; leurs valeurs viennent des fichiers.** Un chargeur lit les JSON au démarrage et construit les mêmes objets qu'aujourd'hui (`Weapon`, `Character`, compétences…).
2. **Tout est référencé par un identifiant** (`"id": "iron_sword"`), jamais par un nom affiché. Les noms peuvent changer ; les identifiants, jamais.
3. **Les mécanismes sont des briques à combiner.** Une compétence n'est pas une classe : c'est une liste de briques (`DAMAGE`, `APPLY_STATUS`, `SELF_DAMAGE`…). Une nouvelle idée qui combine des briques existantes ne demande **aucun** code. Une idée vraiment nouvelle demande **une** nouvelle brique en Java, réutilisable ensuite partout.
4. **Validation au chargement.** Un identifiant inconnu, une valeur hors plage ou un champ mal écrit arrête le jeu avec un message clair (« skills.json : la compétence `rad_strike` cite le statut inconnu `burnn` »), plutôt qu'un bug silencieux en combat.
5. **Les listes fixes restent des `enum` Java** (types de dégâts, statistiques, emplacements…). Le JSON les cite par leur nom ; une faute de frappe est détectée au chargement.

## Fichiers

Tous dans `src/main/resources/data/` :

| Fichier | Contenu |
|---|---|
| `tuning.json` | Réglages globaux : niveaux, XP, états mentaux, combat |
| `acts.json` | Les 8 actes et leurs plages de niveaux |
| `damage_types.json` | Ce que chaque type de dégât déclenche (effet de statut…) |
| `status_effects.json` | Brûlure, Saignement, et tous les futurs effets |
| `mental_states.json` | Les 5 personnalités : gains par événement, style, défense |
| `skills.json` | Toutes les compétences, offensives et défensives |
| `items.json` | Tous les objets, armes comprises, et les sets |
| `classes.json` | Classes de héros : stats de base, croissance, compétences |
| `clans.json` | Les clans et leurs petits bonus |
| `enemies.json` | Types d'ennemis : stats, compétences, IA, Codex, butin |
| `quests.json` | Quêtes (squelette, pour plus tard) |

## Listes fixes (enum Java citées par le JSON)

| Liste | Valeurs |
|---|---|
| Statistiques | `VITALITY`, `VIGOR`, `AGILITY`, `STRENGTH`, `INTELLIGENCE`, `WILLPOWER`, `RESONANCE`, `LUCK`, `MIN_SPEED`, `MAX_SPEED` |
| Types de dégâts | `PHYSICAL`, `SLASHING`, `CRUSHING`, `PIERCING`, `RADIATION`, `ENERGY`, `ARCANE`, `FIRE`, `VOID`, `BLEEDING`, `POISON`, `PSYCHIC`, `EXPLOSIVE`, `ELECTRIC`, `FROST`, `SHOCK`, `HOLY`, `UNHOLY`, `NECROTIC`, `PSIONIC`, `SONIC`, `GRAVITY`, `TIME`, `CHAOS`, `PURE`, `NULL` |
| Portées | `MELEE`, `SHORT`, `MEDIUM`, `LONG` |
| Mise à l'échelle | `STRENGTH`, `INTELLIGENCE`, `VITALITY`, `VIGOR`, `AGILITY`, `WEAPON` (puissance de l'arme × résonance), `SKILL` (puissance de base de la compétence) |
| Cibles | `ENEMY`, `ALLY`, `SELF`, `ANY` |
| Événements de combat | `DAMAGE_DEALT`, `DAMAGE_RECEIVED`, `DEFENSE_PREPARED`, `ATTACK_MISSED`, `ATTACK_BLOCKED`, `ENEMY_DEFEATED`, `ALLY_DEFEATED`, `TURN_STARTED`, `TURN_ENDED`, `SELF_DAMAGE` |
| Emplacements | `MAIN_HAND`, `OFF_HAND`, `HEAD`, `TORSO`, `LEGS`, `FEET`, `NECK`, `RING` |
| Rangs d'ennemi | `NORMAL`, `ELITE`, `BOSS` |
| Raretés | `COMMON`, `UNCOMMON`, `RARE`, `EPIC`, `LEGENDARY` |

## `tuning.json` — réglages globaux

Toutes les valeurs du document de progression et celles aujourd'hui écrites en dur dans le code.

```json
{
  "levels": {
    "max": 60,
    "xpCurve": { "base": 100, "exponent": 1.5 },
    "rewardUnit": { "base": 65, "exponent": 1.1 }
  },
  "xp": {
    "quest": { "MAIN": 1.0, "SIDE": 0.4 },
    "enemyRank": { "NORMAL": 0.05, "ELITE": 0.15, "BOSS": 0.5 },
    "discovery": 0.1,
    "levelGap": {
      "grayOffset": 3, "grayPerLevels": 10,
      "fullRange": 2, "lossPerLevel": 0.20, "minBeforeGray": 0.20,
      "aboveBonusFrom": 3, "aboveBonus": 1.20
    },
    "lateContent": { "graceLevels": 2, "lossPerLevel": 0.15, "floor": 0.25 },
    "styleBonusMax": 0.25,
    "questBonusMax": 0.30
  },
  "mental": {
    "min": -20, "max": 20,
    "normalizeDivisor": 10.0,
    "outgoingDamageSwing": 0.4,
    "incomingDamageSwing": 0.3
  },
  "combat": {
    "defaultActionsPerTurn": 1,
    "maxDefensesPerTurn": 1,
    "mitigatedHitGainScale": 0.5,
    "priorities": { "DEFENSE": 100, "ATTACK": 1 },
    "maxDuelTurns": 50,
    "refusedChoiceRetries": 3
  }
}
```

## `acts.json`

```json
[
  { "id": "act_1", "number": 1, "name": "…", "levels": { "from": 1,  "to": 20 } },
  { "id": "act_4", "number": 4, "name": "…", "levels": null, "note": "Acte des personnages : pas de montée de niveau" },
  { "id": "act_8", "number": 8, "name": "…", "levels": { "from": 58, "to": 60 } }
]
```

## `damage_types.json`

Remplace `StatusEffectFactory` : ce qu'un coup de ce type applique s'il fait des dégâts.

```json
{
  "FIRE":     { "appliesStatus": [ { "status": "burn",  "chance": 1.0 } ] },
  "BLEEDING": { "appliesStatus": [ { "status": "bleed", "chance": 1.0 } ] }
}
```
- `chance` : probabilité d'appliquer l'effet (1.0 = toujours, comme aujourd'hui).
- Un type absent du fichier n'applique rien.

## `status_effects.json`

```json
[
  {
    "id": "burn", "name": "Brûlure",
    "duration": 2,
    "stacking": "ADD_DURATION",
    "maxDuration": null,
    "bypassesDefense": true,
    "bypassesVigor": true,
    "onTurnEnd": [ { "type": "DAMAGE", "amount": 4, "damageType": "FIRE" } ],
    "onTurnStart": [],
    "statModifiers": [],
    "tags": ["DOT", "FIRE"]
  },
  {
    "id": "bleed", "name": "Saignement",
    "duration": 3,
    "stacking": "ADD_DURATION",
    "bypassesDefense": true,
    "bypassesVigor": true,
    "onTurnEnd": [ { "type": "DAMAGE", "amount": 5, "damageType": "BLEEDING" } ]
  }
]
```
- `stacking` : `ADD_DURATION` (comportement actuel : on additionne les durées), `REFRESH` (remet la durée au maximum), `STACK_INTENSITY` (les dégâts s'additionnent), `IGNORE` (un second effet identique est ignoré).
- `statModifiers` : effets de type « faiblesse » ou « bénédiction », ex. `{ "stat": "STRENGTH", "add": -3 }` ou `{ "stat": "VIGOR", "multiply": 1.5 }`.
- `tags` : servent aux immunités (« immunisé aux effets `FIRE` »).

## `mental_states.json`

Remplace les 5 classes `…MentalState` et leurs `switch`.

```json
[
  {
    "id": "RAGE", "name": "Rage",
    "deltas": {
      "DAMAGE_DEALT": 2, "DAMAGE_RECEIVED": 1, "ALLY_DEFEATED": 3,
      "ENEMY_DEFEATED": -1, "DEFENSE_PREPARED": -1
    },
    "defaultDelta": 1,
    "style": { "type": "PEAK_VALUE", "target": 10 },
    "onDefend": []
  },
  {
    "id": "FAITH", "name": "Foi",
    "deltas": { "DAMAGE_DEALT": 2, "DAMAGE_RECEIVED": 1, "ALLY_DEFEATED": 3,
                "ENEMY_DEFEATED": -1, "DEFENSE_PREPARED": -1 },
    "defaultDelta": 1,
    "style": { "type": "DEFEND_BEFORE_HIT", "target": 2 },
    "onDefend": []
  }
]
```
- `deltas` : gain ou perte par événement ; `defaultDelta` pour les autres (aujourd'hui 1).
- `style` : critère du bonus de style (règle 20 de la progression). Types prévus : `PEAK_VALUE` (atteindre une valeur), `STAY_NEAR_ZERO` (rester dans ±N), `DEFEND_BEFORE_HIT` (nombre de défenses qui ont vraiment servi), `END_ABOVE` (finir le combat au-dessus de N).
- `onDefend` : la future **défense propre à chaque personnalité**, avec les mêmes briques que les compétences (ex. Foi : `{ "type": "MENTAL", "amount": 2 }`).

## `skills.json` — compétences

Une seule structure pour toutes les compétences, offensives et défensives.

**Offensive**
```json
{
  "id": "rad_strike", "name": "Frappe radioactive",
  "kind": "OFFENSIVE",
  "target": "ENEMY",
  "priority": 1,
  "basePower": 11,
  "scaling": [ { "stat": "STRENGTH", "ratio": 1.5 }, { "stat": "SKILL", "ratio": 1.0 } ],
  "damageTypes": ["RADIATION", "PHYSICAL"],
  "effects": [ { "type": "DAMAGE", "target": "TARGET" } ],
  "cooldown": 0,
  "codex": { "intentIcon": "heavy_attack", "description": "Un coup chargé de radiations." }
}
```

- `scaling.ratio` : contient aussi le ×1,5 aujourd'hui écrit en dur dans `ScalingType` (`STRENGTH` → Force × 1,5). Les deux exemples donnent donc les mêmes dégâts qu'aujourd'hui.

**Frappe sacrificielle** (blesse aussi le lanceur)
```json
{
  "id": "sacrificial_strike", "name": "Frappe sacrificielle",
  "kind": "OFFENSIVE", "target": "ANY", "priority": 1, "basePower": 0,
  "scaling": [ { "stat": "STRENGTH", "ratio": 1.5 } ],
  "damageTypes": ["PHYSICAL"],
  "effects": [
    { "type": "DAMAGE", "target": "TARGET" },
    { "type": "SELF_DAMAGE", "ratio": 1.0 }
  ]
}
```

**Défensives** : le champ `defense` décrit la formule, aujourd'hui écrite dans `Guard`, `Evade` et `Counter`.
```json
[
  { "id": "guard", "name": "Guard", "kind": "DEFENSIVE", "target": "SELF", "priority": 100,
    "defense": { "type": "REDUCE_FLAT", "vigorMultiplier": 2.0, "applyIncomingMental": true } },

  { "id": "evade", "name": "Evade", "kind": "DEFENSIVE", "target": "SELF", "priority": 100,
    "defense": { "type": "DODGE_RELATIVE", "maxShare": 0.80, "luckPerPoint": 0.005,
                 "minChance": 0.05, "maxChance": 0.75 } },

  { "id": "counter", "name": "Contre", "kind": "DEFENSIVE", "target": "SELF", "priority": 100,
    "defense": { "type": "COUNTER", "vigorReductionFactor": 0.5,
                 "counterDamage": [ { "stat": "VIGOR", "ratio": 1.0 }, { "stat": "WEAPON", "ratio": 1.0 } ],
                 "fallbackWeaponPower": 5 } }
]
```
- `DODGE_RELATIVE` (Esquive) : `chance = maxShare × Agi défenseur ÷ (Agi défenseur + Agi attaquant) + luckPerPoint × Chance`, bornée entre `minChance` et `maxChance`. Comme les deux Agilités grandissent avec le niveau, la chance reste stable du niveau 1 au niveau 60. En attendant `skills.json`, ces valeurs vivent dans `tuning.json` (`combat.evade`).

**Briques d'effet** (`effects`, utilisables aussi dans les statuts, les objets et les états mentaux) :

| Brique | Paramètres | Ce qu'elle fait |
|---|---|---|
| `DAMAGE` | `target`, `multiplier` | Dégâts calculés par `scaling` et `damageTypes` |
| `SELF_DAMAGE` | `ratio` | Le lanceur subit une part des dégâts |
| `APPLY_STATUS` | `status`, `chance`, `target` | Applique un effet de statut |
| `HEAL` | `amount` ou `scaling`, `target` | Soigne |
| `MENTAL` | `amount`, `target` | Change l'état mental |
| `EXTRA_ACTION` | `amount` | Donne des actions en plus ce tour (rogue rapide) |
| `MODIFY_STAT` | `stat`, `add`/`multiply`, `duration` | Buff ou débuff temporaire |
| `CLEANSE` | `tags` | Retire des effets (ex. tous les `DOT`) |

Les cibles d'une brique : `TARGET` (la cible de la compétence), `SELF`, plus tard `ALLY_*` avec les équipes.

## `items.json` — objets et sets

```json
{
  "items": [
    {
      "id": "iron_sword", "name": "Épée en fer",
      "slot": "MAIN_HAND", "itemLevel": 5, "requiredLevel": 3, "rarity": "COMMON",
      "weapon": { "basePower": 35, "damageTypes": ["PHYSICAL"], "range": "MELEE" },
      "stats": [],
      "effects": [],
      "setId": null,
      "appearance": "sword_iron",
      "scalesWithWearer": false
    },
    {
      "id": "vampire_mana_necklace", "name": "Collier de mana du vampire",
      "slot": "NECK", "itemLevel": 24, "requiredLevel": 22, "rarity": "RARE",
      "stats": [ { "stat": "INTELLIGENCE", "add": 6 }, { "stat": "RESONANCE", "add": 0.2 } ],
      "effects": [
        { "trigger": "ON_ENEMY_DEFEATED", "type": "HEAL", "amount": 5, "target": "SELF" }
      ],
      "setId": "night_court",
      "appearance": "necklace_vampire",
      "scalesWithWearer": false
    }
  ],
  "sets": [
    { "id": "night_court", "name": "Cour de la Nuit",
      "bonuses": [ { "pieces": 2, "stats": [ { "stat": "LUCK", "add": 3 } ] } ] }
  ]
}
```
- `weapon` : présent seulement pour une arme (main principale ou secondaire).
- `stats` : bonus fixes de l'objet (`add` ou `multiply`).
- `effects` + `trigger` : effets spéciaux qui rendent un objet unique. Déclencheurs prévus : `ON_HIT`, `ON_HIT_TAKEN`, `ON_DEFEND`, `ON_ENEMY_DEFEATED`, `ON_TURN_START`, `ON_TURN_END`.
- `scalesWithWearer` : pour quelques légendaires dont le niveau suit le porteur.

## `classes.json` — classes de héros

```json
[
  {
    "id": "paladin", "name": "Paladin du Noyau",
    "mentalState": "FAITH",
    "baseStats":  { "VITALITY": 120, "VIGOR": 0, "AGILITY": 10, "STRENGTH": 5,
                    "INTELLIGENCE": 15, "WILLPOWER": 10, "RESONANCE": 1.5, "LUCK": 5,
                    "MIN_SPEED": 3, "MAX_SPEED": 8 },
    "growth":     { "VITALITY": 0, "VIGOR": 0, "AGILITY": 0, "STRENGTH": 0,
                    "INTELLIGENCE": 0, "WILLPOWER": 0, "RESONANCE": 0, "LUCK": 0,
                    "MIN_SPEED": 0, "MAX_SPEED": 0 },
    "resistances": { "HOLY": 0.2, "PHYSICAL": 0.7, "NECROTIC": 1.5, "VOID": 1.3 },
    "skills": ["basic_attack", "rad_strike"],
    "defense": "guard",
    "actionsPerTurn": 1,
    "startingEquipment": { "MAIN_HAND": "iron_sword" }
  }
]
```
- `growth` : gain par niveau (formule S1 de la progression). À zéro dans cet exemple : **les valeurs restent à fixer**.
- `resistances` : multiplicateur de dégâts reçus par type (1.0 si absent).

## `clans.json`

```json
[
  { "id": "clan_example", "name": "…",
    "statBonus": [ { "stat": "VIGOR", "add": 2 } ],
    "passive": [ { "trigger": "ON_DEFEND", "type": "MENTAL", "amount": 1, "target": "SELF" } ] }
]
```

## `enemies.json` — types d'ennemis

```json
[
  {
    "id": "irradiated_gnoll", "name": "Gnoll Irradié",
    "rank": "NORMAL",
    "mentalState": "INSTINCT",
    "baseStats":  { "VITALITY": 50, "VIGOR": 0, "AGILITY": 5, "STRENGTH": 5,
                    "INTELLIGENCE": 2, "WILLPOWER": 2, "RESONANCE": 1.0, "LUCK": 3,
                    "MIN_SPEED": 3, "MAX_SPEED": 5 },
    "growth":     { "VITALITY": 0, "VIGOR": 0, "STRENGTH": 0 },
    "resistances": { "PHYSICAL": 0.8, "SLASHING": 0.9, "RADIATION": 1.2 },
    "skills": ["basic_attack"],
    "defense": "evade",
    "weapon": "iron_sword",
    "actionsPerTurn": 1,
    "ai": {
      "type": "SIMPLE",
      "defendChance": 0.25,
      "pattern": [],
      "targeting": "RANDOM"
    },
    "codex": {
      "lore": "…",
      "revealedBy": { "SKILL_SEEN": "skill", "STATUS_SUFFERED": "status", "DEFEATED": "lore" }
    },
    "loot": [
      { "item": "iron_sword", "chance": 0.05 }
    ]
  }
]
```
- `rank` : rang pour l'XP (normal, élite, boss).
- Le **niveau** n'est pas ici : il est fixé là où l'ennemi apparaît (zone, quête).
- `ai.pattern` : pour les rythmes propres à un ennemi, ex. `[ { "everyTurns": 3, "skill": "heavy_bite" } ]` (« frappe fort tous les 3 tours »).
- `ai.targeting` : `RANDOM`, `LOWEST_HP`, `HIGHEST_THREAT`… (utile avec les équipes ; c'est aussi ce que le Codex fait apprendre).
- `codex.revealedBy` : ce qui révèle quoi dans le Codex.
- `loot` : indépendant de l'XP ; un ennemi gris lâche toujours son butin.

## `quests.json` (squelette)

```json
[
  { "id": "act1_main_01", "act": 1, "type": "MAIN", "level": 3, "name": "…",
    "bonusObjectives": [ { "id": "no_damage_taken", "bonus": 0.10 } ],
    "rewards": { "items": [], "codex": [] } }
]
```
L'XP n'est pas écrite à la main : elle se calcule avec le niveau et le type (formule X7 de la progression).

## Ce que ce format couvre

| Mécanisme actuel ou prévu | Où il vit |
|---|---|
| Stats, résistances, vitesse | `classes.json`, `enemies.json` |
| Croissance par niveau, classe, clan | `classes.json`, `clans.json` |
| Armes, 8 emplacements, niveau d'objet, sets, objets uniques | `items.json` |
| Compétences et leurs mises à l'échelle | `skills.json` |
| Guard, Esquive, Contre et leurs formules | `skills.json` (`defense`) |
| Priorités, budget d'actions, personnages rapides | `tuning.json`, `classes.json`, brique `EXTRA_ACTION` |
| Brûlure, Saignement, cumul des durées | `status_effects.json`, `damage_types.json` |
| États mentaux, gains, multiplicateurs, bornes | `mental_states.json`, `tuning.json` |
| Défense propre à chaque personnalité | `mental_states.json` (`onDefend`) |
| Bonus de style | `mental_states.json` (`style`) |
| IA, rythmes d'ennemis, ciblage | `enemies.json` (`ai`) |
| Codex et intentions | `enemies.json` (`codex`), `skills.json` (`codex`) |
| Butin séparé de l'XP | `enemies.json` (`loot`) |
| Niveaux, courbe d'XP, seuil gris, bonus | `tuning.json` |
| Actes | `acts.json` |
| Quêtes et objectifs bonus | `quests.json` |

## Ce qui demandera encore du Java

Le JSON combine des mécanismes ; il n'en invente pas. Il faudra du code pour :
- chaque **nouvelle brique** d'effet ou nouveau **déclencheur** (une seule fois, puis réutilisable partout) ;
- chaque **nouveau type** de défense, de critère de style ou d'IA ;
- les systèmes qui n'existent pas encore : équipement, quêtes, Codex, équipes.

## Questions ouvertes

1. **Héros ou ennemis ?** Dans les exemples, Paladin, Éclaireur et Mutant sont des classes de héros, Gnoll et Rat des ennemis. Est-ce juste ?
2. **Santé mentale** : `CharacterState` a un champ `currentSanity` jamais utilisé. Doit-il devenir une statistique ?
3. **Coûts des compétences** : mana, recharge (`cooldown`), ressource de clan ? Le champ `cooldown` est prévu, rien d'autre.
4. **Langue des données** : identifiants en anglais (`iron_sword`) et noms affichés en français, ou noms traduisibles plus tard (fichier de textes séparé) ?
