# Codex Atomica — project brief (read first)

Turn-based RPG, post-apocalyptic Celtic fantasy, 3 clans, 8 acts, level cap 60. Today: a **Java 21 console prototype** of the combat/progression logic; a later port to **Godot or Unity (C#)** is planned, so keep logic engine-agnostic. Owner speaks French; game content is English.

## Run / test
- Build: Maven (`pom.xml`). No `mvn` on PATH: use IntelliJ's `C:\Program Files\JetBrains\IntelliJ IDEA 2026.3\plugins\maven-plugin\lib\maven3\bin\mvn.cmd`.
- Tests: `mvn test` (JUnit 5, ~73 tests, must stay green and deterministic).
- Play: run `org.example.app.Main` (IntelliJ ▶). CLI needs Jackson jars on the classpath. `--debug` shows calculation details.

## Code map (`src/main/java/org/example/`)
| Package | Role |
|---|---|
| `app.Main` | Console launcher: pick fighter + level, opponent + level, duel, XP reward |
| `app.console` | `ConsoleActionChooser` (player menu), `ConsoleInput` (numbered choice), `InputClosedException` |
| `app.data.GameData` | Static facade over loaded content (`CONTENT`, `TUNING`, `createPaladin()`…). Kept for old callers |
| `app.GameTester` | Manual dev scenarios (French, not game code) |
| `data` | JSON loading: `GameContentLoader` (validates, fails with file-named `DataException`), `GameContent` (`create(id, level)`), `FighterDef`, `ItemDef`, `GameTuning`, `SkillRegistry` (skill id → Java class), enums `Stat`, `ItemSlot`, `Rarity`, `EnemyRank` |
| `core.character` | `Character` (level, XP, `applyLevel`), `DamageResult`; `Attributes/` stats, profile, state; `arsenal/` equipment, skills |
| `gameplay.combat` | `TurnManager`, `ActionOrderResolver`, `CombatEngine`, `EventSystem` (event queue), `Duel`/`DuelOutcome`, `ActionChooser` |
| `gameplay.log` | `CombatLog` (`info` player-facing, `debug` details), `ConsoleCombatLog`, `CombatLog.SILENT` |
| `gameplay.mental` | 5 personalities (Rage, Faith, Calm, Honour, Instinct): `AbstractMentalState.deltaFor(event)` |
| `gameplay.ai` | `SimpleAiChooser` (defend with probability, else random own skill; `Random` injected) |
| `progression` | `LevelCurve`, `CombatXp`, `Progression` (grant XP / level up), `StatProfile`, `LevelUp` |
| `Skills` | `Actions/` Offensive/DefensiveAction; `OffensiveSkills/` Attack, Radiation Strike, Sacrificial Strike; `DefensiveSkills/` Guard, Evade, Counter; `Scaling/` damage types, scaling, `DamageResolver` |
| `effects` | `StatusEffect`, Burn, Bleed, `StatusEffectFactory` (damage type → status) |
| `Weapons.Weapon` | Weapon (power, damage types, range) |

Empty placeholder packages: `inventory`, `important_directory_TODOS`.

## Data (`src/main/resources/data/`)
`items.json` (weapons), `classes.json` (heroes: Core Paladin, Mutant Scout, Electric Mutant), `enemies.json` (Irradiated Gnoll, Mutant Rat; have `rank`), `tuning.json` (levels, XP, `combat.evade`). Unknown fields/ids fail at load. Skills, status effects, mental states are still Java (planned JSON in `docs/data-format.md`).

## Combat rules (as coded)
- **Turn**: each fighter plans up to `getActionsPerTurn()` (=1) actions, max 1 defense → `resolveCurrentTurn()` sorts by priority (defense 100, attack 1, then rolled speed) → `endTurn(chars)`: status ticks, expiry, defenses end.
- **Damage**: skill raw = Σ scaling (STR×1.5, WEAPON power×resonance, SKILL basePower) × attacker mental multiplier → resistances product (`DamageResolver`) → defender: active defense, else −vigor.
- **Defenses** (one turn, consumed by the hit): Guard = raw − 2×vigor, × incoming mental; Evade = relative dodge `0.8 × agiDef/(agiDef+agiAtk) + 0.005×luck`, clamped 5–75%; Counter = raw − vigor/2, then counter-hit (vigor + weapon×resonance), once.
- **Mental**: value −20..20; dmg ×(1+0.4·tanh(v/10)), incoming ×(1−0.3·tanh(v/10)). Attacker gains DAMAGE_DEALT only if HP was lost (half if a defense mitigated). Every defense emits DEFENSE_PREPARED (−1).
- **Status**: FIRE→Burn 4/turn 2t, BLEEDING→Bleed 5/turn 3t; same status adds duration; ticks use `takeStatusDamage` (ignore defense and vigor).

## Progression (as coded; spec in `design/gdd/progression.md`)
Stats = base + floor(growth×(L−1)); growth = 10% of base per level for all stats except luck/speed. XP to next = round(100·L^1.5); reward unit = round(65·L^1.1); enemy XP = unit × rank (0.05/0.15/0.5) × level-gap modifier (gray below L−(3+L/10) → 0, −20%/level below −2, 120% at +3). Quests, style bonus, equipment, clans: not coded.

## Conventions (must follow)
- Ask before writing files (project `CLAUDE.md` protocol); no commit/merge/push without the user's instruction.
- Git: work on `feature/…` or `fix/…`, merge with `--no-ff` after approval. Conventional Commits in English (`feat`, `fix`, `docs`, `test`, `refactor`, `chore`), detailed body, `Co-Authored-By` trailer.
- Language: game text/data English; design docs, code comments and replies to the owner in French.
- Gameplay values live in JSON, never hard-coded. Logic never prints: use `CombatLog`.
- Tests: deterministic (inject `Random`; `testsupport/FixedRandom`, `TestCharacters`, `RecordingCombatLog`); one regression test per fixed bug.

## Design docs (read only the section you need)
- `design/gdd/progression.md` — levels, 8 acts, XP sources, loot, gear outline, open questions.
- `docs/data-format.md` — full planned JSON format and effect building blocks.
- `Tab 1.md` — world bible (vision, clans).

## Known gaps / next candidates
Burn/Bleed damage does not scale with level · Sacrificial Strike too weak (STR only) · `DuelTest.enemyWinsWhenPlayerFalls` sits at its 5-turn limit · skills/status/mental states to move to JSON · not built: equipment (8 slots, item level), quests, enemy Codex (unknown abilities revealed by use; intents only for known ones), clans, per-personality defense effects, style bonus.

Check `git log` and `git branch` for current state; this file does not track it.
Plan and pending owner decisions: `docs/NEXT_STEPS.md` (read next; update at session end).
